package com.example.digitracksdk.domain.usecase.attendance_usecase

import com.example.digitracksdk.domain.model.attendance_model.AttendanceCycleRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceCycleResponseModel
import com.example.digitracksdk.domain.repository.attendance_repository.AttendanceRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

/**
 * Created by Mo Khurseed Ansari on 11-04-2023.
 */
class AttendanceCycleUseCase
constructor(private val repository: AttendanceRepository) :
    UseCase<AttendanceCycleResponseModel, Any?>()
{
    override suspend fun run(params: Any?): AttendanceCycleResponseModel {
        return repository.getAttendanceCycleApi(params as AttendanceCycleRequestModel)
    }
}