/*
Copyright (C) 2013 Modelon AB

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

#include "types/VariableType.hpp"
using std::ostream;
namespace ModelicaCasADi 
{
void VariableType::print(ostream& os) const { 
    os << getName() << " type (";
    std::string sep("");
    for(attributeMap::const_iterator it = attributes.begin(); it != attributes.end(); ++it){
        os << sep << (it->first) << " = ";
        (it->second).print(os);
        sep = ", ";
    }
    os << ");";
}
}; // End namespace
