package com.example.digitracksdk.domain.usecase.mileage_tracking_usecase

import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageRegularizationListRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageRegularizationListResponseModel
import com.example.digitracksdk.domain.repository.mileage_tracking_repository.MileageTrackingRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class MileageRegularizationListUseCase constructor(private val mileageTrackingRepository: MileageTrackingRepository) :
    UseCase<MileageRegularizationListResponseModel, Any?>() {
    override suspend fun run(params: Any?): MileageRegularizationListResponseModel {
        return mileageTrackingRepository.callMileageRegularizationListApi(params as MileageRegularizationListRequestModel)
    }
}