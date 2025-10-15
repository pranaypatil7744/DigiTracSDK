package com.example.digitracksdk.data.repository.onboarding

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewWorkExpResponseModel
import com.example.digitracksdk.domain.model.onboarding.work_experience.InsertWorkExpRequestModel
import com.example.digitracksdk.domain.model.onboarding.work_experience.InsertWorkExpResponseModel
import com.example.digitracksdk.domain.repository.onboarding.PaperlessViewWorkExpDetailsRepository


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:59
 */
class PaperlessViewWorkExpDetailsRepositoryImp
    (private val apiService: ApiService) : PaperlessViewWorkExpDetailsRepository {

    override suspend fun callViewWorkExpDetailsApi(request: InnovIDRequestModel): PaperlessViewWorkExpResponseModel {
        return apiService.callGetWorkExpDetailsApi(request)
    }

    override suspend fun callInsertWorkExpApi(request: InsertWorkExpRequestModel): InsertWorkExpResponseModel {
        return apiService.callInsertWorkExpApi(request)
    }
}