package com.example.digitracksdk.domain.repository.income_tax

import com.example.digitracksdk.domain.model.income_tax.IncomeTaxDeclarationRequestModel
import com.example.digitracksdk.domain.model.income_tax.IncomeTaxDeclarationResponseModel

interface IncomeTaxRepository {

    suspend fun callIncomeTaxDeclarationApi(request: IncomeTaxDeclarationRequestModel): IncomeTaxDeclarationResponseModel

}