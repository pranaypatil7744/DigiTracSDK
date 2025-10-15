package com.example.digitracksdk.domain.usecase.mileage_tracking_usecase

import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageTrackingFlagRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageTrackingFlagResponseModel
import com.example.digitracksdk.domain.repository.mileage_tracking_repository.MileageTrackingRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class MileageTrackingFlagUseCase constructor(private val mileageTrackingRepository: MileageTrackingRepository) :
    UseCase<MileageTrackingFlagResponseModel, Any?>() {
    override suspend fun run(params: Any?): MileageTrackingFlagResponseModel {
        return mileageTrackingRepository.callMileageTrackingFlagApi(params as MileageTrackingFlagRequestModel)
    }
}