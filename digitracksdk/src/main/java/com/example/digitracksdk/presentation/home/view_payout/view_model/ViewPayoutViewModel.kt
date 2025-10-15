package com.example.digitracksdk.presentation.home.view_payout.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.view_payout.ViewPayoutRequest
import com.example.digitracksdk.domain.model.view_payout.ViewPayoutResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.view_payout.ViewPayoutUseCase
import com.example.digitracksdk.listener.ValidationListener
import kotlinx.coroutines.cancel

class ViewPayoutViewModel constructor(
    private val viewPayoutUseCase: ViewPayoutUseCase
) : ViewModel() {

    val viewPayoutResponseData = MutableLiveData<ViewPayoutResponseModel>()
    var messageData = MutableLiveData<String>()
    val showProgressbar = MutableLiveData<Boolean>()

    var validationListener: ValidationListener? = null

    fun callViewPayoutApi(request: ViewPayoutRequest) {
        showProgressbar.value = true
        viewPayoutUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ViewPayoutResponseModel> {
                override fun onSuccess(result: ViewPayoutResponseModel) {
                    showProgressbar.value = false
                    viewPayoutResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}