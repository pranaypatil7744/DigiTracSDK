package com.example.digitracksdk.data.repository.onboarding

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.onboarding.pf_uan.GetPFESICRequestModel
import com.example.digitracksdk.domain.model.onboarding.pf_uan.GetPFESICResponseModel
import com.example.digitracksdk.domain.model.onboarding.pf_uan.InsertPFESICRequestModel
import com.example.digitracksdk.domain.model.onboarding.pf_uan.InsertPFESICResponseModel
import com.example.digitracksdk.domain.repository.onboarding.pf_uan.PaperlessPFESICRepository

/**
 * Created by Mo Khurseed Ansari on 18-10-2023.
 */
class PFESICRepositoryImp
    (private val apiService: ApiService) : PaperlessPFESICRepository {
    override suspend fun callInsertPFESICApi(request: InsertPFESICRequestModel): InsertPFESICResponseModel {

        return apiService.callInsertPFESICApi(request)
    }
    override suspend fun callGetPFESICApi(request: GetPFESICRequestModel): GetPFESICResponseModel {
        return apiService.callGetPFESICApi(request)
    }
}