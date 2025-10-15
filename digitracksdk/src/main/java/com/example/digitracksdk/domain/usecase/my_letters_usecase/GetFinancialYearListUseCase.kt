package com.example.digitracksdk.domain.usecase.my_letters_usecase

import com.example.digitracksdk.domain.model.my_letters.GetFinancialYearsListRequestModel
import com.example.digitracksdk.domain.model.my_letters.GetFinancialYearsListResponseModel
import com.example.digitracksdk.domain.repository.my_letters_repository.MyLettersRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class GetFinancialYearListUseCase constructor(private val myLettersRepository: MyLettersRepository) :
    UseCase<GetFinancialYearsListResponseModel, Any?>() {
    override suspend fun run(params: Any?): GetFinancialYearsListResponseModel {
        return myLettersRepository.callGetFinancialYearListApi(params as GetFinancialYearsListRequestModel)
    }
}