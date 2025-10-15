package com.example.digitracksdk.domain.usecase.attendance_usecase

import com.example.digitracksdk.domain.model.attendance_model.CheckValidAttendanceTokenRequestModel
import com.example.digitracksdk.domain.model.attendance_model.CheckValidAttendanceTokenResponseModel
import com.example.digitracksdk.domain.repository.attendance_repository.AttendanceRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class CheckValidAttendanceTokenUseCase constructor(private val attendanceRepository: AttendanceRepository) :
    UseCase<CheckValidAttendanceTokenResponseModel, Any?>() {
    override suspend fun run(params: Any?): CheckValidAttendanceTokenResponseModel {
        return attendanceRepository.callCheckValidAttendanceTokenApi(params as CheckValidAttendanceTokenRequestModel)
    }
}