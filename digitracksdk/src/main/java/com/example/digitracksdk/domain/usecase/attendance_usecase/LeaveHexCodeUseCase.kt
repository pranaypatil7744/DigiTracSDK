package com.example.digitracksdk.domain.usecase.attendance_usecase

import com.example.digitracksdk.domain.model.attendance_model.LeaveHexCodeRequestModel
import com.example.digitracksdk.domain.model.attendance_model.LeaveHexCodeResponseModel
import com.example.digitracksdk.domain.repository.attendance_repository.AttendanceRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

/**
 * Created by Mo Khurseed Ansari on 11-04-2023.
 */
class LeaveHexCodeUseCase
constructor(private val repository: AttendanceRepository) :
    UseCase<LeaveHexCodeResponseModel, Any>()
{
    override suspend fun run(params: Any?): LeaveHexCodeResponseModel {
       return repository.getAttendanceHexCodeApi(params as LeaveHexCodeRequestModel)
    }
}