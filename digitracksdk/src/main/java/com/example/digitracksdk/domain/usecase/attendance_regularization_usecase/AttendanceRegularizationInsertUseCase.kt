package com.example.digitracksdk.domain.usecase.attendance_regularization_usecase

import com.example.digitracksdk.domain.model.attendance_regularization_model.InsertAttendanceRegularizationRequestModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.InsertAttendanceRegularizationResponseModel
import com.example.digitracksdk.domain.repository.attendance_regularization_repository.AttendanceRegularizationRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class AttendanceRegularizationInsertUseCase constructor(private val attendanceRegularizationRepository: AttendanceRegularizationRepository) :
    UseCase<InsertAttendanceRegularizationResponseModel, Any?>() {
    override suspend fun run(params: Any?): InsertAttendanceRegularizationResponseModel {
        return attendanceRegularizationRepository.callAttendanceRegularizationInsertApi(params as InsertAttendanceRegularizationRequestModel)
    }
}