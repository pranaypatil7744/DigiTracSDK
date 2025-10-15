package com.example.digitracksdk.domain.repository.home_repository

import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.JobReferralRequestModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.JobReferralResponseModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.ReferredUsersRequestModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.ReferredUsersResponseModel

interface JobReferralRepository {
    suspend fun callJobReferralApi(request: JobReferralRequestModel): JobReferralResponseModel
    suspend fun callReferredUserList(request: ReferredUsersRequestModel): ReferredUsersResponseModel
}