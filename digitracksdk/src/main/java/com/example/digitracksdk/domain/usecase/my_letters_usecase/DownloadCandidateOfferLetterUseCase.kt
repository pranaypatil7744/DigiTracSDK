package com.example.digitracksdk.domain.usecase.my_letters_usecase

import com.example.digitracksdk.domain.model.my_letters.DownloadCandidateOfferLetterRequestModel
import com.example.digitracksdk.domain.model.my_letters.DownloadCandidateOfferLetterResponseModel
import com.example.digitracksdk.domain.repository.my_letters_repository.MyLettersRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class DownloadCandidateOfferLetterUseCase constructor(private val myLettersRepository: MyLettersRepository) :
    UseCase<DownloadCandidateOfferLetterResponseModel, Any?>() {
    override suspend fun run(params: Any?): DownloadCandidateOfferLetterResponseModel {
        return myLettersRepository.callDownloadCandidateOfferLetterApi(params as DownloadCandidateOfferLetterRequestModel)
    }
}