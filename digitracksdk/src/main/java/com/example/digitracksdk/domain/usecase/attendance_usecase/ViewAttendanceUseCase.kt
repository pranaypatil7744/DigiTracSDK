package com.example.digitracksdk.domain.usecase.attendance_usecase

import com.example.digitracksdk.domain.model.attendance_model.ViewAttendanceRequestModel
import com.example.digitracksdk.domain.model.attendance_model.ViewAttendanceResponseModel
import com.example.digitracksdk.domain.repository.attendance_repository.AttendanceRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ViewAttendanceUseCase constructor(private val attendanceRepository: AttendanceRepository) :
    UseCase<ViewAttendanceResponseModel, Any?>() {
    override suspend fun run(params: Any?): ViewAttendanceResponseModel {
        return attendanceRepository.callViewAttendanceApi(params as ViewAttendanceRequestModel)
    }
}