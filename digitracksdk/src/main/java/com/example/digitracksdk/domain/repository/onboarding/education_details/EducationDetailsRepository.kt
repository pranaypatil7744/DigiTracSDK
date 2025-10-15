package com.example.digitracksdk.domain.repository.onboarding.education_details

import com.example.digitracksdk.domain.model.onboarding.GetEducationCategoryResponseModel
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewEducationDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.educational_details.EducationalStreamRequestModel
import com.example.digitracksdk.domain.model.onboarding.educational_details.EducationalStreamResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.InsertEducationInfoRequestModel
import com.example.digitracksdk.domain.model.onboarding.insert.InsertEducationInfoResponseModel

interface EducationDetailsRepository {

    suspend fun callViewViewEducationDetailsApi(request: InnovIDRequestModel): PaperlessViewEducationDetailsResponseModel

    suspend fun callGetEducationCategoryApi(): GetEducationCategoryResponseModel

    suspend fun callPobInsertEducationInfoApi(request: InsertEducationInfoRequestModel): InsertEducationInfoResponseModel

    suspend fun callEducationalStreamApi(request : EducationalStreamRequestModel) : EducationalStreamResponseModel
}

