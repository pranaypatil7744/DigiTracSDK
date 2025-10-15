package com.example.digitracksdk.domain.usecase.my_letters_usecase

import com.example.digitracksdk.domain.model.my_letters.OtherLettersRequestModel
import com.example.digitracksdk.domain.model.my_letters.OtherLettersResponseModel
import com.example.digitracksdk.domain.repository.my_letters_repository.MyLettersRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class OtherLettersUseCase constructor(private val myLettersRepository: MyLettersRepository):
    UseCase<OtherLettersResponseModel, Any?>(){
    override suspend fun run(params: Any?): OtherLettersResponseModel {
        return myLettersRepository.callOtherLettersApi(params as OtherLettersRequestModel)
    }

}