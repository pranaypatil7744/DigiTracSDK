package com.example.digitracksdk.data.repository.resignation_repo_imp

import com.example.digitracksdk.domain.model.resignation.ResignationCategoryResponseModel
import com.example.digitracksdk.domain.model.resignation.ResignationListRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationListResponseModel
import com.example.digitracksdk.domain.model.resignation.ResignationNoticePeriodRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationNoticePeriodResponseModel
import com.example.digitracksdk.domain.model.resignation.ResignationReasonRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationReasonResponseModel
import com.example.digitracksdk.domain.model.resignation.RevokeResignationRequestModel
import com.example.digitracksdk.domain.model.resignation.RevokeResignationResponseModel
import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.resignation.ResignationRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationResponseModel
import com.example.digitracksdk.domain.repository.resignation.ResignationRepository

class ResignationRepositoryImp(private val apiService: ApiService): ResignationRepository {
    override suspend fun callResignationCategoryApi(): ResignationCategoryResponseModel {
        return apiService.callResignationCategoryApi()
    }

    override suspend fun callResignationApi(request: ResignationRequestModel): ResignationResponseModel {
        return apiService.callResignationRequestApi(request)
    }

    override suspend fun callResignationListApi(request: ResignationListRequestModel): ResignationListResponseModel {
        return apiService.callResignationListApi(request)
    }

    override suspend fun callResignationNoticePeriodApi(request: ResignationNoticePeriodRequestModel): ResignationNoticePeriodResponseModel {
        return apiService.callGetResignationNoticePeriodApi(request)
    }

    override suspend fun callRevokeResignationApi(request: RevokeResignationRequestModel): RevokeResignationResponseModel {
        return apiService.callRevokeResignationApi(request)
    }

    override suspend fun callResignationReasonApi(request: ResignationReasonRequestModel): ResignationReasonResponseModel {

        return apiService.callResignationReasonApi(request)
    }
}