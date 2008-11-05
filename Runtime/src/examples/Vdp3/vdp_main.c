
#include <stdio.h>
#include <stdlib.h>
#include "../../Jmi3/jmi.h"

int main(int argv, char* argc[])
{

	Jmi* jmi;
	jmi_new(jmi);

	int i;

	printf("Number of interactive constants:               %d\n",jmi->jmi_dae->n_ci);
	printf("Number of dependent constants:                 %d\n",jmi->jmi_dae->n_cd);
	printf("Number of interactive parameters:              %d\n",jmi->jmi_dae->n_pi);
	printf("Number of dependent parameters:                %d\n",jmi->jmi_dae->n_pd);
	printf("Number of derivatives:                         %d\n",jmi->jmi_dae->n_dx);
	printf("Number of states:                              %d\n",jmi->jmi_dae->n_x);
	printf("Number of inputs:                              %d\n",jmi->jmi_dae->n_u);
	printf("Number of algebraics:                          %d\n",jmi->jmi_dae->n_w);
	printf("Number of DAE equations:                       %d\n",jmi->jmi_dae->n_eq_F);
/*	printf("Number of DAE initial equations (F0):          %d\n",n_eq_F0);
	printf("Number of DAE initial equations (F1):          %d\n",n_eq_F1);
	printf("Number of elements in Jacobian wrt dx, x, u:   %d\n",n_jac_F);
*/
	/*
	Double_t* ci = (Double_t*)calloc(n_ci,sizeof(Double_t));
	Double_t* cd = (Double_t*)calloc(n_cd,sizeof(Double_t));
	Double_t* pi = (Double_t*)calloc(n_pi,sizeof(Double_t));
	Double_t* pd = (Double_t*)calloc(n_pd,sizeof(Double_t));
	Double_t* dx = (Double_t*)calloc(n_dx,sizeof(Double_t));
	Double_t* x = (Double_t*)calloc(n_x,sizeof(Double_t));
	Double_t* u = (Double_t*)calloc(n_u,sizeof(Double_t));
	Double_t* w = (Double_t*)calloc(n_w,sizeof(Double_t));
	Double_t* res_F = (Double_t*)calloc(n_eq_F,sizeof(Double_t));
	Double_t* res_F0 = (Double_t*)calloc(n_eq_F0,sizeof(Double_t));
	Double_t* res_F1 = (Double_t*)calloc(n_eq_F1,sizeof(Double_t));
	Double_t* jac_sd_F = (Double_t*)calloc(n_jac_F,sizeof(Double_t));
	Double_t* jac_DER_F = (Double_t*)calloc(n_jac_F,sizeof(Double_t));

	Double_t t = 0;

	// Here initial values for all parameters should be reDER from
	// xml-files

	// Try to initialize x = (0,1,0)
	x[0] = 0;
	x[1] = 1;
	x[2] = 0;

	jmi_dae_F(ci,cd,pi,pd,dx,x,u,w,t,res_F);
	jmi_init_F0(ci,cd,pi,pd,dx,x,u,w,t,res_F0);
	jmi_init_F1(ci,cd,pi,pd,dx,x,u,w,t,res_F1);
    jmi_dae_sd_dF(ci,cd,pi,pd,dx,x,u,w,t,mask,jac_sd_F);
    jmi_dae_ad_dF(ci,cd,pi,pd,dx,x,u,w,t,mask,jac_DER_F);

	printf("\n *** State initialized to (0,1,0) ***\n\n");
	printf("DAE residual:\n");
	for (i=0;i<n_eq_F;i++){
		printf("res[%d] = %f\n",i,res_F[i]);
	}

	printf("\ninitial DAE residual (F0):\n");
	for (i=0;i<n_eq_F0;i++){
		printf("res[%d] = %f\n",i,res_F0[i]);
	}

	printf("\ninitial DAE residual (F1):\n");
	for (i=0;i<n_eq_F1;i++){
		printf("res[%d] = %f\n",i,res_F1[i]);
	}

	printf("\n Jacobian of F wrt dx, x, u:\n");
	for (i=0;i<n_jac_F;i++){
		printf("jac_sd_F[%d] = %f, jac_DER_F[%d] = %f\n",i,jac_sd_F[i], i,jac_DER_F[i]);
	}

	free(ci);
	free(cd);
	free(pi);
	free(pd);
	free(dx);
	free(x);
	free(u);
	free(w);
	free(res_F);
	free(res_F0);
	free(res_F1);
*/

	jmi_delete(jmi);

	return 0;
}
