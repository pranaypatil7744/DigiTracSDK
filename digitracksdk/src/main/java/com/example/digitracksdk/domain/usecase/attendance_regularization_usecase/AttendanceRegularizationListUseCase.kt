package com.example.digitracksdk.domain.usecase.attendance_regularization_usecase

import com.example.digitracksdk.domain.model.attendance_regularization_model.AttendanceRegularizationListRequestModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.AttendanceRegularizationListResponseModel
import com.example.digitracksdk.domain.repository.attendance_regularization_repository.AttendanceRegularizationRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class AttendanceRegularizationListUseCase constructor(private val attendanceRegularizationRepository: AttendanceRegularizationRepository) :
    UseCase<AttendanceRegularizationListResponseModel, Any?>() {
    override suspend fun run(params: Any?): AttendanceRegularizationListResponseModel {
        return attendanceRegularizationRepository.callAttendanceRegularizationListApi(params as AttendanceRegularizationListRequestModel)
    }
}