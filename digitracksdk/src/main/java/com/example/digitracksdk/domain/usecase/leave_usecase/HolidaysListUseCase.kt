package com.example.digitracksdk.domain.usecase.leave_usecase

import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.leaves.HolidaysListResponseModel
import com.example.digitracksdk.domain.repository.leaves_repository.LeavesRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class HolidaysListUseCase constructor(private val leavesRepository: LeavesRepository) :
    UseCase<HolidaysListResponseModel, Any?>() {
    override suspend fun run(params: Any?): HolidaysListResponseModel {
        return leavesRepository.callHolidaysListApi(params as CommonRequestModel)
    }
}