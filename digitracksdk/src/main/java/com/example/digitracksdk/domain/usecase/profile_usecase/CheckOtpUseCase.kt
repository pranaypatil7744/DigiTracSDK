package com.example.digitracksdk.domain.usecase.profile_usecase

import com.example.digitracksdk.domain.model.profile_model.CheckOtpRequestModel
import com.example.digitracksdk.domain.model.profile_model.CheckOtpResponseModel
import com.example.digitracksdk.domain.repository.profile_repository.ProfileRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class CheckOtpUseCase constructor(private val profileRepository: ProfileRepository):
    UseCase<CheckOtpResponseModel, Any?>() {
    override suspend fun run(params: Any?): CheckOtpResponseModel {
        return profileRepository.callCheckOtpApi(params as CheckOtpRequestModel)
    }
}