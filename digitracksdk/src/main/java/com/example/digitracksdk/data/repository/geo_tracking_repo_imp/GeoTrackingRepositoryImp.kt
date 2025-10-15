package com.example.digitracksdk.data.repository.geo_tracking_repo_imp

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingDetailsRequestModel
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingDetailsResponseModel
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingSummaryListRequestModel
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingSummaryListResponseModel
import com.example.digitracksdk.domain.repository.geo_tracking_repository.GeoTrackingRepository

class GeoTrackingRepositoryImp(private val apiService: ApiService) : GeoTrackingRepository {
    override suspend fun callGeoTrackingSummary(request: GeoTrackingSummaryListRequestModel): GeoTrackingSummaryListResponseModel {
        return apiService.callGetGeoTrackingSummaryListApi(request)
    }

    override suspend fun callGeoTrackingDetails(request: GeoTrackingDetailsRequestModel): GeoTrackingDetailsResponseModel {

        return apiService.callGeoTrackingDetailsApi(request)
    }

}