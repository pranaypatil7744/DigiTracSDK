package com.example.digitracksdk.domain.repository.onboarding

import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewGetCandidateReferenceDetailsResponseModel


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,15:44
 */
interface PaperlessViewCandidateReferenceDetailsRepository {

    suspend fun callViewCandidateReferenceDetailsApi(request: InnovIDRequestModel): PaperlessViewGetCandidateReferenceDetailsResponseModel

}

