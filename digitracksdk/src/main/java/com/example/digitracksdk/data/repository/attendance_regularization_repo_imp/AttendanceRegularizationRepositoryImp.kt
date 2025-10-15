package com.example.digitracksdk.data.repository.attendance_regularization_repo_imp

import com.example.digitracksdk.domain.model.attendance_regularization_model.AttendanceRegularizationListRequestModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.AttendanceRegularizationListResponseModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.InsertAttendanceRegularizationRequestModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.InsertAttendanceRegularizationResponseModel
import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.AttendanceRegularizationTypeResponseModel
import com.example.digitracksdk.domain.repository.attendance_regularization_repository.AttendanceRegularizationRepository

class AttendanceRegularizationRepositoryImp(private val apiServiceNormal: ApiService, private val apiServiceCandidateApp: ApiService) :
    AttendanceRegularizationRepository {
    override suspend fun callAttendanceRegularizationListApi(request: AttendanceRegularizationListRequestModel): AttendanceRegularizationListResponseModel {
        return apiServiceNormal.callAttendanceRegularizationListApi(request)
    }

    override suspend fun callAttendanceRegularizationTypeApi(request: CommonRequestModel): AttendanceRegularizationTypeResponseModel {
        return apiServiceNormal.callAttendanceRegularizationTypeApi(request)
    }

    override suspend fun callAttendanceRegularizationInsertApi(request: InsertAttendanceRegularizationRequestModel): InsertAttendanceRegularizationResponseModel {
        return apiServiceCandidateApp.callAttendanceRegularizationInsertApi(
            request.Empcode,
            request.RequestTypeId,
            request.RegularizationDate,
            request.InTime,
            request.OutTime,
            request.Location,
            request.Remarks,
            request.ToDate
        )
    }

    override suspend fun callInsertAttendanceRegularizationApi(request: InsertAttendanceRegularizationRequestModel): InsertAttendanceRegularizationResponseModel {
        return apiServiceNormal.callInsertAttendanceRegularizationApi(request)
    }
}