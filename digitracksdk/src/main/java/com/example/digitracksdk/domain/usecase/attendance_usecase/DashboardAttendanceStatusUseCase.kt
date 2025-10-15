package com.example.digitracksdk.domain.usecase.attendance_usecase

import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.attendance_model.DashboardAttendanceStatusResponseModel
import com.example.digitracksdk.domain.repository.attendance_repository.AttendanceRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class DashboardAttendanceStatusUseCase constructor(private val attendanceRepository: AttendanceRepository) :
    UseCase<DashboardAttendanceStatusResponseModel, Any?>() {
    override suspend fun run(params: Any?): DashboardAttendanceStatusResponseModel {
        return attendanceRepository.callDashboardAttendanceStatusApi(params as CommonRequestModel)
    }
}