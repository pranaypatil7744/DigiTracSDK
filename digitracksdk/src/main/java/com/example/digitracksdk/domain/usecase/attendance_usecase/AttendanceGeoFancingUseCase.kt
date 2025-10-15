package com.example.digitracksdk.domain.usecase.attendance_usecase

import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceGeoFancingResponseModel
import com.example.digitracksdk.domain.repository.attendance_repository.AttendanceRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class AttendanceGeoFancingUseCase constructor(private val attendanceRepository: AttendanceRepository) :
    UseCase<AttendanceGeoFancingResponseModel, Any?>() {
    override suspend fun run(params: Any?): AttendanceGeoFancingResponseModel {
        return attendanceRepository.callAttendanceGeoFancingModel(params as CommonRequestModel)
    }
}