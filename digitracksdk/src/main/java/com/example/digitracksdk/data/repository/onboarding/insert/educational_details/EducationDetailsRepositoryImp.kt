package com.example.digitracksdk.data.repository.onboarding.insert.educational_details

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.onboarding.GetEducationCategoryResponseModel
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewEducationDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.educational_details.EducationalStreamRequestModel
import com.example.digitracksdk.domain.model.onboarding.educational_details.EducationalStreamResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.InsertEducationInfoRequestModel
import com.example.digitracksdk.domain.model.onboarding.insert.InsertEducationInfoResponseModel
import com.example.digitracksdk.domain.repository.onboarding.education_details.EducationDetailsRepository

class EducationDetailsRepositoryImp
    (private val apiService: ApiService) : EducationDetailsRepository {
    override suspend fun callViewViewEducationDetailsApi(request: InnovIDRequestModel): PaperlessViewEducationDetailsResponseModel {
        return apiService.callGetEducationDetailsApi(request)
    }

    override suspend fun callGetEducationCategoryApi(): GetEducationCategoryResponseModel {
        return apiService.callGetEducationCategoryApi()
    }

    override suspend fun callPobInsertEducationInfoApi(request: InsertEducationInfoRequestModel): InsertEducationInfoResponseModel {
        return apiService.callPOBInsertEducationInfoApi(request)
    }

    override suspend fun callEducationalStreamApi(request: EducationalStreamRequestModel): EducationalStreamResponseModel {
    return apiService.callEducationalStream(request)
    }
}