package com.example.digitracksdk.domain.usecase.attendance_usecase

import com.example.digitracksdk.domain.model.attendance_model.LogYourVisitRequestModel
import com.example.digitracksdk.domain.model.attendance_model.LogYourVisitResponseModel
import com.example.digitracksdk.domain.repository.attendance_repository.AttendanceRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class LogYourVisitUseCase constructor(private var attendanceRepository: AttendanceRepository) :
    UseCase<LogYourVisitResponseModel, Any?>() {
    override suspend fun run(params: Any?): LogYourVisitResponseModel {
        return attendanceRepository.callGetLogYourVisitApi(params as LogYourVisitRequestModel)
    }
}