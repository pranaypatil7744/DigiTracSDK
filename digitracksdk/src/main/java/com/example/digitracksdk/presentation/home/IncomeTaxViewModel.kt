package com.example.digitracksdk.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.income_tax.IncomeTaxDeclarationRequestModel
import com.example.digitracksdk.domain.model.income_tax.IncomeTaxDeclarationResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.income_tax_use_case.IncomeTaxDeclarationUseCase

class IncomeTaxViewModel constructor(
    private val incomeTaxDeclarationUseCase: IncomeTaxDeclarationUseCase
) : ViewModel() {
    val incomeTaxDeclarationResponseData = MutableLiveData<IncomeTaxDeclarationResponseModel>()
    var messageData = MutableLiveData<String>()

    fun callIncomeTaxDeclarationApi(request: IncomeTaxDeclarationRequestModel) {
        incomeTaxDeclarationUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<IncomeTaxDeclarationResponseModel> {
                override fun onSuccess(result: IncomeTaxDeclarationResponseModel) {
                    incomeTaxDeclarationResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }
}