package com.example.digitracksdk.domain.usecase.attendance_usecase

import com.example.digitracksdk.domain.model.attendance_model.CurrentDayAttendanceStatusRequestModel
import com.example.digitracksdk.domain.model.attendance_model.CurrentDayAttendanceStatusResponseModel
import com.example.digitracksdk.domain.repository.attendance_repository.AttendanceRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class CurrentDayAtteStatusUseCase constructor(private val attendanceRepository: AttendanceRepository) :
    UseCase<CurrentDayAttendanceStatusResponseModel, Any?>() {
    override suspend fun run(params: Any?): CurrentDayAttendanceStatusResponseModel {
        return attendanceRepository.callCurrentDayAttendanceStatusApi(params as CurrentDayAttendanceStatusRequestModel)
    }
}