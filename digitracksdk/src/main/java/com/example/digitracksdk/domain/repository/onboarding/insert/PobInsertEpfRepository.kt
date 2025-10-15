package com.example.digitracksdk.domain.repository.onboarding.insert

import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertEpfModel
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertEpfResponseModel


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,15:44
 */
interface PobInsertEpfRepository {

    suspend fun callPobInsertEpfApi(request: POBInsertEpfModel): POBInsertEpfResponseModel

}

