package com.example.digitracksdk.domain.repository.profile_repository

import com.example.digitracksdk.domain.model.profile_model.CheckOtpRequestModel
import com.example.digitracksdk.domain.model.profile_model.CheckOtpResponseModel
import com.example.digitracksdk.domain.model.profile_model.CityListRequestModel
import com.example.digitracksdk.domain.model.profile_model.CityListResponseModel
import com.example.digitracksdk.domain.model.profile_model.StateListResponseModel
import com.example.digitracksdk.domain.model.profile_model.ValidCandidateRequestModel
import com.example.digitracksdk.domain.model.profile_model.ValidCandidateResponseModel
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.profile_model.InsertBasicDetailsRequestModel
import com.example.digitracksdk.domain.model.profile_model.InsertBasicDetailsResponseModel
import com.example.digitracksdk.domain.model.profile_model.NewInnovIdCreationRequestModel
import com.example.digitracksdk.domain.model.profile_model.NewInnovIdCreationResponseModel
import com.example.digitracksdk.domain.model.profile_model.UpdateProfileRequestModel
import com.example.digitracksdk.domain.model.profile_model.UpdateProfileResponseModel

interface ProfileRepository {

    suspend fun callStateListApi(request: CommonRequestModel): StateListResponseModel

    suspend fun callCityListApi(request: CityListRequestModel): CityListResponseModel

    suspend fun callUpdateProfileApi(request: UpdateProfileRequestModel): UpdateProfileResponseModel

    suspend fun callValidCandidateApi(request: ValidCandidateRequestModel): ValidCandidateResponseModel

    suspend fun callCheckOtpApi(request: CheckOtpRequestModel): CheckOtpResponseModel

    suspend fun callInsertBasicDetailsApi(request: InsertBasicDetailsRequestModel): InsertBasicDetailsResponseModel

    suspend fun callInnovIDCreationNewApi(request: NewInnovIdCreationRequestModel): NewInnovIdCreationResponseModel


}