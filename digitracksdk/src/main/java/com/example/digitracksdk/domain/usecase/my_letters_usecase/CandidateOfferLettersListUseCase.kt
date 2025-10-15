package com.example.digitracksdk.domain.usecase.my_letters_usecase

import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.my_letters.CandidateOfferLetterListResponseModel
import com.example.digitracksdk.domain.repository.my_letters_repository.MyLettersRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class CandidateOfferLettersListUseCase constructor(private val myLettersRepository: MyLettersRepository) :
    UseCase<CandidateOfferLetterListResponseModel, Any?>() {
    override suspend fun run(params: Any?): CandidateOfferLetterListResponseModel {
        return myLettersRepository.callCandidateOfferLettersListApi(params as CommonRequestModel)
    }
}