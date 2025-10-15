package com.example.digitracksdk.domain.usecase.attendance_usecase

import com.example.digitracksdk.domain.repository.attendance_repository.AttendanceRepository
import com.example.digitracksdk.domain.usecase.base.UseCase
import com.innov.digitrac.webservice_api.request_resonse.AttendanceMarkRequestModel
import com.innov.digitrac.webservice_api.request_resonse.AttendanceMarkResponseModel

class AttendanceMarkUseCase constructor(private val attendanceRepository: AttendanceRepository) :
    UseCase<AttendanceMarkResponseModel, Any?>() {
    override suspend fun run(params: Any?): AttendanceMarkResponseModel {
        return attendanceRepository.callAttendanceMarkApi(params as AttendanceMarkRequestModel)
    }
}