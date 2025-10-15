package com.example.digitracksdk.domain.usecase.dashboard

import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.JobReferralRequestModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.JobReferralResponseModel
import com.example.digitracksdk.domain.repository.home_repository.JobReferralRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class JobReferralUseCase constructor(private val jobReferralRepository: JobReferralRepository) :
    UseCase<JobReferralResponseModel, Any?>() {

    override suspend fun run(params: Any?): JobReferralResponseModel {
        return jobReferralRepository.callJobReferralApi(params as JobReferralRequestModel)
    }
}