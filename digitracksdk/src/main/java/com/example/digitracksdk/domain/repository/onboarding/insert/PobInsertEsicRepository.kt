package com.example.digitracksdk.domain.repository.onboarding.insert

import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertESICDetailsModel
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertEsicResponseModel


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,15:44
 */
interface PobInsertEsicRepository {

    suspend fun callPobInsertEsicApi(request: POBInsertESICDetailsModel): POBInsertEsicResponseModel

}

