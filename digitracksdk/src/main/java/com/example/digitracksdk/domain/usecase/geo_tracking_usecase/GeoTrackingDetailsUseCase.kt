package com.example.digitracksdk.domain.usecase.geo_tracking_usecase

import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingDetailsRequestModel
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingDetailsResponseModel
import com.example.digitracksdk.domain.repository.geo_tracking_repository.GeoTrackingRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

/**
 * Created by Mo Khurseed Ansari on 26-Jul-2022.
 */
class GeoTrackingDetailsUseCase
constructor(
    private val repository: GeoTrackingRepository

) : UseCase<GeoTrackingDetailsResponseModel, Any>()
{
    override suspend fun run(params: Any?): GeoTrackingDetailsResponseModel {
      return repository.callGeoTrackingDetails(params as GeoTrackingDetailsRequestModel)
    }
}