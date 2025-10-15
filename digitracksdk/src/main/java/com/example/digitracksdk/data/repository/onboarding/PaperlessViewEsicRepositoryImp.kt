package com.example.digitracksdk.data.repository.onboarding

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewEsicResponseModel
import com.example.digitracksdk.domain.repository.onboarding.PaperlessViewEsicRepository


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:59
 */
class PaperlessViewEsicRepositoryImp
    (private val apiService: ApiService) : PaperlessViewEsicRepository {
    override suspend fun callViewEsicApi(request: InnovIDRequestModel): PaperlessViewEsicResponseModel {

        return apiService.callGetEsicDetailsApi(request)
    }
}