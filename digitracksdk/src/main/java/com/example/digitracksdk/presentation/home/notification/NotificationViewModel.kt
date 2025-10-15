package com.example.digitracksdk.presentation.home.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.notification.NotificationDetailsRequestModel
import com.example.digitracksdk.domain.model.notification.NotificationDetailsResponseModel
import com.example.digitracksdk.domain.model.notification.NotificationListResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.notification_usecase.NotificationDetailsUseCase
import com.example.digitracksdk.domain.usecase.notification_usecase.NotificationListUseCase
import kotlinx.coroutines.cancel

class NotificationViewModel constructor(
    private val notificationListUseCase: NotificationListUseCase,
    private val notificationDetailsUseCase: NotificationDetailsUseCase
):ViewModel() {
    val notificationListResponseData = MutableLiveData<NotificationListResponseModel>()
    val notificationDetailsResponseData = MutableLiveData<NotificationDetailsResponseModel>()
    val messageData = MutableLiveData<String>()
    val showProgress = MutableLiveData<Boolean>()

    fun callNotificationDetailsApi(request: NotificationDetailsRequestModel){
        showProgress.value = true
        notificationDetailsUseCase.invoke(viewModelScope,request,object :
            UseCaseResponse<NotificationDetailsResponseModel> {
            override fun onSuccess(result: NotificationDetailsResponseModel) {
                notificationDetailsResponseData.value = result
                showProgress.value = false
            }

            override fun onError(apiError: ApiError?) {
                messageData.value = apiError?.message.toString()
                showProgress.value = false
            }

        })
    }

    fun callNotificationListApi(request: CommonRequestModel){
        showProgress.value = true
        notificationListUseCase.invoke(viewModelScope,request,object :
            UseCaseResponse<NotificationListResponseModel> {
            override fun onSuccess(result: NotificationListResponseModel) {
                notificationListResponseData.value = result
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