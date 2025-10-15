package com.example.digitracksdk.presentation.attendance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.attendance_model.AttendanceStatusResponseModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceZoneResponseModel
import com.example.digitracksdk.domain.usecase.attendance_usecase.UpdateAttendanceStatusUseCase
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.attendance_model.AttendanceCycleRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceCycleResponseModel
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.GnetIdRequestModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.model.attendance_model.AttendanceFlagRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceFlagResponseModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceGeoFancingResponseModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceTimeSheetRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceTimeSheetResponseModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceValidationRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceValidationResponseModel
import com.example.digitracksdk.domain.model.attendance_model.CheckValidAttendanceTokenRequestModel
import com.example.digitracksdk.domain.model.attendance_model.CheckValidAttendanceTokenResponseModel
import com.example.digitracksdk.domain.model.attendance_model.CheckValidLogYourVisitRequestModel
import com.example.digitracksdk.domain.model.attendance_model.CheckValidLogYourVisitResponseModel
import com.example.digitracksdk.domain.model.attendance_model.CreateAttendanceTokenRequestModel
import com.example.digitracksdk.domain.model.attendance_model.CreateAttendanceTokenResponseModel
import com.example.digitracksdk.domain.model.attendance_model.CreateLogYourVisitTokenRequestModel
import com.example.digitracksdk.domain.model.attendance_model.CreateLogYourVisitTokenResponseModel
import com.example.digitracksdk.domain.model.attendance_model.CurrentDayAttendanceStatusRequestModel
import com.example.digitracksdk.domain.model.attendance_model.CurrentDayAttendanceStatusResponseModel
import com.example.digitracksdk.domain.model.attendance_model.DashboardAttendanceStatusResponseModel
import com.example.digitracksdk.domain.model.attendance_model.LeaveHexCodeRequestModel
import com.example.digitracksdk.domain.model.attendance_model.LeaveHexCodeResponseModel
import com.example.digitracksdk.domain.model.attendance_model.LogYourVisitRequestModel
import com.example.digitracksdk.domain.model.attendance_model.LogYourVisitResponseModel
import com.example.digitracksdk.domain.model.attendance_model.UpdateAttendanceStatusRequestModel
import com.example.digitracksdk.domain.model.attendance_model.UpdateAttendanceStatusResponseModel
import com.example.digitracksdk.domain.model.attendance_model.ViewAttendanceRequestModel
import com.example.digitracksdk.domain.model.attendance_model.ViewAttendanceResponseModel
import com.example.digitracksdk.domain.usecase.attendance_usecase.AttendanceCycleUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.AttendanceFlagUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.AttendanceGeoFancingUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.AttendanceMarkUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.AttendanceStatusUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.AttendanceValidationUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.AttendanceZoneUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.CheckValidAttendanceTokenUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.CreateAttendanceTokenUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.CreateLogVisitTokenUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.CurrentDayAtteStatusUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.DashboardAttendanceStatusUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.GetAttendanceTimeSheetUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.InsertAttendancePicRequestModel
import com.example.digitracksdk.domain.usecase.attendance_usecase.InsertAttendancePicResponseModel
import com.example.digitracksdk.domain.usecase.attendance_usecase.InsertAttendancePicUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.LeaveHexCodeUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.LogYourVisitUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.ValidateLogVisitTokenUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.ViewAttendanceUseCase
import com.innov.digitrac.webservice_api.request_resonse.AttendanceMarkRequestModel
import com.innov.digitrac.webservice_api.request_resonse.AttendanceMarkResponseModel
import kotlinx.coroutines.cancel

class AttendanceViewModel constructor(
    private val attendanceGeoFancingUseCase: AttendanceGeoFancingUseCase,
    private val dashboardAttendanceStatusUseCase: DashboardAttendanceStatusUseCase,
    private val getAttendanceTimeSheetUseCase: GetAttendanceTimeSheetUseCase,
    private val attendanceStatusUseCase: AttendanceStatusUseCase,
    private val attendanceFlagUseCase: AttendanceFlagUseCase,
    private val attendanceMarkUseCase: AttendanceMarkUseCase,
    private val attendanceValidationUseCase: AttendanceValidationUseCase,
    private val currentDayAtteStatusUseCase: CurrentDayAtteStatusUseCase,
    private val attendanceZoneUseCase: AttendanceZoneUseCase,
    private val createAttendanceTokenUseCase: CreateAttendanceTokenUseCase,
    private val checkValidAttendanceTokenUseCase: CheckValidAttendanceTokenUseCase,
    private val insertAttendancePicUseCase: InsertAttendancePicUseCase,
    private val logYourVisitUseCase: LogYourVisitUseCase,
    private val viewAttendanceUseCase: ViewAttendanceUseCase,
    private val updateAttendanceStatusUseCase: UpdateAttendanceStatusUseCase,
    private val createLogVisitTokenUseCase: CreateLogVisitTokenUseCase,
    private val validateLogVisitTokenUseCase: ValidateLogVisitTokenUseCase,
    private val attendanceCycleUseCase : AttendanceCycleUseCase,
    private val leaveHexCodeUseCase: LeaveHexCodeUseCase
) : ViewModel() {

    val attendanceGeoFancingResponseData = MutableLiveData<AttendanceGeoFancingResponseModel>()
    val createLogVisitTokenResponseData = MutableLiveData<CreateLogYourVisitTokenResponseModel>()
    val validateLogVisitTokenResponseData = MutableLiveData<CheckValidLogYourVisitResponseModel>()
    val updateAttendanceStatusResponseData = MutableLiveData<UpdateAttendanceStatusResponseModel>()
    val logYourVisitResponseData = MutableLiveData<LogYourVisitResponseModel>()
    val createAttendanceTokenResponseData = MutableLiveData<CreateAttendanceTokenResponseModel>()
    val insertAttendancePicResponseData = MutableLiveData<InsertAttendancePicResponseModel>()
    val checkValidAttendanceTokenResponseData =
        MutableLiveData<CheckValidAttendanceTokenResponseModel>()
    val dashboardAttendanceStatusResponseData =
        MutableLiveData<DashboardAttendanceStatusResponseModel>()
    val attendanceTimeSheetResponseData = MutableLiveData<AttendanceTimeSheetResponseModel>()
    val attendanceStatusResponseData = MutableLiveData<AttendanceStatusResponseModel>()
    val attendanceFlagResponseData = MutableLiveData<AttendanceFlagResponseModel>()
    val attendanceMarkResponseData = MutableLiveData<AttendanceMarkResponseModel>()
    val attendanceValidationResponseData = MutableLiveData<AttendanceValidationResponseModel>()
    val attendanceZoneResponseData = MutableLiveData<AttendanceZoneResponseModel>()
    val viewAttendanceResponseData = MutableLiveData<ViewAttendanceResponseModel>()
    val currentDayAttendanceStatusResponseData =
        MutableLiveData<CurrentDayAttendanceStatusResponseModel>()

    val attendanceCycleResponseData = MutableLiveData<AttendanceCycleResponseModel>()
    val leaveHexCodeResponseData = MutableLiveData<LeaveHexCodeResponseModel>()
    val messageData = MutableLiveData<String>()
    val progressBar = MutableLiveData<Boolean>()


    fun callCreateLogVisitTokenApi(request: CreateLogYourVisitTokenRequestModel) {
        progressBar.value = true
        createLogVisitTokenUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<CreateLogYourVisitTokenResponseModel> {
                override fun onSuccess(result: CreateLogYourVisitTokenResponseModel) {
                    progressBar.value = false
                    createLogVisitTokenResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    progressBar.value = false
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callValidateLogVisitTokenApi(request: CheckValidLogYourVisitRequestModel) {
        progressBar.value = true
        validateLogVisitTokenUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<CheckValidLogYourVisitResponseModel> {
                override fun onSuccess(result: CheckValidLogYourVisitResponseModel) {
                    progressBar.value = false
                    validateLogVisitTokenResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    progressBar.value = false
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callViewAttendanceApi(request: ViewAttendanceRequestModel) {
        progressBar.value = true
        viewAttendanceUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ViewAttendanceResponseModel> {
                override fun onSuccess(result: ViewAttendanceResponseModel) {
                    progressBar.value = false
                    viewAttendanceResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    progressBar.value = false
                    messageData.value = apiError?.message.toString()
                }
            })
    }




    fun callUpdateAttendanceStatusApi(request: UpdateAttendanceStatusRequestModel) {
        progressBar.value = true
        updateAttendanceStatusUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<UpdateAttendanceStatusResponseModel> {
                override fun onSuccess(result: UpdateAttendanceStatusResponseModel) {
                    progressBar.value = false
                    updateAttendanceStatusResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    progressBar.value = false
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callLogYourVisitApi(request: LogYourVisitRequestModel) {
        progressBar.value = true
        logYourVisitUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<LogYourVisitResponseModel> {
                override fun onSuccess(result: LogYourVisitResponseModel) {
                    progressBar.value = false
                    logYourVisitResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    progressBar.value = false
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callInsertAttendancePicApi(request: InsertAttendancePicRequestModel) {
        insertAttendancePicUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<InsertAttendancePicResponseModel> {
                override fun onSuccess(result: InsertAttendancePicResponseModel) {
                    insertAttendancePicResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callCheckValidAttendanceTokenApi(request: CheckValidAttendanceTokenRequestModel) {
        checkValidAttendanceTokenUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<CheckValidAttendanceTokenResponseModel> {
                override fun onSuccess(result: CheckValidAttendanceTokenResponseModel) {
                    checkValidAttendanceTokenResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callCreateAttendanceTokenApi(request: CreateAttendanceTokenRequestModel) {
        createAttendanceTokenUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<CreateAttendanceTokenResponseModel> {
                override fun onSuccess(result: CreateAttendanceTokenResponseModel) {
                    createAttendanceTokenResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callAttendanceZoneApi(request: CommonRequestModel) {
        attendanceZoneUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<AttendanceZoneResponseModel> {
                override fun onSuccess(result: AttendanceZoneResponseModel) {
                    attendanceZoneResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callCurrentDayAttendanceStatusApi(request: CurrentDayAttendanceStatusRequestModel) {
        currentDayAtteStatusUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<CurrentDayAttendanceStatusResponseModel> {
                override fun onSuccess(result: CurrentDayAttendanceStatusResponseModel) {
                    currentDayAttendanceStatusResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callAttendanceValidationApi(request: AttendanceValidationRequestModel) {
        attendanceValidationUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<AttendanceValidationResponseModel> {

                override fun onSuccess(result: AttendanceValidationResponseModel) {
                    result.date = request.FromDate
                    attendanceValidationResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callAttendanceStatusApi(request: GnetIdRequestModel) {
        attendanceStatusUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<AttendanceStatusResponseModel> {
                override fun onSuccess(result: AttendanceStatusResponseModel) {
                    attendanceStatusResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callAttendanceFlagApi(request: AttendanceFlagRequestModel){
        attendanceFlagUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<AttendanceFlagResponseModel> {
                override fun onSuccess(result: AttendanceFlagResponseModel) {
                    attendanceFlagResponseData.value=result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            }
        )
    }

    fun callAttendanceMarkApi(request: AttendanceMarkRequestModel){
        attendanceMarkUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<AttendanceMarkResponseModel> {
                override fun onSuccess(result: AttendanceMarkResponseModel) {
                    attendanceMarkResponseData.value=result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            }
        )
    }

    fun callAttendanceTimeSheetApi(request: AttendanceTimeSheetRequestModel) {
        getAttendanceTimeSheetUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<AttendanceTimeSheetResponseModel> {
                override fun onSuccess(result: AttendanceTimeSheetResponseModel) {
                    attendanceTimeSheetResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callDashboardAttendanceStatusApi(request: CommonRequestModel) {
        dashboardAttendanceStatusUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<DashboardAttendanceStatusResponseModel> {
                override fun onSuccess(result: DashboardAttendanceStatusResponseModel) {
                    dashboardAttendanceStatusResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callAttendanceGeoFancingApi(request: CommonRequestModel) {
        attendanceGeoFancingUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<AttendanceGeoFancingResponseModel> {
                override fun onSuccess(result: AttendanceGeoFancingResponseModel) {
                    attendanceGeoFancingResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callAttendanceCycleApi(request: AttendanceCycleRequestModel) {
        progressBar.value = true
        attendanceCycleUseCase.invoke(viewModelScope, request, object :
            UseCaseResponse<AttendanceCycleResponseModel> {
            override fun onSuccess(result: AttendanceCycleResponseModel) {
                attendanceCycleResponseData.value = result
                progressBar.value = false
            }

            override fun onError(apiError: ApiError?) {
                messageData.value = apiError?.getErrorMessage()
                progressBar.value = false
            }

        }
        )
    }

    fun callLeaveHexCodeApi(request  : LeaveHexCodeRequestModel)
    {
        progressBar.value = true
        leaveHexCodeUseCase.invoke(viewModelScope , request ,object  :
            UseCaseResponse<LeaveHexCodeResponseModel>
        {
            override fun onSuccess(result: LeaveHexCodeResponseModel) {
                leaveHexCodeResponseData.value = result
                progressBar.value = false
            }

            override fun onError(apiError: ApiError?) {
                messageData.value = apiError?.getErrorMessage()
                progressBar.value = false
            }

        }
        )

    }


    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}