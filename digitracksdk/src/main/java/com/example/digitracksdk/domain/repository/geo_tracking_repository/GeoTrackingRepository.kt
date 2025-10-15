package com.example.digitracksdk.domain.repository.geo_tracking_repository

import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingDetailsRequestModel
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingDetailsResponseModel
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingSummaryListRequestModel
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingSummaryListResponseModel

interface GeoTrackingRepository
{
    suspend fun callGeoTrackingSummary (request : GeoTrackingSummaryListRequestModel) : GeoTrackingSummaryListResponseModel
    suspend fun callGeoTrackingDetails(request : GeoTrackingDetailsRequestModel) : GeoTrackingDetailsResponseModel
}