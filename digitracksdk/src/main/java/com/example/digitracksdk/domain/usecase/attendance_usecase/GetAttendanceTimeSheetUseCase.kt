package com.example.digitracksdk.domain.usecase.attendance_usecase

import com.example.digitracksdk.domain.model.attendance_model.AttendanceTimeSheetRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceTimeSheetResponseModel
import com.example.digitracksdk.domain.repository.attendance_repository.AttendanceRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class GetAttendanceTimeSheetUseCase constructor(private val attendanceRepository: AttendanceRepository) :
    UseCase<AttendanceTimeSheetResponseModel, Any?>() {
    override suspend fun run(params: Any?): AttendanceTimeSheetResponseModel {
        return attendanceRepository.callAttendanceTimeSheetApi(params as AttendanceTimeSheetRequestModel)
    }
}