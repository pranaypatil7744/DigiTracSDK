package com.example.digitracksdk.domain.repository.attendance_repository

import com.example.digitracksdk.domain.model.attendance_model.AttendanceCycleRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceCycleResponseModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceStatusResponseModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceZoneResponseModel
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.GnetIdRequestModel
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

interface AttendanceRepository {

    suspend fun callAttendanceGeoFancingModel(request: CommonRequestModel): AttendanceGeoFancingResponseModel

    suspend fun callDashboardAttendanceStatusApi(request: CommonRequestModel): DashboardAttendanceStatusResponseModel

    suspend fun callAttendanceTimeSheetApi(request: AttendanceTimeSheetRequestModel): AttendanceTimeSheetResponseModel

    suspend fun callAttendanceStatusApi(request: GnetIdRequestModel): AttendanceStatusResponseModel
    suspend fun callAttendanceFlagApi(request: AttendanceFlagRequestModel): AttendanceFlagResponseModel
    suspend fun callAttendanceMarkApi(request: AttendanceMarkRequestModel): AttendanceMarkResponseModel

    suspend fun callAttendanceValidationApi(request: AttendanceValidationRequestModel): AttendanceValidationResponseModel

    suspend fun callCurrentDayAttendanceStatusApi(request: CurrentDayAttendanceStatusRequestModel): CurrentDayAttendanceStatusResponseModel

    suspend fun callAttendanceZoneApi(request: CommonRequestModel): AttendanceZoneResponseModel

    suspend fun callCreateAttendanceTokenApi(request: CreateAttendanceTokenRequestModel): CreateAttendanceTokenResponseModel

    suspend fun callCheckValidAttendanceTokenApi(request: CheckValidAttendanceTokenRequestModel): CheckValidAttendanceTokenResponseModel

    suspend fun callInsertAttendancePicApi(request: InsertAttendancePicRequestModel): InsertAttendancePicResponseModel

    suspend fun callGetLogYourVisitApi(request: LogYourVisitRequestModel): LogYourVisitResponseModel

    suspend fun callViewAttendanceApi(request: ViewAttendanceRequestModel): ViewAttendanceResponseModel

    suspend fun callUpdateAttendanceStatusApi(request: UpdateAttendanceStatusRequestModel): UpdateAttendanceStatusResponseModel

    suspend fun callCreateLogYourVisitTokenApi(request: CreateLogYourVisitTokenRequestModel): CreateLogYourVisitTokenResponseModel

    suspend fun callValidateLogYourVisitTokenApi(request: CheckValidLogYourVisitRequestModel): CheckValidLogYourVisitResponseModel

    suspend fun getAttendanceCycleApi(request : AttendanceCycleRequestModel) : AttendanceCycleResponseModel
    suspend fun getAttendanceHexCodeApi(request : LeaveHexCodeRequestModel) : LeaveHexCodeResponseModel

}