package com.example.digitracksdk.data.repository

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.JobReferralRequestModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.JobReferralResponseModel
import com.example.digitracksdk.domain.repository.home_repository.JobReferralRepository
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.ReferredUsersRequestModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.ReferredUsersResponseModel

class JobReferralRepositoryImp(var apiService: ApiService): JobReferralRepository {

    override suspend fun callJobReferralApi(request: JobReferralRequestModel): JobReferralResponseModel {
        return apiService.callJobListingApi(request)
    }

    override suspend fun callReferredUserList(request: ReferredUsersRequestModel): ReferredUsersResponseModel {
        return apiService.callReferredUserList(request)
    }

}