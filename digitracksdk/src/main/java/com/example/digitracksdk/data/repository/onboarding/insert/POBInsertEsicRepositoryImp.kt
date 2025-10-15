package com.example.digitracksdk.data.repository.onboarding.insert

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertESICDetailsModel
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertEsicResponseModel
import com.example.digitracksdk.domain.repository.onboarding.insert.PobInsertEsicRepository


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:59
 */
class POBInsertEsicRepositoryImp
    (private val apiService: ApiService) : PobInsertEsicRepository {

    override suspend fun callPobInsertEsicApi(request: POBInsertESICDetailsModel): POBInsertEsicResponseModel {
        return apiService.callPOBInsertESICDetailsApi(request)
    }
}