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

#ifdef __cplusplus 
extern "C" {
#endif

#include <stdlib.h>
#include <jm_types.h>
#include "miniunz.h"

jm_status_enu_t fmi_zip_unzip(const char* zip_file_path, const char* output_folder)
{
	/*
	Usage : miniunz [-e] [-x] [-v] [-l] [-o] [-p password] file.zip [file_to_extr.] [-d extractdir]
	  -e  Extract without pathname (junk paths)
	  -x  Extract with pathname
	  -v  list files
	  -l  list files
	  -d  directory to extract into
	  -o  overwrite files without prompting
	  -p  extract crypted file using password
	*/

	int argc = 5;
	const char *argv[5];
	argv[0]="miniunz";
	argv[1]="-o";
	argv[2]=zip_file_path;
	argv[3]="-d";
	argv[4]=output_folder;

	if (miniunz(argc, (char**)argv) == 0) {
		return jm_status_success;
	} else {
		return jm_status_error;	
	}
}

#ifdef __cplusplus 
}
#endif