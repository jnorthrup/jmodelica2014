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

#ifndef MXWRAP_HPP
#define MXWRAP_HPP

#include "symbolic/casadi.hpp"
#include "casadi/MX.h"

typedef casadi::MX JMX;

inline CasADi::MX toMX(const JMX &jmx) {
    jlong p = JMX::getCPtr(jmx);
    return **(CasADi::MX **)&p;
}
#ifdef org_jmodelica_modelica_compiler_FExp_H  // if JCC-generated FExp.h included:
inline CasADi::MX toMX(const org::jmodelica::modelica::compiler::FExp &ex) { return toMX(ex.toMX()); }
#endif
#ifdef org_jmodelica_optimica_compiler_FExp_H  // if JCC-generated FExp.h included:
inline CasADi::MX toMX(const org::jmodelica::optimica::compiler::FExp &ex) { return toMX(ex.toMX()); }
#endif

inline JMX toJMX(const CasADi::MX &ex) {
    CasADi::MX *px = new CasADi::MX(ex);
    return JMX(*(jlong *)&px, (jboolean)true);
}

#endif
