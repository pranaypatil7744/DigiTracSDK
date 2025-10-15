package com.example.digitracksdk.data.repository.profile_repo_iml

import com.example.digitracksdk.domain.model.profile_model.CheckOtpRequestModel
import com.example.digitracksdk.domain.model.profile_model.CheckOtpResponseModel
import com.example.digitracksdk.domain.model.profile_model.CityListRequestModel
import com.example.digitracksdk.domain.model.profile_model.CityListResponseModel
import com.example.digitracksdk.domain.model.profile_model.StateListResponseModel
import com.example.digitracksdk.domain.model.profile_model.ValidCandidateRequestModel
import com.example.digitracksdk.domain.model.profile_model.ValidCandidateResponseModel
import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.profile_model.InsertBasicDetailsRequestModel
import com.example.digitracksdk.domain.model.profile_model.InsertBasicDetailsResponseModel
import com.example.digitracksdk.domain.model.profile_model.NewInnovIdCreationRequestModel
import com.example.digitracksdk.domain.model.profile_model.NewInnovIdCreationResponseModel
import com.example.digitracksdk.domain.model.profile_model.UpdateProfileRequestModel
import com.example.digitracksdk.domain.model.profile_model.UpdateProfileResponseModel
import com.example.digitracksdk.domain.repository.profile_repository.ProfileRepository

class ProfileRepositoryImp(private val apiService: ApiService) : ProfileRepository {
    override suspend fun callStateListApi(request: CommonRequestModel): StateListResponseModel {
        return apiService.callStateListApi(request)
    }

    override suspend fun callCityListApi(request: CityListRequestModel): CityListResponseModel {
        return apiService.callCityListApi(request)
    }

    override suspend fun callUpdateProfileApi(request: UpdateProfileRequestModel): UpdateProfileResponseModel {
        return apiService.callUpdateProfileApi(request)
    }

    override suspend fun callValidCandidateApi(request: ValidCandidateRequestModel): ValidCandidateResponseModel {
        return apiService.callValidCandidateApi(request)
    }

    override suspend fun callCheckOtpApi(request: CheckOtpRequestModel): CheckOtpResponseModel {
        return apiService.callCheckOtpApi(request)
    }

    override suspend fun callInsertBasicDetailsApi(request: InsertBasicDetailsRequestModel): InsertBasicDetailsResponseModel {
        return apiService.callInsertBasicDetailsApi(request)
    }

    override suspend fun callInnovIDCreationNewApi(request: NewInnovIdCreationRequestModel): NewInnovIdCreationResponseModel {
        return apiService.callInnovIDCreationNewApi(request)
    }
}