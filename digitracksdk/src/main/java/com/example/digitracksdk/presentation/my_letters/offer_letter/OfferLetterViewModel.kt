package com.example.digitracksdk.presentation.my_letters.offer_letter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.my_letters.CandidateOfferLetterListResponseModel
import com.example.digitracksdk.domain.model.my_letters.DownloadCandidateOfferLetterRequestModel
import com.example.digitracksdk.domain.model.my_letters.DownloadCandidateOfferLetterResponseModel
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.my_letters.OfferLetterAcceptRejectRequestModel
import com.example.digitracksdk.domain.model.my_letters.OfferLetterAcceptRejectResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.my_letters_usecase.CandidateOfferLettersListUseCase
import com.example.digitracksdk.domain.usecase.my_letters_usecase.DownloadCandidateOfferLetterUseCase
import com.example.digitracksdk.domain.usecase.my_letters_usecase.OfferLetterAcceptRejectUseCase
import kotlinx.coroutines.cancel

class OfferLetterViewModel constructor(
    private val candidateOfferLetterUseCase: CandidateOfferLettersListUseCase,
    private val downloadCandidateOfferLetterUseCase: DownloadCandidateOfferLetterUseCase,
    private val offerLetterAcceptRejectUseCase: OfferLetterAcceptRejectUseCase
) : ViewModel() {
    val candidateOfferLetterResponseData = MutableLiveData<CandidateOfferLetterListResponseModel>()
    val offerLetterAcceptRejectResponseData = MutableLiveData<OfferLetterAcceptRejectResponseModel>()
    val downloadCandidateOfferLetterResponseData =
        MutableLiveData<DownloadCandidateOfferLetterResponseModel>()
    val messageData = MutableLiveData<String>()
    val showProgress = MutableLiveData<Boolean>()

    fun callCandidateOfferLetterListApi(request: CommonRequestModel) {
        showProgress.value = true
        candidateOfferLetterUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<CandidateOfferLetterListResponseModel> {
                override fun onSuccess(result: CandidateOfferLetterListResponseModel) {
                    candidateOfferLetterResponseData.value = result
                    showProgress.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                    showProgress.value = false
                }
            })
    }

    fun callOfferLetterAcceptRejectApi(request: OfferLetterAcceptRejectRequestModel) {
        showProgress.value = true
        offerLetterAcceptRejectUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<OfferLetterAcceptRejectResponseModel> {
                override fun onSuccess(result: OfferLetterAcceptRejectResponseModel) {
                    offerLetterAcceptRejectResponseData.value = result
                    showProgress.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                    showProgress.value = false
                }
            })
    }

    fun callDownloadCandidateOfferLetterApi(request: DownloadCandidateOfferLetterRequestModel) {
        showProgress.value = true
        downloadCandidateOfferLetterUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<DownloadCandidateOfferLetterResponseModel> {
                override fun onSuccess(result: DownloadCandidateOfferLetterResponseModel) {
                    downloadCandidateOfferLetterResponseData.value = result
                    showProgress.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                    showProgress.value = false
                }
            })
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}