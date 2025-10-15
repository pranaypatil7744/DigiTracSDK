package com.example.digitracksdk.domain.usecase.attendance_usecase

import com.example.digitracksdk.domain.repository.attendance_repository.AttendanceRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class InsertAttendancePicUseCase constructor(private val attendanceRepository: AttendanceRepository) :
    UseCase<InsertAttendancePicResponseModel, Any?>() {
    override suspend fun run(params: Any?): InsertAttendancePicResponseModel {
        return attendanceRepository.callInsertAttendancePicApi(params as InsertAttendancePicRequestModel)
    }
}