package com.example.digitracksdk.domain.repository.onboarding

import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewEpfResponseModel


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:52
 */
interface PaperlessViewEpfRepository {

    suspend fun callViewEpfApi(request: InnovIDRequestModel): PaperlessViewEpfResponseModel
}