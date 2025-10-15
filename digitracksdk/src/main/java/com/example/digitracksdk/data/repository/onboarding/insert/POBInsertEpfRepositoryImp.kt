package com.example.digitracksdk.data.repository.onboarding.insert

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertEpfModel
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertEpfResponseModel
import com.example.digitracksdk.domain.repository.onboarding.insert.PobInsertEpfRepository


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:59
 */
class POBInsertEpfRepositoryImp
    (private val apiService: ApiService) : PobInsertEpfRepository {

    override suspend fun callPobInsertEpfApi(request: POBInsertEpfModel): POBInsertEpfResponseModel {
        return apiService.callPOBInsertEpfApi(request)
    }
}