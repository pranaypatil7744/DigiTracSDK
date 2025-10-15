package com.example.digitracksdk.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.refine.RefineRequest
import com.example.digitracksdk.domain.model.refine.RefineResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.refine.RefineUseCase

class RefineViewModel constructor(private val refineUseCase: RefineUseCase) : ViewModel() {

    val refineResponseData = MutableLiveData<RefineResponseModel>()
    var messageData = MutableLiveData<String>()

    fun callRefineUrlApi(request: RefineRequest) {
        refineUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<RefineResponseModel> {
                override fun onSuccess(result: RefineResponseModel) {
                    refineResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }
}