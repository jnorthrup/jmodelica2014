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


#ifndef FMI1_CAPI_IMPL_H_
#define FMI1_CAPI_IMPL_H_

#ifdef __cplusplus 
extern "C" {
#endif

#include <FMI1/fmi1_types.h>
#include <FMI1/fmi1_functions.h>
#include <FMI1/fmi1_enums.h>
#include <jm_portability.h>
#include <jm_callbacks.h>

#define LOGGER_MODULE_NAME "CAPIIMPORT"

/** 
 * \brief C-API struct used as a placeholder for the FMI funktions and shared library handler. 
 */
typedef struct {
	const char* dllPath;
	const char* modelIdentifier;
	fmi1_callback_functions_t callBackFunctions;

	jm_callbacks* callbacks;

	DLL_HANDLE dllHandle;

	fmi1_fmu_kind_enu_t standard;

	fmi1_component_t					c;

	/* FMI common */
	fmi1_get_version_ft					fmiGetVersion;
	fmi1_set_debug_logging_ft			fmiSetDebugLogging;
    fmi1_set_real_ft					fmiSetReal;
    fmi1_set_integer_ft					fmiSetInteger;
    fmi1_set_boolean_ft					fmiSetBoolean;
    fmi1_set_string_ft					fmiSetString;
	fmi1_get_real_ft					fmiGetReal;
    fmi1_get_integer_ft					fmiGetInteger;
    fmi1_get_boolean_ft					fmiGetBoolean;
    fmi1_get_string_ft					fmiGetString;

	/* FMI ME */
    fmi1_get_model_typesPlatform_ft		fmiGetModelTypesPlatform;    
    fmi1_instantiate_model_ft			fmiInstantiateModel;
    fmi1_free_model_instance_ft			fmiFreeModelInstance;    
    fmi1_set_time_ft					fmiSetTime;
    fmi1_set_continuous_states_ft		fmiSetContinuousStates;
    fmi1_completed_integrator_step_ft	fmiCompletedIntegratorStep;
    fmi1_initialize_ft			 		fmiInitialize;
    fmi1_get_derivatives_ft				fmiGetDerivatives;
    fmi1_get_event_indicators_ft		fmiGetEventIndicators;
    fmi1_event_update_ft				fmiEventUpdate;
    fmi1_get_continuous_states_ft		fmiGetContinuousStates;
    fmi1_get_nominal_continuousStates_ft fmiGetNominalContinuousStates;
    fmi1_get_state_valueReferences_ft	fmiGetStateValueReferences;
    fmi1_terminate_ft					fmiTerminate;

	/* FMI CS */
	fmi1_get_types_platform_ft			fmiGetTypesPlatform;   
    fmi1_instantiate_slave_ft			fmiInstantiateSlave;
    fmi1_initialize_slave_ft			fmiInitializeSlave;
    fmi1_terminate_slave_ft				fmiTerminateSlave;
    fmi1_reset_slave_ft					fmiResetSlave;
    fmi1_free_slave_instance_ft			fmiFreeSlaveInstance;
    fmi1_set_real_inputDerivatives_ft	fmiSetRealInputDerivatives;
    fmi1_get_real_outputDerivatives_ft	fmiGetRealOutputDerivatives;
    fmi1_do_step_ft						fmiDoStep;
    fmi1_cancel_step_ft					fmiCancelStep;
    fmi1_get_status_ft					fmiGetStatus;
    fmi1_get_real_status_ft				fmiGetRealStatus;
    fmi1_get_integer_status_ft			fmiGetIntegerStatus;
    fmi1_get_boolean_status_ft			fmiGetBooleanStatus;
    fmi1_get_string_status_ft			fmiGetStringStatus;

} fmi1_capi_t;

#ifdef __cplusplus 
}
#endif

#endif /* End of header file FMI1_CAPI_IMPL_H_ */