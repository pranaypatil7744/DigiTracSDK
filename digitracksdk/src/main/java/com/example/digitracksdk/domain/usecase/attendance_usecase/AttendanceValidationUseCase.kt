package com.example.digitracksdk.domain.usecase.attendance_usecase

import com.example.digitracksdk.domain.model.attendance_model.AttendanceValidationRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceValidationResponseModel
import com.example.digitracksdk.domain.repository.attendance_repository.AttendanceRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class AttendanceValidationUseCase constructor(private val attendanceRepository: AttendanceRepository) :
    UseCase<AttendanceValidationResponseModel, Any?>() {
    override suspend fun run(params: Any?): AttendanceValidationResponseModel {
        return attendanceRepository.callAttendanceValidationApi(params as AttendanceValidationRequestModel)
    }
}