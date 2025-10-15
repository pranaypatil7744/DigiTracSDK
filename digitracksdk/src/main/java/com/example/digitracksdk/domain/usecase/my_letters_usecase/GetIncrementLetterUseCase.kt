package com.example.digitracksdk.domain.usecase.my_letters_usecase

import com.example.digitracksdk.domain.model.GnetIdRequestModel
import com.example.digitracksdk.domain.model.my_letters.GetIncrementLetterResponseModel
import com.example.digitracksdk.domain.repository.my_letters_repository.MyLettersRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class GetIncrementLetterUseCase constructor(private val myLettersRepository: MyLettersRepository) :
    UseCase<GetIncrementLetterResponseModel, Any?>() {
    override suspend fun run(params: Any?): GetIncrementLetterResponseModel {
        return myLettersRepository.callGetIncrementLetterApi(params as GnetIdRequestModel)
    }
}