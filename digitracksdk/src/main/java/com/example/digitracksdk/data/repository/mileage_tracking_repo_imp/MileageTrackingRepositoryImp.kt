package com.example.digitracksdk.data.repository.mileage_tracking_repo_imp

import com.example.digitracksdk.domain.model.mileage_tracking_model.InsertMileageRegularizationRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.InsertMileageRegularizationResponseModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.InsertMileageTrackingRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.InsertMileageTrackingResponseModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageRegularizationListRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageRegularizationListResponseModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageTrackingFlagRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageTrackingFlagResponseModel
import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageTrackingListRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageTrackingListResponseModel
import com.example.digitracksdk.domain.repository.mileage_tracking_repository.MileageTrackingRepository

class MileageTrackingRepositoryImp(private val apiService: ApiService) : MileageTrackingRepository {
    override suspend fun callInsertMileageRegularizationApi(request: InsertMileageRegularizationRequestModel): InsertMileageRegularizationResponseModel {
        return apiService.callInsertMileageRegularizationApi(request)
    }

    override suspend fun callMileageRegularizationListApi(request: MileageRegularizationListRequestModel): MileageRegularizationListResponseModel {
        return apiService.callMileageRegularizationListApi(request)
    }

    override suspend fun callInsertMileageTrackingApi(request: InsertMileageTrackingRequestModel): InsertMileageTrackingResponseModel {
        return apiService.callInsertMileageTrackingApi(request)
    }

    override suspend fun callMileageTrackingListApi(request: MileageTrackingListRequestModel): MileageTrackingListResponseModel {
        return apiService.callMileageTrackingListApi(request)
    }

    override suspend fun callMileageTrackingFlagApi(request: MileageTrackingFlagRequestModel): MileageTrackingFlagResponseModel {
        return apiService.callMileageTrackingFlagApi(request)
    }
}