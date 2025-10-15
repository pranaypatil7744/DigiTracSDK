package com.example.digitracksdk.presentation.home.jobs_and_refer_fds

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.JobReferralRequestModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.JobReferralResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.dashboard.JobReferralUseCase
import com.example.digitracksdk.domain.usecase.dashboard.ReferredUsersUseCase
import com.example.digitracksdk.presentation.home.innov_id_card.InnovIDCardViewModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.ReferredUsersRequestModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.ReferredUsersResponseModel
import kotlinx.coroutines.cancel

class JobsAndReferFriendViewModel constructor(private val jobReferralUseCase: JobReferralUseCase,
                                              private val referredUsersUseCase: ReferredUsersUseCase
): ViewModel() {

    val jobReferralResponseData = MutableLiveData<JobReferralResponseModel>()
    val referredUserResponseData = MutableLiveData<ReferredUsersResponseModel>()
    val showProgressbar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    fun callJobReferralListApi(request: JobReferralRequestModel){
        showProgressbar.value = true
        jobReferralUseCase.invoke(viewModelScope,request,object :
            UseCaseResponse<JobReferralResponseModel> {
            override fun onSuccess(result: JobReferralResponseModel) {
                jobReferralResponseData.value = result
                showProgressbar.value = false
            }

            override fun onError(apiError: ApiError?) {
                messageData.value = apiError?.message.toString()
                showProgressbar.value = false
            }

        })
    }

    fun callReferredUsersList(request: ReferredUsersRequestModel){
        showProgressbar.value = true
        referredUsersUseCase.invoke(viewModelScope,request,object :
            UseCaseResponse<ReferredUsersResponseModel> {
            override fun onSuccess(result: ReferredUsersResponseModel) {
                referredUserResponseData.value = result
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

    companion object{
        private val TAG = InnovIDCardViewModel::class.java.name
    }
}