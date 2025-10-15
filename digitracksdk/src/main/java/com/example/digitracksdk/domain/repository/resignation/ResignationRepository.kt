package com.example.digitracksdk.domain.repository.resignation

import com.example.digitracksdk.domain.model.resignation.ResignationCategoryResponseModel
import com.example.digitracksdk.domain.model.resignation.ResignationListRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationListResponseModel
import com.example.digitracksdk.domain.model.resignation.ResignationNoticePeriodRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationNoticePeriodResponseModel
import com.example.digitracksdk.domain.model.resignation.ResignationReasonRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationReasonResponseModel
import com.example.digitracksdk.domain.model.resignation.ResignationRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationResponseModel
import com.example.digitracksdk.domain.model.resignation.RevokeResignationRequestModel
import com.example.digitracksdk.domain.model.resignation.RevokeResignationResponseModel

interface ResignationRepository {

    suspend fun callResignationCategoryApi(): ResignationCategoryResponseModel

    suspend fun callResignationApi(request: ResignationRequestModel): ResignationResponseModel

    suspend fun callResignationListApi(request: ResignationListRequestModel): ResignationListResponseModel

    suspend fun callResignationNoticePeriodApi(request  : ResignationNoticePeriodRequestModel) : ResignationNoticePeriodResponseModel

    suspend fun callRevokeResignationApi(request : RevokeResignationRequestModel) : RevokeResignationResponseModel

    suspend fun callResignationReasonApi(request : ResignationReasonRequestModel) : ResignationReasonResponseModel
}