package com.example.digitracksdk.domain.usecase.dashboard

import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.home_model.response.CheckBirthdayResponseModel
import com.example.digitracksdk.domain.repository.home_repository.HomeRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class CheckBirthdayUseCase constructor(private val homeRepository: HomeRepository) :
    UseCase<CheckBirthdayResponseModel, Any?>() {
    override suspend fun run(params: Any?): CheckBirthdayResponseModel {
        return homeRepository.callCheckBirthdayApi(params as CommonRequestModel)
    }
}