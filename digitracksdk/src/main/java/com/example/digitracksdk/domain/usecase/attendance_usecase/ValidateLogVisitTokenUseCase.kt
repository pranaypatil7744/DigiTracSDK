package com.example.digitracksdk.domain.usecase.attendance_usecase

import com.example.digitracksdk.domain.model.attendance_model.CheckValidLogYourVisitRequestModel
import com.example.digitracksdk.domain.model.attendance_model.CheckValidLogYourVisitResponseModel
import com.example.digitracksdk.domain.repository.attendance_repository.AttendanceRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ValidateLogVisitTokenUseCase constructor(private val attendanceRepository: AttendanceRepository) :
    UseCase<CheckValidLogYourVisitResponseModel, Any?>() {
    override suspend fun run(params: Any?): CheckValidLogYourVisitResponseModel {
        return attendanceRepository.callValidateLogYourVisitTokenApi(params as CheckValidLogYourVisitRequestModel)
    }

}