package com.example.digitracksdk.presentation.home.geo_tracking

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingDetailsRequestModel
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingDetailsResponseModel
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingSummaryListRequestModel
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingSummaryListResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.geo_tracking_usecase.GeoTrackingDetailsUseCase
import com.example.digitracksdk.domain.usecase.geo_tracking_usecase.GeoTrackingUseCase
import kotlinx.coroutines.cancel

class GeoTrackingSummaryViewModel constructor(
    private val geoTrackingSummaryUseCase: GeoTrackingUseCase,
    private val geoTrackingDetailsUseCase: GeoTrackingDetailsUseCase

) : ViewModel() {

    val messageData = MutableLiveData<String>()
    val showProgressBar = MutableLiveData<Boolean>()
    val geoTrackingSummaryListResponseData = MutableLiveData<GeoTrackingSummaryListResponseModel>()
    val geoTrackingDetailsResponseData = MutableLiveData<GeoTrackingDetailsResponseModel>()

    fun callGeoTrackingSummaryListApi(request: GeoTrackingSummaryListRequestModel) {
        showProgressBar.value = true
        geoTrackingSummaryUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<GeoTrackingSummaryListResponseModel> {
                override fun onSuccess(result: GeoTrackingSummaryListResponseModel) {
                    showProgressBar.value = false
                    geoTrackingSummaryListResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            }
        )
    }

    fun callGeoTrackingDetailsApi(request: GeoTrackingDetailsRequestModel) {
        showProgressBar.value = true
        geoTrackingDetailsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<GeoTrackingDetailsResponseModel> {
                override fun onSuccess(result: GeoTrackingDetailsResponseModel) {
                    showProgressBar.value = false
                    geoTrackingDetailsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            }
        )
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()

    }
}