#!/usr/bin/env python 
# -*- coding: utf-8 -*-

# Copyright (C) 2011 Modelon AB
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, version 3 of the License.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program. If not, see <http://www.gnu.org/licenses/>.

# Import library for path manipulations
import os.path

# Import numerical libraries
import numpy as N
import matplotlib.pyplot as plt

# Import the JModelica.org Python packages
from jmodelica.casadi_interface import compile_casadi, CasadiModel

def run_demo(with_plots=True, graph="SX"):
    """Demonstrate how to optimize a Van der Pol oscillator."""
    curr_dir = os.path.dirname(os.path.abspath(__file__));
    jn = compile_casadi("VDP_pack.VDP_Opt2", curr_dir + "/files/VDP.mop")
    model = CasadiModel(jn)

    opts = model.optimize_options(algorithm="CasadiRadau2")
    opts['n_e'] = 80
    opts['n_cp'] = 1
    opts['graph'] = graph

    res = model.optimize(algorithm="CasadiRadau2", options=opts)
    
    # Extract variable profiles
    x1 = res['x1']
    x2 = res['x2']
    u = res['u']
    time = res['time']
    
    if with_plots:
        # Plot
        plt.figure(1)
        plt.clf()
        plt.subplot(311)
        plt.plot(time,x1)
        plt.grid()
        plt.ylabel('x1')
        
        plt.subplot(312)
        plt.plot(time,x2)
        plt.grid()
        plt.ylabel('x2')
        
        plt.subplot(313)
        plt.plot(time[1:],u[1:])
        plt.grid()
        plt.ylabel('u')
        plt.xlabel('time')
        plt.show()

if __name__ == "__main__":
    run_demo()