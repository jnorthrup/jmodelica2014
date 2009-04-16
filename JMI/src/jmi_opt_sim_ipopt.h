 /*
    Copyright (C) 2009 Modelon AB

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License version 3 as published
    by the Free Software Foundation, or optionally, under the terms of the
    Common Public License version 1.0 as published by IBM.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License, or the Common Public License, for more details.

    You should have received copies of the GNU General Public License
    and the Common Public License along with this program.  If not,
    see <http://www.gnu.org/licenses/> or
    <http://www.ibm.com/developerworks/library/os-cpl.html/> respectively.
*/


/** \file jmi_opt_sim_ipopt.h
 *  \brief An interface between the NLP representation provided by jmi_opt_sim
 *  and IPOPT.
 **/

/**
 * min f(x)
 *
 * s.t.
 *
 * g_lb <= g(x) <= g_ub
 *
 */
#ifndef _JMI_OPT_SIM_IPOPT_H
#define _JMI_OPT_SIM_IPOPT_H

#include "jmi.h"
#include "jmi_opt_sim.h"
#include <IpStdCInterface.h>

#ifdef __cplusplus
extern "C" {
#endif


typedef struct {
	jmi_opt_sim_t *jmi_opt_sim;
//	jmi_real_t *x;
	Index n;
	Index m;
	Index dg_n_nz;
	Index *dg_row;
	Index *dg_col;
	Index hess_lag_n_nz;
	jmi_real_t *g_lb;
	jmi_real_t *g_ub;
	IpoptProblem nlp;

	enum ApplicationReturnStatus status;
	jmi_real_t objective;
	jmi_real_t *g;
	jmi_real_t *mult_g;
	jmi_real_t *mult_x_lb;
	jmi_real_t *mult_x_ub;

} jmi_opt_sim_ipopt_t;

int jmi_opt_sim_ipopt_new(jmi_opt_sim_ipopt_t **jmi_opt_sim_ipopt, jmi_opt_sim_t *jmi_opt_sim);

int jmi_opt_sim_ipopt_set_initial_point(jmi_opt_sim_ipopt_t *jmi_opt_sim_ipopt, jmi_real_t *x_init);

int jmi_opt_sim_ipopt_solve(jmi_opt_sim_ipopt_t *jmi_opt_sim_ipopt);

#ifdef __cplusplus
}
#endif

#endif /* _JMI_OPT_SIM_IPOPT_H */
