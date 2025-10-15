package com.example.digitracksdk.domain.usecase.mileage_tracking_usecase

import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageTrackingListRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageTrackingListResponseModel
import com.example.digitracksdk.domain.repository.mileage_tracking_repository.MileageTrackingRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class MileageTrackingListUseCase constructor(private val mileageTrackingRepository: MileageTrackingRepository
): UseCase<MileageTrackingListResponseModel, Any?>() {
    override suspend fun run(params: Any?): MileageTrackingListResponseModel {
        return mileageTrackingRepository.callMileageTrackingListApi(params as MileageTrackingListRequestModel)
    }
}