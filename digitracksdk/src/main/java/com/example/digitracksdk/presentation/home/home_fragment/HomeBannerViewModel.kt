package com.example.digitracksdk.presentation.home.home_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.innov.digitrac.domain.model.home_model.HomeBannerRequestModel
import com.innov.digitrac.domain.model.home_model.HomeBannerResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.home_usecase.HomeBannerUseCase
import kotlinx.coroutines.cancel

class HomeBannerViewModel constructor(private val homeBannerUseCase: HomeBannerUseCase):ViewModel() {
    val responseData = MutableLiveData<HomeBannerResponseModel>()
    val showProgressbar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    fun callHomeBannerApi(request: HomeBannerRequestModel){
        showProgressbar.value = true
        homeBannerUseCase.invoke(viewModelScope,request,object :
            UseCaseResponse<HomeBannerResponseModel> {
            override fun onSuccess(result: HomeBannerResponseModel) {
                responseData.value = result
                showProgressbar.value = false
            }

            override fun onError(apiError: ApiError?) {
                messageData.value = apiError?.message.toString()
                showProgressbar.value = false
            }

        })
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}