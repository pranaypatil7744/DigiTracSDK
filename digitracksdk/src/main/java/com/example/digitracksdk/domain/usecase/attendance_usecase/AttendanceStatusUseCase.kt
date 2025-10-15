package com.example.digitracksdk.domain.usecase.attendance_usecase

import com.example.digitracksdk.domain.model.GnetIdRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceStatusResponseModel
import com.example.digitracksdk.domain.repository.attendance_repository.AttendanceRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class AttendanceStatusUseCase constructor(private val attendanceRepository: AttendanceRepository) :
    UseCase<AttendanceStatusResponseModel, Any?>() {
    override suspend fun run(params: Any?): AttendanceStatusResponseModel {
        return attendanceRepository.callAttendanceStatusApi(params as GnetIdRequestModel)
    }
}