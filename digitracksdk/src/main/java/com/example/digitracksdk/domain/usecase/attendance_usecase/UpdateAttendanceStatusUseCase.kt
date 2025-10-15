package com.example.digitracksdk.domain.usecase.attendance_usecase

import com.example.digitracksdk.domain.model.attendance_model.UpdateAttendanceStatusRequestModel
import com.example.digitracksdk.domain.model.attendance_model.UpdateAttendanceStatusResponseModel
import com.example.digitracksdk.domain.repository.attendance_repository.AttendanceRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class UpdateAttendanceStatusUseCase constructor(private val attendanceRepository: AttendanceRepository):
    UseCase<UpdateAttendanceStatusResponseModel, Any?>() {
    override suspend fun run(params: Any?): UpdateAttendanceStatusResponseModel {
        return attendanceRepository.callUpdateAttendanceStatusApi(params as UpdateAttendanceStatusRequestModel)
    }
}