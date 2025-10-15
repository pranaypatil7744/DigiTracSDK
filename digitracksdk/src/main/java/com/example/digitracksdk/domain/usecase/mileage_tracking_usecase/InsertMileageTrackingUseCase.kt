package com.example.digitracksdk.domain.usecase.mileage_tracking_usecase

import com.example.digitracksdk.domain.model.mileage_tracking_model.InsertMileageTrackingRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.InsertMileageTrackingResponseModel
import com.example.digitracksdk.domain.repository.mileage_tracking_repository.MileageTrackingRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class InsertMileageTrackingUseCase constructor(private val mileageTrackingRepository: MileageTrackingRepository) :
    UseCase<InsertMileageTrackingResponseModel, Any?>() {
    override suspend fun run(params: Any?): InsertMileageTrackingResponseModel {
        return mileageTrackingRepository.callInsertMileageTrackingApi(params as InsertMileageTrackingRequestModel)
    }
}