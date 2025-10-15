package com.example.digitracksdk.presentation.my_letters.other_letter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.my_letters.GetForm16RequestModel
import com.example.digitracksdk.domain.model.my_letters.GetForm16ResponseModel
import com.example.digitracksdk.domain.model.my_letters.OtherLettersRequestModel
import com.example.digitracksdk.domain.model.my_letters.OtherLettersResponseModel
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.my_letters.GetIncrementLetterResponseModel
import com.example.digitracksdk.domain.model.GnetIdRequestModel
import com.example.digitracksdk.domain.model.my_letters.GetFinancialYearsListRequestModel
import com.example.digitracksdk.domain.model.my_letters.GetFinancialYearsListResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.my_letters_usecase.GetFinancialYearListUseCase
import com.example.digitracksdk.domain.usecase.my_letters_usecase.GetForm16UseCase
import com.example.digitracksdk.domain.usecase.my_letters_usecase.GetIncrementLetterUseCase
import com.example.digitracksdk.domain.usecase.my_letters_usecase.OtherLettersUseCase

class OtherLetterViewModel constructor(
    private val otherLettersUseCase: OtherLettersUseCase,
    private val getForm16UseCase: GetForm16UseCase,
    private val getFinancialYearListUseCase: GetFinancialYearListUseCase,
    private val getIncrementLetterUseCase: GetIncrementLetterUseCase
) : ViewModel() {

    val otherLetterResponseData = MutableLiveData<OtherLettersResponseModel>()
    val getForm16ResponseData = MutableLiveData<GetForm16ResponseModel>()
    val getFinancialYearResponseData = MutableLiveData<GetFinancialYearsListResponseModel>()
    val getIncrementLettersResponseData = MutableLiveData<GetIncrementLetterResponseModel>()
    val messageData = MutableLiveData<String>()
    val showProgress = MutableLiveData<Boolean>()

    fun callGetIncrementLettersApi(request: GnetIdRequestModel) {
        showProgress.value=true
        getIncrementLetterUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<GetIncrementLetterResponseModel> {
                override fun onSuccess(result: GetIncrementLetterResponseModel) {
                    showProgress.value=false
                    getIncrementLettersResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgress.value=false
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callOtherLetterApi(request: OtherLettersRequestModel) {
        showProgress.value=true
        otherLettersUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<OtherLettersResponseModel> {
                override fun onSuccess(result: OtherLettersResponseModel) {
                    showProgress.value=false
                    otherLetterResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgress.value=false
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callGetFinancialYearListApi(request: GetFinancialYearsListRequestModel) {
        showProgress.value=true
        getFinancialYearListUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<GetFinancialYearsListResponseModel> {
                override fun onSuccess(result: GetFinancialYearsListResponseModel) {
                    showProgress.value=false
                    getFinancialYearResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgress.value=false
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callGetForm16Api(request: GetForm16RequestModel) {
        showProgress.value=true
        getForm16UseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<GetForm16ResponseModel> {
                override fun onSuccess(result: GetForm16ResponseModel) {
                    showProgress.value=false
                    getForm16ResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgress.value=false
                    messageData.value = apiError?.message.toString()
                }
            })
    }
}