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



/** \file fmi1_import_type.h
*  \brief Public interface to the FMI XML C-library: variable types handling.
*/

#ifndef FMI1_IMPORT_TYPE_H_
#define FMI1_IMPORT_TYPE_H_

#include "FMI1/fmi1_import.h"

#ifdef __cplusplus
extern "C" {
#endif

/* \defgroup Support for processing variable types
*  @{
*/

fmi1_import_display_unit_t* fmi1_import_get_type_display_unit(fmi1_import_real_typedef_t*);

fmi1_import_type_definitions_t* fmi1_import_get_type_definitions(fmi1_import_t* );

/* Base types used in type definitions */
typedef enum fmi1_import_base_type_enu_t
{
        fmi1_import_base_type_enu_real,
        fmi1_import_base_type_enu_int,
        fmi1_import_base_type_enu_bool,
        fmi1_import_base_type_enu_str,
        fmi1_import_base_type_enu_enum
} fmi1_import_base_type_enu_t;

/* Convert base type constant to string */
const char* fmi1_import_base_type2string(fmi1_import_base_type_enu_t bt);

size_t fmi1_import_get_type_definition_number(fmi1_import_type_definitions_t* td);

fmi1_import_variable_typedef_t* fmi1_import_get_typedef(fmi1_import_type_definitions_t* td, unsigned int  index);

const char* fmi1_import_get_type_name(fmi1_import_variable_typedef_t*);

/* Note that NULL pointer is returned if the attribute is not present in the XML.*/
const char* fmi1_import_get_type_description(fmi1_import_variable_typedef_t*);

fmi1_import_base_type_enu_t fmi1_import_get_base_type(fmi1_import_variable_typedef_t*);

/* Boolean and String has no extra attributes -> not needed*/

fmi1_import_real_typedef_t* fmi1_import_ret_type_as_real(fmi1_import_variable_typedef_t*);
fmi1_import_integer_typedef_t* fmi1_import_get_type_as_int(fmi1_import_variable_typedef_t*);
fmi1_import_enumeration_typedef_t* fmi1_import_get_type_as_enum(fmi1_import_variable_typedef_t*);

/* Note that NULL-pointer is always returned for strings and booleans */
const char* fmi1_import_get_type_quantity(fmi1_import_variable_typedef_t*);

double fmi1_import_get_real_type_min(fmi1_import_real_typedef_t*);
double fmi1_import_get_real_type_max(fmi1_import_real_typedef_t*);
double fmi1_import_get_real_type_nominal(fmi1_import_real_typedef_t*);
fmi1_import_unit_t* fmi1_import_get_real_type_unit(fmi1_import_real_typedef_t*);
int fmi1_import_get_real_type_is_relative_quantity(fmi1_import_real_typedef_t*);

int fmi1_import_get_integer_type_min(fmi1_import_integer_typedef_t*);
int fmi1_import_get_integer_type_max(fmi1_import_integer_typedef_t*);

int fmi1_import_get_enum_type_min(fmi1_import_enumeration_typedef_t*);
int fmi1_import_get_enum_type_max(fmi1_import_enumeration_typedef_t*);
unsigned int  fmi1_import_get_enum_type_size(fmi1_import_enumeration_typedef_t*);
const char* fmi1_import_get_enum_type_item_name(fmi1_import_enumeration_typedef_t*, unsigned int  item);
const char* fmi1_import_get_enum_type_item_description(fmi1_import_enumeration_typedef_t*, unsigned int  item);

/*
*  @}
*/
#ifdef __cplusplus
}
#endif
#endif
