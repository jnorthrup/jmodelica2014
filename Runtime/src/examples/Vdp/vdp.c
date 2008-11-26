// Example of generated function


#include <stdio.h>
#include <stdlib.h>
#include "../../Jmi/jmi.h"

/*
#if defined __cplusplus
        extern "C" {
#endif
*/

static const int N_ci = 0;
static const int N_cd = 0;
static const int N_pi = 1;
static const int N_pd = 0;
static const int N_dx = 3;
static const int N_x = 3;
static const int N_u = 1;
static const int N_w = 0;
static const int N_eq_F = 3;

#define ci(i) ((*(jmi->z))[jmi->offs_ci+i])
#define cd(i) ((*(jmi->z))[jmi->offs_cd+i])
#define pi(i) ((*(jmi->z))[jmi->offs_pi+i])
#define pd(i) ((*(jmi->z))[jmi->offs_pd+i])
#define dx(i) ((*(jmi->z))[jmi->offs_dx+i])
#define x(i) ((*(jmi->z))[jmi->offs_x+i])
#define u(i) ((*(jmi->z))[jmi->offs_u+i])
#define w(i) ((*(jmi->z))[jmi->offs_w+i])
#define tt ((*(jmi->z))[jmi->offs_t])

	  // TODO: This does not work...
//static jmi_dae_F_t vdp_dae_F(jmi_t* jmi, jmi_ad_var_vec_t res) {

static int vdp_dae_F(jmi_t* jmi, jmi_ad_var_vec_p res) {

  printf("vdp_dae_F: start\n");
/*
	jmi_real_t* ci;
	jmi_real_t* cd;
	jmi_real_t* pi;
	jmi_real_t* pd;
	jmi_real_t* dx;
	jmi_real_t* x;
	jmi_real_t* u;
	jmi_real_t* w;
	jmi_real_t* t_;
	jmi_real_t t;

	jmi_get_ci(jmi, &ci);
	jmi_get_cd(jmi, &cd);
	jmi_get_pi(jmi, &pi);
	jmi_get_pd(jmi, &pd);
	jmi_get_dx(jmi, &dx);
	jmi_get_x(jmi, &x);
	jmi_get_u(jmi, &u);
	jmi_get_w(jmi, &w);
	jmi_get_t(jmi, &t_);

	t = t_[0];

	res[0] = (1-x[1]*x[1])*x[0] - x[1] + u[0] - dx[0];
	res[1] = pi[0]*x[0] - dx[1];
	res[2] = x[0]*x[0] + x[1]*x[1] + u[0]*u[0] - dx[2];
*/

	// Copy the values that are set by the user to the "active"
	// vector. This probably gives a performance penalty, but it
	// is necessary in order to have generated code that can
	// be compiled both with and without AD.

  //  #if JMI_AD == JMI_AD_CPPAD
    // int i;
  // for (i=0;i<jmi->n_z;i++) {
  //  printf("*--* %f\n",CppAD::Value((*(jmi->z))[i]));
  // }
  //#endif



  (*res)[0] = (1-x(1)*x(1))*x(0) - x(1) + u(0) - dx(0);
  (*res)[1] = pi(0)*x(0) - dx(1);
  (*res)[2] = x(0)*x(0) + x(1)*x(1) + u(0)*u(0) - dx(2);

	/*
   #if JMI_AD == JMI_AD_CPPAD
   double qwe;  
   int i;
   for (i=0;i<jmi->n_z;i++) {
  
     qwe = res[i];
  printf("*--* %f\n",qwe);
   }
 #endif
	*/

  printf("vdp_dae_F: stop\n");

	/*	
      	printf("vdp_dae_F: res[0] = %f\n",res[0]);
	printf("vdp_dae_F: res[1] = %f\n",res[1]);
	printf("vdp_dae_F: res[2] = %f\n",res[2]);
	*/
	return 0;
}

/*
 * TODO: This code can certainly be improved and optimized. For example, macros would probably
 * make it easier to read.
 */

// TODO: This does not work...
//static jmi_dae_dF_t vdp_dae_jac_sd_F(jmi_t* jmi, int sparsity, int skip, int* mask, jmi_real_vec_t jac) {

static int vdp_dae_jac_sd_F(jmi_t* jmi, int sparsity, int skip, int* mask, jmi_real_vec_t jac) {

	jmi_real_t* ci;
	jmi_real_t* cd;
	jmi_real_t* pi;
	jmi_real_t* pd;
	jmi_real_t* dx;
	jmi_real_t* x;
	jmi_real_t* u;
	jmi_real_t* w;
	jmi_real_t* t_;
	jmi_real_t t;

	jmi_get_ci(jmi, &ci);
	jmi_get_cd(jmi, &cd);
	jmi_get_pi(jmi, &pi);
	jmi_get_pd(jmi, &pd);
	jmi_get_dx(jmi, &dx);
	jmi_get_x(jmi, &x);
	jmi_get_u(jmi, &u);
	jmi_get_w(jmi, &w);
	jmi_get_t(jmi, &t_);

	t = t_[0];

	int i;
	int jac_n = N_eq_F;
	int jac_m = 0;
	int col_index = 0;

	if (!(skip & JMI_DER_PI_SKIP)) {
		for (i=0;i<N_pi;i++) {
			jac_m += mask[col_index++];
		}
	}
	if (!(skip & JMI_DER_PD_SKIP)) {
		for (i=0;i<N_pd;i++) {
			jac_m += mask[col_index++];
		}
	}
	if (!(skip & JMI_DER_DX_SKIP)) {
		for (i=0;i<N_dx;i++) {
			jac_m += mask[col_index++];
		}
	}
	if (!(skip & JMI_DER_X_SKIP)) {
		for (i=0;i<N_x;i++) {
			jac_m += mask[col_index++];
		}
	}
	if (!(skip & JMI_DER_U_SKIP)) {
		for (i=0;i<N_u;i++) {
			jac_m += mask[col_index++];
		}
	}
	if (!(skip & JMI_DER_W_SKIP)) {
		for (i=0;i<N_w;i++) {
			jac_m += mask[col_index++];
		}
	}

	// Set Jacobian to zero if dense evaluation.
	if ((sparsity & JMI_DER_DENSE_ROW_MAJOR) | (sparsity & JMI_DER_DENSE_COL_MAJOR)) {
		for (i=0;i<jac_n*jac_m;i++) {
			jac[i] = 0;
		}
	}

	int jac_index = 0;
	col_index = 0;
	if (!(skip & JMI_DER_PI_SKIP)) {
		if (mask[col_index++] == 1) {
			jmi_real_t jac_tmp_1 = x[0];
			switch (sparsity) {
			case JMI_DER_DENSE_COL_MAJOR:
				jac[jac_n*0 + 1] = jac_tmp_1;
				break;
			case JMI_DER_DENSE_ROW_MAJOR:
				jac[jac_m*1 + 0] = jac_tmp_1;
				break;
			case JMI_DER_SPARSE:
				jac[jac_index] = jac_tmp_1;
				jac_index++;
			}
		}
	} else {
		col_index += jmi->n_pi;
	}

	if (!(skip & JMI_DER_DX_SKIP)) {
		if (mask[col_index++] == 1) {
			jmi_real_t jac_tmp_1 = -1;
			switch (sparsity) {
			case JMI_DER_DENSE_COL_MAJOR:
				jac[jac_n*1 + 0] = jac_tmp_1;
				break;
			case JMI_DER_DENSE_ROW_MAJOR:
				jac[jac_m*0 + 1] = jac_tmp_1;
				break;
			case JMI_DER_SPARSE:
				jac[jac_index] = jac_tmp_1;
				jac_index++;
			}
		}
		if (mask[col_index++] == 1) {
			jmi_real_t jac_tmp_1 = -1;
			switch (sparsity) {
			case JMI_DER_DENSE_COL_MAJOR:
				jac[jac_n*2 + 1] = jac_tmp_1;
				break;
			case JMI_DER_DENSE_ROW_MAJOR:
				jac[jac_m*1 + 2] = jac_tmp_1;
				break;
			case JMI_DER_SPARSE:
				jac[jac_index] = jac_tmp_1;
				jac_index++;
			}
		}
		if (mask[col_index++] == 1) {
			jmi_real_t jac_tmp_1 = -1;
			switch (sparsity) {
			case JMI_DER_DENSE_COL_MAJOR:
				jac[jac_n*3 + 2] = jac_tmp_1;
				break;
			case JMI_DER_DENSE_ROW_MAJOR:
				jac[jac_m*2 + 3] = jac_tmp_1;
				break;
			case JMI_DER_SPARSE:
				jac[jac_index] = jac_tmp_1;
				jac_index++;
			}
		}
	} else {
		col_index += jmi->n_dx;
	}

	if (!(skip & JMI_DER_X_SKIP)) {
		if (mask[col_index++] == 1) {
			jmi_real_t jac_tmp_1 = (1-x[1]*x[1]);
			jmi_real_t jac_tmp_2 = pi[0];
			jmi_real_t jac_tmp_3 = 2*x[0];
			switch (sparsity) {
			case JMI_DER_DENSE_COL_MAJOR:
				jac[jac_n*4 + 0] = jac_tmp_1;
				jac[jac_n*4 + 1] = jac_tmp_2;
				jac[jac_n*4 + 2] = jac_tmp_3;
				break;
			case JMI_DER_DENSE_ROW_MAJOR:
				jac[jac_m*0 + 4] = jac_tmp_1;
				jac[jac_m*1 + 4] = jac_tmp_2;
				jac[jac_m*2 + 4] = jac_tmp_3;
				break;
			case JMI_DER_SPARSE:
				jac[jac_index++] = jac_tmp_1;
				jac[jac_index++] = jac_tmp_2;
				jac[jac_index++] = jac_tmp_3;
			}
		}
		if (mask[col_index++] == 1) {
			jmi_real_t jac_tmp_1 = -2*x[1]*x[0] - 1;
			jmi_real_t jac_tmp_2 = 2*x[1];
			switch (sparsity) {
			case JMI_DER_DENSE_COL_MAJOR:
				jac[jac_n*5 + 0] = jac_tmp_1;
				jac[jac_n*5 + 2] = jac_tmp_2;
				break;
			case JMI_DER_DENSE_ROW_MAJOR:
				jac[jac_m*0 + 5] = jac_tmp_1;
				jac[jac_m*2 + 5] = jac_tmp_2;
				jac_index += 3;
				break;
			case JMI_DER_SPARSE:
				jac[jac_index++] = jac_tmp_1;
				jac[jac_index++] = jac_tmp_2;
			}
		}
		if (mask[col_index++] == 1) {
		}
	} else {
		col_index += jmi->n_x;
	}

	if (!(skip & JMI_DER_U_SKIP)) {
		if (mask[col_index++] == 1) {
			jmi_real_t jac_tmp_1 = 1;
			jmi_real_t jac_tmp_2 = 2*u[0];
			switch (sparsity) {
			case JMI_DER_DENSE_COL_MAJOR:
				jac[jac_n*7 + 0] = jac_tmp_1;
				jac[jac_n*7 + 2] = jac_tmp_2;
				jac_index += 3;
				break;
			case JMI_DER_DENSE_ROW_MAJOR:
				jac[jac_m*0 + 7] = jac_tmp_1;
				jac[jac_m*2 + 7] = jac_tmp_2;
				break;
			case JMI_DER_SPARSE:
				jac[jac_index++] = jac_tmp_1;
				jac[jac_index++] = jac_tmp_2;
			}

		}
	} else {
		col_index += jmi->n_u;
	}

	return 0;
}

static int vdp_dae_jac_sd_F_n_nz(int* n_nz) {

	*n_nz = (1 + //pi
			0 + //pd
			3 + //dx
			5 + //x
			2 + //u
			0 + //w
			0 //t
			);

	return 0;
}

static int vdp_dae_jac_sd_F_nz_indices(int* row, int* col) {

//	int i,j;
	int jac_ind = 0;
	int col_ind = 0;

	// Jacobian for independent parameters
    //dF/dpd_1
	row[jac_ind] = 2;
	col[jac_ind++] = 1;
	col_ind += N_pi;

	// Jacobian for dependent parameters

	// Jacobian for derivatives
	//dF/ddx_1
	row[jac_ind] = 1;
	col[jac_ind++] = col_ind + 1;
	//dF/ddx_2
	row[jac_ind] = 2;
	col[jac_ind++] = col_ind + 2;
	//dF/ddx_3
	row[jac_ind] = 3;
	col[jac_ind++] = col_ind + 3;

	col_ind += N_dx;

	// Jacobian for states
	//dF/dx_1
	row[jac_ind] = 1;
	col[jac_ind++] = col_ind + 1;
	row[jac_ind] = 2;
	col[jac_ind++] = col_ind + 1;
	row[jac_ind] = 3;
	col[jac_ind++] = col_ind + 1;
	//dF/dx_2
	row[jac_ind] = 1;
	col[jac_ind++] = col_ind + 2;
	row[jac_ind] = 3;
	col[jac_ind++] = col_ind + 2;
	//dF/dx_3

	col_ind += N_x;

	// Jacobian for inputs
	//dF/du_2
	row[jac_ind] = 1;
	col[jac_ind++] = col_ind + 1;
	row[jac_ind] = 3;
	col[jac_ind++] = col_ind + 1;

	col_ind += N_u;

	// Jacobian for algebraics
	col_ind += N_w;

	// Jacobian for time

	/*
	// Template for dense Jacobian...

	// Jacobian for independent parameters
	for (j=col_ind;j<jmi->jmi_dae->n_pi+col_ind;j++) {
		for (i=0;i<jmi->jmi_dae->n_eq_F;i++) {
			row[jac_ind] = i+1;
			col[jac_ind++] = j+1;
		}
	}

	col_ind += jmi->jmi_dae->n_pi;
	// Jacobian for dependent parameters
	for (j=col_ind;j<jmi->jmi_dae->n_pd+col_ind;j++) {
		for (i=0;i<jmi->jmi_dae->n_eq_F;i++) {
			row[jac_ind] = i+1;
			col[jac_ind++] = j+1;
		}
	}

	col_ind += jmi->jmi_dae->n_pd;
	// Jacobian for derivatives
	for (j=col_ind;j<jmi->jmi_dae->n_dx+col_ind;j++) {
		for (i=0;i<jmi->jmi_dae->n_eq_F;i++) {
			row[jac_ind] = i+1;
			col[jac_ind++] = j+1;
		}
	}

	col_ind += jmi->jmi_dae->n_dx;
	// Jacobian for states
	for (j=col_ind;j<jmi->jmi_dae->n_x+col_ind;j++) {
		for (i=0;i<jmi->jmi_dae->n_eq_F;i++) {
			row[jac_ind] = i+1;
			col[jac_ind++] = j+1;
		}
	}

	col_ind += jmi->jmi_dae->n_x;
	// Jacobian for inputs
	for (j=col_ind;j<jmi->jmi_dae->n_u+col_ind;j++) {
		for (i=0;i<jmi->jmi_dae->n_eq_F;i++) {
			row[jac_ind] = i+1;
			col[jac_ind++] = j+1;
		}
	}

	col_ind += jmi->jmi_dae->n_u;
	// Jacobian for algebraics
	for (j=col_ind;j<jmi->jmi_dae->n_w+col_ind;j++) {
		for (i=0;i<jmi->jmi_dae->n_eq_F;i++) {
			row[jac_ind] = i+1;
			col[jac_ind++] = j+1;
		}
	}

	col_ind += jmi->jmi_dae->n_w;
	// Jacobian for time
	for (j=col_ind;j<1+col_ind;j++) {
		for (i=0;i<jmi->jmi_dae->n_eq_F;i++) {
			row[jac_ind] = i+1;
			col[jac_ind++] = j+1;
		}
	}
    */
	return 0;
}

// This is the init function
int jmi_new(jmi_t** jmi) {


	jmi_init(jmi, N_ci, N_cd, N_pi, N_pd, N_dx,
			      N_x, N_u, N_w);

	int dF_n_nz;
	vdp_dae_jac_sd_F_n_nz(&dF_n_nz);
	int* dF_irow = (int*)calloc(dF_n_nz,sizeof(int));
	int* dF_icol = (int*)calloc(dF_n_nz,sizeof(int));
	vdp_dae_jac_sd_F_nz_indices(dF_irow,dF_icol);

	jmi_dae_init(*jmi, *vdp_dae_F, N_eq_F, *vdp_dae_jac_sd_F,
			          dF_n_nz, dF_irow, dF_icol);

	free(dF_irow);
	free(dF_icol);

	return 0;
}
/*
#if defined __cplusplus
    }
#endif
*/
