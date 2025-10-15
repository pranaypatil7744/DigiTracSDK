package com.example.digitracksdk.data.repository.attendance_repo_imp

import com.example.digitracksdk.domain.model.attendance_model.AttendanceCycleRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceCycleResponseModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceStatusResponseModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceZoneResponseModel
import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.GnetIdRequestModel
import com.example.digitracksdk.domain.repository.attendance_repository.AttendanceRepository
import com.example.digitracksdk.domain.usecase.attendance_usecase.InsertAttendancePicRequestModel
import com.example.digitracksdk.domain.usecase.attendance_usecase.InsertAttendancePicResponseModel
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
import com.innov.digitrac.webservice_api.request_resonse.AttendanceMarkRequestModel
import com.innov.digitrac.webservice_api.request_resonse.AttendanceMarkResponseModel

class AttendanceRepositoryImp(private val apiService: ApiService): AttendanceRepository {
    override suspend fun callAttendanceGeoFancingModel(request: CommonRequestModel): AttendanceGeoFancingResponseModel {
        return apiService.callAttendanceGeoFancingApi(request)
    }

    override suspend fun callDashboardAttendanceStatusApi(request: CommonRequestModel): DashboardAttendanceStatusResponseModel {
        return apiService.callDashboardAttendanceStatusApi(request)
    }

    override suspend fun callAttendanceTimeSheetApi(request: AttendanceTimeSheetRequestModel): AttendanceTimeSheetResponseModel {
        return apiService.callAttendanceTimeSheetApi(request)
    }

    override suspend fun callAttendanceStatusApi(request: GnetIdRequestModel): AttendanceStatusResponseModel {
        return apiService.callAttendanceStatusApi(request)
    }

    override suspend fun callAttendanceFlagApi(request: AttendanceFlagRequestModel): AttendanceFlagResponseModel {
        return apiService.callAttendanceFlagApi(request)
    }

    override suspend fun callAttendanceMarkApi(request: AttendanceMarkRequestModel): AttendanceMarkResponseModel {
        return apiService.callAttendanceMarkApi(request)
    }

    override suspend fun callAttendanceValidationApi(request: AttendanceValidationRequestModel): AttendanceValidationResponseModel {
        return apiService.callAttendanceValidationApi(request)
    }

    override suspend fun callCurrentDayAttendanceStatusApi(request: CurrentDayAttendanceStatusRequestModel): CurrentDayAttendanceStatusResponseModel {
        return apiService.callCurrentDayAttendanceStatusApi(request)
    }

    override suspend fun callAttendanceZoneApi(request: CommonRequestModel): AttendanceZoneResponseModel {
        return apiService.callAttendanceZoneApi(request)
    }

    override suspend fun callCreateAttendanceTokenApi(request: CreateAttendanceTokenRequestModel): CreateAttendanceTokenResponseModel {
        return apiService.callCreateAttendanceTokenApi(request)
    }

    override suspend fun callCheckValidAttendanceTokenApi(request: CheckValidAttendanceTokenRequestModel): CheckValidAttendanceTokenResponseModel {
        return apiService.callCheckValidAttendanceTokenApi(request)
    }

    override suspend fun callInsertAttendancePicApi(request: InsertAttendancePicRequestModel): InsertAttendancePicResponseModel {
        return apiService.callInsertAttendancePicApi(request)
    }

    override suspend fun callGetLogYourVisitApi(request: LogYourVisitRequestModel): LogYourVisitResponseModel {
        return apiService.callGetLogYourVisitApi(request)
    }

    override suspend fun callViewAttendanceApi(request: ViewAttendanceRequestModel): ViewAttendanceResponseModel {
        return apiService.callViewAttendanceApi(request)
    }

    override suspend fun callUpdateAttendanceStatusApi(request: UpdateAttendanceStatusRequestModel): UpdateAttendanceStatusResponseModel {
        return apiService.callUpdateAttendanceStatusApi(request)
    }

    override suspend fun callCreateLogYourVisitTokenApi(request: CreateLogYourVisitTokenRequestModel): CreateLogYourVisitTokenResponseModel {
        return apiService.callCreateLogYourVisitTokenApi(request)
    }

    override suspend fun callValidateLogYourVisitTokenApi(request: CheckValidLogYourVisitRequestModel): CheckValidLogYourVisitResponseModel {
        return apiService.callValidateLogYourVisitTokenApi(request)
    }

    override suspend fun getAttendanceCycleApi(request: AttendanceCycleRequestModel): AttendanceCycleResponseModel {
       return apiService.callAttendanceCycleApi(request)
    }

    override suspend fun getAttendanceHexCodeApi(request: LeaveHexCodeRequestModel): LeaveHexCodeResponseModel {
       return apiService.callLeaveTypeHexCodeApi(request)
    }
}