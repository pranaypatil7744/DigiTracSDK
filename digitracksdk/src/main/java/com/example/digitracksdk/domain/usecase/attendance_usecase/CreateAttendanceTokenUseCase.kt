package com.example.digitracksdk.domain.usecase.attendance_usecase

import com.example.digitracksdk.domain.model.attendance_model.CreateAttendanceTokenRequestModel
import com.example.digitracksdk.domain.model.attendance_model.CreateAttendanceTokenResponseModel
import com.example.digitracksdk.domain.repository.attendance_repository.AttendanceRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class CreateAttendanceTokenUseCase constructor(private val attendanceRepository: AttendanceRepository) :
    UseCase<CreateAttendanceTokenResponseModel, Any?>() {
    override suspend fun run(params: Any?): CreateAttendanceTokenResponseModel {
        return attendanceRepository.callCreateAttendanceTokenApi(params as CreateAttendanceTokenRequestModel)
    }
}