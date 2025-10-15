package com.example.digitracksdk.domain.repository.onboarding.insert

import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertWorkExpModel
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertWorkExpResponseModel


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,15:44
 */
interface PobInsertWorkExpRepository {

    suspend fun callPobInsertWorkExpApi(request: POBInsertWorkExpModel): POBInsertWorkExpResponseModel

}

