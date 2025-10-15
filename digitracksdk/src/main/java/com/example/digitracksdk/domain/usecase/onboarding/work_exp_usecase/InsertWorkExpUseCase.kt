package com.example.digitracksdk.domain.usecase.onboarding.work_exp_usecase

import com.example.digitracksdk.domain.model.onboarding.work_experience.InsertWorkExpRequestModel
import com.example.digitracksdk.domain.model.onboarding.work_experience.InsertWorkExpResponseModel
import com.example.digitracksdk.domain.repository.onboarding.PaperlessViewWorkExpDetailsRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class InsertWorkExpUseCase constructor(private val paperlessViewWorkExpDetailsRepository: PaperlessViewWorkExpDetailsRepository) :
    UseCase<InsertWorkExpResponseModel, Any?>() {
    override suspend fun run(params: Any?): InsertWorkExpResponseModel {
        return paperlessViewWorkExpDetailsRepository.callInsertWorkExpApi(params as InsertWorkExpRequestModel)
    }
}