
/*
Copyright (C) 2009 Modelon AB

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

/** \file CGenerator.java
*  \brief CGenerator class.
*/

package org.jmodelica.codegen;

import java.io.*;

import org.jmodelica.ast.*;

/**
 * OptimicaCGenerator extends the functionality of CGenerator to also
 * include C code generation of Optimica quantities.
 *
 */
public class OptimicaCGenerator extends CGenerator {
	
	class OptTag_C_numPathEqConstraints extends OptTag {
		
		public OptTag_C_numPathEqConstraints(AbstractGenerator myGenerator, 
		  FOptClass foptclass) {
			super("n_ceq","Number of path equality constraints",
			  myGenerator,foptclass);
		}
	
		public void generate(PrintStream genPrinter) {
			genPrinter.print(foptclass.numPathEqConstraints());
		}	
	}

	class OptTag_C_numPathIneqConstraints extends OptTag {
		
		public OptTag_C_numPathIneqConstraints(AbstractGenerator myGenerator, 
		  FOptClass foptclass) {
			super("n_cineq","Number of path inequality constraints",
			  myGenerator,foptclass);
		}
	
		public void generate(PrintStream genPrinter) {
			genPrinter.print(foptclass.numPathLeqConstraints() + " + " + 
					foptclass.numPathGeqConstraints());
		}	
	}

	class OptTag_C_numPointEqConstraints extends OptTag {
		
		public OptTag_C_numPointEqConstraints(AbstractGenerator myGenerator, 
		  FOptClass foptclass) {
			super("n_heq","Number of point equality constraints",
			  myGenerator,foptclass);
		}
	
		public void generate(PrintStream genPrinter) {
			genPrinter.print(foptclass.numPointEqConstraints());
		}	
	}

	class OptTag_C_numPointIneqConstraints extends OptTag {
		
		public OptTag_C_numPointIneqConstraints(AbstractGenerator myGenerator, 
		  FOptClass foptclass) {
			super("n_hineq","Number of point inequality constraints",
			  myGenerator,foptclass);
		}
	
		public void generate(PrintStream genPrinter) {
			genPrinter.print(foptclass.numPointLeqConstraints() + " + " + 
					foptclass.numPointGeqConstraints());
		}	
	}

	class OptTag_C_numTimePoints extends OptTag {
		
		public OptTag_C_numTimePoints(AbstractGenerator myGenerator, 
		  FOptClass foptclass) {
			super("n_tp","Number of point time points",
			  myGenerator,foptclass);
		}
	
		public void generate(PrintStream genPrinter) {
			genPrinter.print(foptclass.numTimePoints());
		}	
	}
	
	class OptTag_C_pointVariableAliases extends OptTag {
		
		public OptTag_C_pointVariableAliases(AbstractGenerator myGenerator, 
		  FOptClass foptclass) {
			super("C_point_variable_aliases","C: macros for C point variable aliases",
			  myGenerator,foptclass);
		}
	
		public void generate(PrintStream genPrinter) {
			for (FVariable fv : foptclass.derivativeVariables()) {
				genPrinter.print("#define _" + fv.nameUnderscore());
				genPrinter.print("_p_(j) ((*(jmi->z))[jmi->offs_dx_p+" + 
						"j*(jmi->n_dx + jmi->n_x + jmi->n_u + jmi->n_w) +" +
						fv.derivativeVariableIndex() + "])\n");
			}
			for (FVariable fv : foptclass.differentiatedRealVariables()) {
				genPrinter.print("#define _" + fv.nameUnderscore());
				genPrinter.print("_p_(j) ((*(jmi->z))[jmi->offs_x_p+" + 
						"j*(jmi->n_dx + jmi->n_x + jmi->n_u + jmi->n_w) +" +
						fv.differentiatedRealVariableIndex() + "])\n");
			}
			for (FVariable fv : foptclass.realInputs()) {
				genPrinter.print("#define _" + fv.nameUnderscore());
				genPrinter.print("_p_(j) ((*(jmi->z))[jmi->offs_u_p+" + 
						"j*(jmi->n_dx + jmi->n_x + jmi->n_u + jmi->n_w) +" +
						fv.realInputIndex() + "])\n");
			}
			for (FVariable fv : foptclass.algebraicRealVariables()) {
				genPrinter.print("#define _" + fv.nameUnderscore());
				genPrinter.print("_p_(j) ((*(jmi->z))[jmi->offs_w_p+" +
						"j*(jmi->n_dx + jmi->n_x + jmi->n_u + jmi->n_w) +" +
						fv.algebraicRealVariableIndex() + "])\n");
			}
		}
	}

	class OptTag_C_CostFunction extends OptTag {
		
		public OptTag_C_CostFunction(AbstractGenerator myGenerator, 
		  FOptClass foptclass) {
			super("C_Opt_cost_function","C: cost function",
			  myGenerator,foptclass);
		}
	
		public void generate(PrintStream genPrinter) {
			genPrinter.print("   (*res)[0] = ");
			foptclass.objectiveExp().prettyPrint_C(genPrinter,"");
			genPrinter.print(";\n");
		}
	}
	
	class OptTag_C_PathEqualityConstraints extends OptTag {
	
		public OptTag_C_PathEqualityConstraints(AbstractGenerator myGenerator, 
		  FOptClass foptclass) {
			super("C_Opt_path_equality_constraints","C: path equality constraints",
			  myGenerator,foptclass);
		}
	
		public void generate(PrintStream genPrinter) {
			int i=0;
			for (FConstraintEq c : foptclass.pathEqConstraints()) {
				c.genResidual_C(i,"    ",genPrinter);				
				i++;
			}
		}	
	}

	class OptTag_C_PathInequalityConstraints extends OptTag {
		
		public OptTag_C_PathInequalityConstraints(AbstractGenerator myGenerator, 
		  FOptClass foptclass) {
			super("C_Opt_path_inequality_constraints","C: path inequality constraints (leq)",
			  myGenerator,foptclass);
		}
	
		public void generate(PrintStream genPrinter) {
			int i=0;
			for (FConstraintLeq c : foptclass.pathLeqConstraints()) {
				c.genResidual_C(i,"    ",genPrinter);				
				i++;
			}
			for (FConstraintGeq c : foptclass.pathGeqConstraints()) {
				c.genResidual_C(i,"    ",genPrinter);				
				i++;
			}
		}	
	}

	class OptTag_C_PointEqualityConstraints extends OptTag {
		
		public OptTag_C_PointEqualityConstraints(AbstractGenerator myGenerator, 
		  FOptClass foptclass) {
			super("C_Opt_point_equality_constraints","C: point equality constraints",
			  myGenerator,foptclass);
		}
	
		public void generate(PrintStream genPrinter) {
			int i=0;
			for (FConstraintEq c : foptclass.pointEqConstraints()) {
				c.genResidual_C(i,"    ",genPrinter);				
				i++;
			}
		}	
	}

	class OptTag_C_PointInequalityConstraints extends OptTag {
		
		public OptTag_C_PointInequalityConstraints(AbstractGenerator myGenerator, 
		  FOptClass foptclass) {
			super("C_Opt_point_inequality_constraints","C: point inequality constraints (leq)",
			  myGenerator,foptclass);
		}
	
		public void generate(PrintStream genPrinter) {
			int i=0;
			for (FConstraintLeq c : foptclass.pointLeqConstraints()) {
				c.genResidual_C(i,"    ",genPrinter);				
				i++;
			}
			for (FConstraintGeq c : foptclass.pointGeqConstraints()) {
				c.genResidual_C(i,"    ",genPrinter);				
				i++;
			}
		}	
	}

	
	/**
	 * Constructor.
	 * 
	 * @param expPrinter Printer object used to generate code for expressions.
	 * @param escapeCharacter Escape characters used to decode tags.
	 * @param fclass An FClass object used as a basis for the code generation.
	 */
	public OptimicaCGenerator(Printer expPrinter, char escapeCharacter,
			FOptClass fclass) {
		super(expPrinter,escapeCharacter, fclass);

		// Create tags			
		AbstractTag tag = null;
		
		tag = new OptTag_C_numPathEqConstraints(this,fclass);
		tagMap.put(tag.getName(),tag);

		tag = new OptTag_C_numPathIneqConstraints(this,fclass);
		tagMap.put(tag.getName(),tag);

		tag = new OptTag_C_numPointEqConstraints(this,fclass);
		tagMap.put(tag.getName(),tag);

		tag = new OptTag_C_numPointIneqConstraints(this,fclass);
		tagMap.put(tag.getName(),tag);

		tag = new OptTag_C_numTimePoints(this,fclass);
		tagMap.put(tag.getName(),tag);

		tag = new OptTag_C_pointVariableAliases(this,fclass);
		tagMap.put(tag.getName(),tag);
		
		tag = new OptTag_C_CostFunction(this,fclass);
		tagMap.put(tag.getName(),tag);

		tag = new OptTag_C_PathEqualityConstraints(this,fclass);
		tagMap.put(tag.getName(),tag);

		tag = new OptTag_C_PathInequalityConstraints(this,fclass);
		tagMap.put(tag.getName(),tag);

		tag = new OptTag_C_PointEqualityConstraints(this,fclass);
		tagMap.put(tag.getName(),tag);

		tag = new OptTag_C_PointInequalityConstraints(this,fclass);
		tagMap.put(tag.getName(),tag);

	}
}

