package com.example.digitracksdk.data.repository.onboarding

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewEpfResponseModel
import com.example.digitracksdk.domain.repository.onboarding.PaperlessViewEpfRepository


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:59
 */
class PaperlessViewEpfRepositoryImp
    (private val apiService: ApiService) : PaperlessViewEpfRepository {
    override suspend fun callViewEpfApi(request: InnovIDRequestModel): PaperlessViewEpfResponseModel {

        return apiService.callGetEpfDetailsApi(request)
    }
}