package com.example.digitracksdk.domain.usecase.login_usecase

import com.example.digitracksdk.domain.model.login_model.FirebaseTokenUpdateRequestModel
import com.example.digitracksdk.domain.model.login_model.FirebaseTokenUpdateResponseModel
import com.example.digitracksdk.domain.repository.login_repository.LoginRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class FirebaseTokenUpdateUseCase constructor(private val loginRepository: LoginRepository):
    UseCase<FirebaseTokenUpdateResponseModel, Any?>() {
    override suspend fun run(params: Any?): FirebaseTokenUpdateResponseModel {
        return loginRepository.callFirebaseTokenUpdateApi(params as FirebaseTokenUpdateRequestModel)
    }
}