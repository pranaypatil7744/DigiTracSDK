package com.example.digitracksdk.domain.repository.onboarding

import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewWorkExpResponseModel
import com.example.digitracksdk.domain.model.onboarding.work_experience.InsertWorkExpRequestModel
import com.example.digitracksdk.domain.model.onboarding.work_experience.InsertWorkExpResponseModel


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,15:44
 */
interface PaperlessViewWorkExpDetailsRepository {

    suspend fun callViewWorkExpDetailsApi(request: InnovIDRequestModel): PaperlessViewWorkExpResponseModel

    suspend fun callInsertWorkExpApi(request: InsertWorkExpRequestModel): InsertWorkExpResponseModel



}

