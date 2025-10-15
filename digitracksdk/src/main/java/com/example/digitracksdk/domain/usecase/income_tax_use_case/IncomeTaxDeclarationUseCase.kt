package com.example.digitracksdk.domain.usecase.income_tax_use_case

import com.example.digitracksdk.domain.model.income_tax.IncomeTaxDeclarationRequestModel
import com.example.digitracksdk.domain.model.income_tax.IncomeTaxDeclarationResponseModel
import com.example.digitracksdk.domain.repository.income_tax.IncomeTaxRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class IncomeTaxDeclarationUseCase constructor(private val incomeTaxDeclarationRepository: IncomeTaxRepository) :
    UseCase<IncomeTaxDeclarationResponseModel, Any?>() {
    override suspend fun run(params: Any?): IncomeTaxDeclarationResponseModel {
        return incomeTaxDeclarationRepository.callIncomeTaxDeclarationApi(params as IncomeTaxDeclarationRequestModel)
    }
}