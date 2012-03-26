/*
    Copyright (C) 2012 Modelon AB

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, version 3 of the License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

#include <string.h>
#include <stdio.h>
#include <stdarg.h>

#include "fmi_xml_context_impl.h"

fmi_xml_context_t* fmi_xml_allocate_context( jm_callbacks* callbacks) {
	jm_callbacks* cb;
	fmi_xml_context_t* c;

    if(callbacks) {
        cb = callbacks;
    }
    else {
        cb = jm_get_default_callbacks();
    }
    c = cb->malloc(sizeof(fmi_xml_context_t));
    if(!c) return 0;
	c->callbacks = callbacks;
	c->parser = 0;
	c->fmi_version = fmi_version_unknown_enu;
 
    return c;
}

void fmi_xml_free_context(fmi_xml_context_t *context) {
    if(!context) return;
    if(context->parser) {
        XML_ParserFree(context->parser);
        context->parser = 0;
    }
    context->callbacks->free(context);
}

void fmi_xml_error(fmi_xml_context_t *context, const char* fmt, ...) {
    va_list args;
    const char * module = "FMIXML";
    va_start (args, fmt);

	vsprintf(context->errMessageBuf, fmt, args);
    if(context->callbacks->logger)
        context->callbacks->logger(context, module, 0, "ERROR", context->errMessageBuf);
    va_end (args);

	XML_StopParser(context->parser,0);
}

void XMLCALL fmi_xml_parse_element_start(void *c, const char *elm, const char **attr) {
	fmi_xml_context_t *context = (fmi_xml_context_t*)c;
	const char* fmiVersion = 0;
	int i = 0;

	if(strcmp(elm, "fmiModelDescription") != 0) {
		fmi_xml_error(context, "First element in XML must be fmiModelDescription");
		return;
	}
	while(attr[i]) {
		if(strcmp(attr[i], "fmiVersion") == 0) {
			fmiVersion = attr[i+1];
			break;
		}
		i+=2;
	}
	if(!fmiVersion) {
		fmi_xml_error(context, "Could not find fmiVersion attribute in the XML. Cannot proceed.");
		return;
	}
	if( strcmp(fmiVersion, "1.0") == 0 ) {
		context->fmi_version = fmi_version_1_enu;
		XML_StopParser(context->parser,0);
		return;
	}
	else {
		fmi_xml_error(context, "This version of FMI standard is not supported (fmiVersion=%s)", fmiVersion);
		return;
	}
}

void XMLCALL fmi_xml_parse_element_end(void* c, const char *elm) {
}

void XMLCALL fmi_xml_parse_element_data(void* c, const XML_Char *s, int len) {
}

fmi_version_enu_t fmi_xml_get_fmi_version(fmi_xml_context_t* context, const char* filename) {
    XML_Memory_Handling_Suite memsuite;
    XML_Parser parser = NULL;
    FILE* file;

    memsuite.malloc_fcn = context->callbacks->malloc;
    memsuite.realloc_fcn = context->callbacks->realloc;
    memsuite.free_fcn = context->callbacks->free;
    context -> parser = parser = XML_ParserCreate_MM(0, &memsuite, 0);

    if(! parser) {
        fmi_xml_error(context, "Could not initialize XML parsing library.");
        fmi_xml_free_context(context);
        return fmi_version_unknown_enu;
    }

    XML_SetUserData( parser, context);

    XML_SetElementHandler(parser, fmi_xml_parse_element_start, fmi_xml_parse_element_end);

    XML_SetCharacterDataHandler(parser, fmi_xml_parse_element_data);

    file = fopen(filename, "rb");
    if (file == NULL) {
        fmi_xml_error(context, "Cannot open file '%s' for parsing", filename);
        return fmi_version_unknown_enu;
    }

	context->fmi_version = fmi_version_unknown_enu;

#define XML_BLOCK_SIZE 1000

    while (!feof(file)) {
        char text[XML_BLOCK_SIZE];
        int n = fread(text, sizeof(char), XML_BLOCK_SIZE, file);
        if(ferror(file)) {
            fmi_xml_error(context, "Error reading from file %s", filename);
            fclose(file);
            return fmi_version_unknown_enu;
        }
        if (!XML_Parse(parser, text, n, feof(file)) && (context->fmi_version == fmi_version_unknown_enu)) {
             fmi_xml_error(context, "Parse error in file %s at line %d:\n%s",
                          filename,
                         (int)XML_GetCurrentLineNumber(parser),
                         XML_ErrorString(XML_GetErrorCode(parser)));
             fclose(file);
             return fmi_version_unknown_enu; /* failure */
        }
		if(context->fmi_version != fmi_version_unknown_enu) break;
    }
    fclose(file);

	if(context->fmi_version == fmi_version_unknown_enu) {
             fmi_xml_error(context, "Could not detect FMI standard version");
	}

    return context->fmi_version;
}