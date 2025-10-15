package com.example.digitracksdk.domain.usecase.mileage_tracking_usecase

import com.example.digitracksdk.domain.model.mileage_tracking_model.InsertMileageRegularizationRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.InsertMileageRegularizationResponseModel
import com.example.digitracksdk.domain.repository.mileage_tracking_repository.MileageTrackingRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class InsertMileageRegularizationUseCase constructor(private val mileageTrackingRepository: MileageTrackingRepository) :
    UseCase<InsertMileageRegularizationResponseModel, Any?>() {
    override suspend fun run(params: Any?): InsertMileageRegularizationResponseModel {
        return mileageTrackingRepository.callInsertMileageRegularizationApi(params as InsertMileageRegularizationRequestModel
        )
    }
}