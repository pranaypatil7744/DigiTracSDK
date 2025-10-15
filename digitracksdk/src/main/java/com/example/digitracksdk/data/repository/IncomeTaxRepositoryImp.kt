package com.example.digitracksdk.data.repository

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.income_tax.IncomeTaxDeclarationRequestModel
import com.example.digitracksdk.domain.model.income_tax.IncomeTaxDeclarationResponseModel
import com.example.digitracksdk.domain.repository.income_tax.IncomeTaxRepository

class IncomeTaxRepositoryImp constructor(private val apiService: ApiService) :
    IncomeTaxRepository {
    override suspend fun callIncomeTaxDeclarationApi(request: IncomeTaxDeclarationRequestModel): IncomeTaxDeclarationResponseModel {
        return apiService.callIncomeTaxDeclarationApi(request)
    }
}