package com.example.digitracksdk.presentation.home.rewards

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.rewards.RewardRequestModel
import com.example.digitracksdk.domain.model.rewards.RewardResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.rewards_usecase.RewardsUseCase

class RewardsViewModel(
    private val rewardsUseCase: RewardsUseCase
) : ViewModel() {
    val rewardsResponseData = MutableLiveData<RewardResponseModel>()
    val showProgressBar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    fun callRewardsApi(request: RewardRequestModel) {
        showProgressBar.value = true
        rewardsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<RewardResponseModel> {
            override fun onSuccess(result: RewardResponseModel) {
                showProgressBar.value = false
                rewardsResponseData.value = result
            }

            override fun onError(apiError: ApiError?) {
                showProgressBar.value = false
                messageData.value = apiError?.message.toString()
            }
        })
    }
}