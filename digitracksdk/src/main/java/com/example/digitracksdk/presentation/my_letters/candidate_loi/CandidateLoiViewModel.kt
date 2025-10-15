package com.example.digitracksdk.presentation.my_letters.candidate_loi

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.my_letters.CandidateLoiListResponseModel
import com.example.digitracksdk.domain.model.my_letters.OtherLettersResponseModel
import com.example.digitracksdk.domain.model.my_letters.ViewCandidateLoiRequestModel
import com.example.digitracksdk.domain.model.my_letters.ViewCandidateLoiResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.my_letters_usecase.CandidateLoiListUseCase
import com.example.digitracksdk.domain.usecase.my_letters_usecase.ViewCandidateLoiUseCase
import kotlinx.coroutines.cancel

class CandidateLoiViewModel constructor(
    private val candidateLoiUseCase: CandidateLoiListUseCase,
    private val viewCandidateLoiUseCase: ViewCandidateLoiUseCase
) : ViewModel() {

    val candidateLoiListResponseData = MutableLiveData<CandidateLoiListResponseModel>()
    val otherLetterResponseData = MutableLiveData<OtherLettersResponseModel>()
    val viewCandidateLoiResponseData =
        MutableLiveData<ViewCandidateLoiResponseModel>()
    val messageData = MutableLiveData<String>()


    fun callCandidateLoiListApi(request: CommonRequestModel) {
        candidateLoiUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<CandidateLoiListResponseModel> {
                override fun onSuccess(result: CandidateLoiListResponseModel) {
                    candidateLoiListResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callViewCandidateLoiApi(request: ViewCandidateLoiRequestModel) {
        viewCandidateLoiUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ViewCandidateLoiResponseModel> {
                override fun onSuccess(result: ViewCandidateLoiResponseModel) {
                    viewCandidateLoiResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}