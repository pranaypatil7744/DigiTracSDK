package com.example.digitracksdk.domain.usecase.attendance_usecase

import com.example.digitracksdk.domain.model.attendance_model.CreateLogYourVisitTokenRequestModel
import com.example.digitracksdk.domain.model.attendance_model.CreateLogYourVisitTokenResponseModel
import com.example.digitracksdk.domain.repository.attendance_repository.AttendanceRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class CreateLogVisitTokenUseCase constructor(private val attendanceRepository: AttendanceRepository) :
    UseCase<CreateLogYourVisitTokenResponseModel, Any?>() {
    override suspend fun run(params: Any?): CreateLogYourVisitTokenResponseModel {
        return attendanceRepository.callCreateLogYourVisitTokenApi(params as CreateLogYourVisitTokenRequestModel)
    }
}