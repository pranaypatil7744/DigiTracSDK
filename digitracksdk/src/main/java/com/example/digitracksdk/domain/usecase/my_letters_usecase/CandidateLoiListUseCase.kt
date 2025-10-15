package com.example.digitracksdk.domain.usecase.my_letters_usecase

import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.my_letters.CandidateLoiListResponseModel
import com.example.digitracksdk.domain.repository.my_letters_repository.MyLettersRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class CandidateLoiListUseCase
constructor(private val myLettersRepository: MyLettersRepository) :
    UseCase<CandidateLoiListResponseModel, Any?>() {
    override suspend fun run(params: Any?): CandidateLoiListResponseModel {
        return myLettersRepository.callCandidateLoiListApi(params as CommonRequestModel)
    }
}