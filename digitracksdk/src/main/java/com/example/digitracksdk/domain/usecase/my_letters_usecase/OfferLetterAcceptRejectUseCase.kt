package com.example.digitracksdk.domain.usecase.my_letters_usecase

import com.example.digitracksdk.domain.model.my_letters.OfferLetterAcceptRejectRequestModel
import com.example.digitracksdk.domain.model.my_letters.OfferLetterAcceptRejectResponseModel
import com.example.digitracksdk.domain.repository.my_letters_repository.MyLettersRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class OfferLetterAcceptRejectUseCase constructor(private val myLettersRepository: MyLettersRepository):
    UseCase<OfferLetterAcceptRejectResponseModel, Any?>() {
    override suspend fun run(params: Any?): OfferLetterAcceptRejectResponseModel {
        return myLettersRepository.callOfferLetterAcceptRejectApi(params as OfferLetterAcceptRejectRequestModel)
    }
}