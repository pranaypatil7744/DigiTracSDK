package com.example.digitracksdk.presentation.home.training

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.training_model.TrainingRequestModel
import com.example.digitracksdk.domain.model.training_model.TrainingResponseModel
import com.example.digitracksdk.domain.model.training_model.ViewTrainingDocumentRequestModel
import com.example.digitracksdk.domain.model.training_model.ViewTrainingDocumentResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.training_usecase.TrainingUseCase
import com.example.digitracksdk.domain.usecase.training_usecase.ViewTrainingDocumentUseCase
import kotlinx.coroutines.cancel

class TrainingViewModel constructor(private val trainingUseCase: TrainingUseCase,
                                    private val viewTrainingDocumentUseCase: ViewTrainingDocumentUseCase
):ViewModel() {
    val trainingResponseData = MutableLiveData<TrainingResponseModel>()
    val viewTrainingDocumentResponseData = MutableLiveData<ViewTrainingDocumentResponseModel>()
    val trainingMessage = MutableLiveData<String>()
    val showProgressBar = MutableLiveData<Boolean>()

    fun callViewTrainingDocumentApi(request: ViewTrainingDocumentRequestModel){
        showProgressBar.value = true
        viewTrainingDocumentUseCase.invoke(viewModelScope,request,object :
            UseCaseResponse<ViewTrainingDocumentResponseModel> {
            override fun onSuccess(result: ViewTrainingDocumentResponseModel) {
                showProgressBar.value = false
                viewTrainingDocumentResponseData.value = result
            }

            override fun onError(apiError: ApiError?) {
                showProgressBar.value = false
                trainingMessage.value = apiError?.message.toString()
            }

        })
    }

    fun callTrainingApi(request: TrainingRequestModel){
        showProgressBar.value = true
        trainingUseCase.invoke(viewModelScope,request,object :
            UseCaseResponse<TrainingResponseModel> {
            override fun onSuccess(result: TrainingResponseModel) {
                showProgressBar.value = false
                trainingResponseData.value = result
            }

            override fun onError(apiError: ApiError?) {
                showProgressBar.value = false
                trainingMessage.value = apiError?.message.toString()
            }

        })
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}