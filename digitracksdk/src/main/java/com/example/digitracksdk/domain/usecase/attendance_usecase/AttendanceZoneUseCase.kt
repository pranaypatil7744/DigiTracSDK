package com.example.digitracksdk.domain.usecase.attendance_usecase

import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceZoneResponseModel
import com.example.digitracksdk.domain.repository.attendance_repository.AttendanceRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class AttendanceZoneUseCase constructor(private val attendanceRepository: AttendanceRepository) :
    UseCase<AttendanceZoneResponseModel, Any?>() {
    override suspend fun run(params: Any?): AttendanceZoneResponseModel {
        return attendanceRepository.callAttendanceZoneApi(params as CommonRequestModel)
    }
}