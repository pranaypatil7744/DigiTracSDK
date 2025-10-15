package com.example.digitracksdk.data.repository.onboarding.insert

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertWorkExpModel
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertWorkExpResponseModel
import com.example.digitracksdk.domain.repository.onboarding.insert.PobInsertWorkExpRepository


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:59
 */
class POBWorkExpRepositoryImp
    (private val apiService: ApiService) : PobInsertWorkExpRepository {

    override suspend fun callPobInsertWorkExpApi(request: POBInsertWorkExpModel): POBInsertWorkExpResponseModel {
        return apiService.callPOBInsertWorkExpApi(request)
    }
}