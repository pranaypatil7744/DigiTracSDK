package com.example.digitracksdk.domain.usecase.geo_tracking_usecase

import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingSummaryListRequestModel
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingSummaryListResponseModel
import com.example.digitracksdk.domain.repository.geo_tracking_repository.GeoTrackingRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class GeoTrackingUseCase
constructor(
    private val repository: GeoTrackingRepository
) : UseCase<GeoTrackingSummaryListResponseModel, Any>() {
    override suspend fun run(params: Any?): GeoTrackingSummaryListResponseModel {
        return repository.callGeoTrackingSummary(params as GeoTrackingSummaryListRequestModel)
    }
}