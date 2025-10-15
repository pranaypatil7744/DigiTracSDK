package com.example.digitracksdk.domain.repository.onboarding.family_details

import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewFamilyDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.PaperlessFamilyDetailsModel
import com.example.digitracksdk.domain.model.onboarding.insert.PaperlessFamilyDetailsResponseModel

interface FamilyDetailsRepository {

    suspend fun callViewViewFamilyDetailsApi(request: InnovIDRequestModel): PaperlessViewFamilyDetailsResponseModel

    suspend fun callFamilyDetailsApi(request: PaperlessFamilyDetailsModel): PaperlessFamilyDetailsResponseModel

}

