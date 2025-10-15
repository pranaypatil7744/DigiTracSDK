package com.example.digitracksdk.domain.usecase.my_letters_usecase

import com.example.digitracksdk.domain.model.my_letters.ViewCandidateLoiRequestModel
import com.example.digitracksdk.domain.model.my_letters.ViewCandidateLoiResponseModel
import com.example.digitracksdk.domain.repository.my_letters_repository.MyLettersRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ViewCandidateLoiUseCase constructor(private val myLettersRepository: MyLettersRepository) :
    UseCase<ViewCandidateLoiResponseModel, Any?>() {
    override suspend fun run(params: Any?): ViewCandidateLoiResponseModel {
        return myLettersRepository.callViewCandidateLoiApi(params as ViewCandidateLoiRequestModel)
    }
}