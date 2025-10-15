package com.example.digitracksdk.domain.usecase.dashboard

import com.example.digitracksdk.domain.repository.home_repository.JobReferralRepository
import com.example.digitracksdk.domain.usecase.base.UseCase
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.ReferredUsersRequestModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.ReferredUsersResponseModel

class ReferredUsersUseCase constructor(private val jobReferralRepository: JobReferralRepository) :
    UseCase<ReferredUsersResponseModel, Any?>() {

    override suspend fun run(params: Any?): ReferredUsersResponseModel {
        return jobReferralRepository.callReferredUserList(params as ReferredUsersRequestModel)
    }
}