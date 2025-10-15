package com.example.digitracksdk.domain.usecase.attendance_usecase

import com.example.digitracksdk.domain.repository.attendance_repository.AttendanceRepository
import com.example.digitracksdk.domain.usecase.base.UseCase
import com.example.digitracksdk.domain.model.attendance_model.AttendanceFlagRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceFlagResponseModel

class AttendanceFlagUseCase constructor(private val attendanceRepository: AttendanceRepository) :
    UseCase<AttendanceFlagResponseModel, Any?>() {
    override suspend fun run(params: Any?): AttendanceFlagResponseModel {
        return attendanceRepository.callAttendanceFlagApi(params as AttendanceFlagRequestModel)
    }
}