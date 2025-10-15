package com.example.digitracksdk.domain.usecase.my_letters_usecase

import com.example.digitracksdk.domain.model.my_letters.GetForm16RequestModel
import com.example.digitracksdk.domain.model.my_letters.GetForm16ResponseModel
import com.example.digitracksdk.domain.repository.my_letters_repository.MyLettersRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class GetForm16UseCase constructor(private val myLettersRepository: MyLettersRepository) :
    UseCase<GetForm16ResponseModel, Any?>() {
    override suspend fun run(params: Any?): GetForm16ResponseModel {
        return myLettersRepository.callGetForm16Api(params as GetForm16RequestModel)
    }
}