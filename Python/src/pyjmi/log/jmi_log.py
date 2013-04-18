#!/usr/bin/env python 
# -*- coding: utf-8 -*-

# Copyright (C) 2013 Modelon AB
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Lesser General Public License as published by
# the Free Software Foundation, version 3 of the License.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public License
# along with this program. If not, see <http://www.gnu.org/licenses/>.
"""
Utilities to postprocess and analyze JMI logs
"""

def update_jacs_scalings(jacs, jacs_updated, scalings, scalings_updated, namednode):
    name, node = namednode.name, namednode.value
    if name == 'JacobianUpdated':
        block = node.block
        jacs[block] = node.jacobian
        jacs_updated[block] = True
    elif name == 'ScalingUpdated':
        block = node.block
        scalings[block] = node.scaling
        scalings_updated[block] = True

def gather_solves(log):
    """Attempts to emulate the old structured log.

    Takes a log root node and returns a list of solves, marked up with a block_solves list.
    Each block_solve is marked up with an iterations list and initial_residual_scaling.
    Each iteration is marked up with jacobian and residual_scaling.
    """
    jacs = {}
    jacs_updated = {}
    scalings = {}
    scalings_updated = {}

    solves = log.children('EquationSolve')    
    for solve in solves:
        block_solves = []

        for bl_node in solve.children(('NewtonSolve', 'JacobianUpdated', 'ScalingUpdated')):
            if bl_node.name == 'NewtonSolve':
                block_solve = bl_node.value
                block_solves.append(block_solve)
                block_solve['iterations'] = iterations = []

                block_index = block_solve.block

                block_solve['initial_residual_scaling'] = scalings[block_index]
                block_solve['initial_residual_scaling_updated'] = scalings_updated[block_index]
                scalings_updated[block_index] = False

                for it_node in block_solve.children(('KinsolInfo', 'JacobianUpdated', 'ScalingUpdated')):
                    if it_node.name == 'KinsolInfo':
                        if 'iteration_index' in it_node.value:
                            iteration = it_node.value

                            if block_index in jacs:
                                iteration['jacobian'] = jacs[block_index]
                                iteration['jacobian_updated'] = jacs_updated[block_index]
                                jacs_updated[block_index] = False
                            if block_index in scalings:
                                iteration['residual_scaling'] = scalings[block_index]
                                iteration['residual_scaling_updated'] = scalings_updated[block_index]
                            scalings_updated[block_index] = False
                            
                            iterations.append(it_node.value)
                    else:
                        update_jacs_scalings(jacs, jacs_updated, scalings, scalings_updated, it_node)
            else:
                update_jacs_scalings(jacs, jacs_updated, scalings, scalings_updated, bl_node)            
        solve['block_solves'] = block_solves
        
    return solves