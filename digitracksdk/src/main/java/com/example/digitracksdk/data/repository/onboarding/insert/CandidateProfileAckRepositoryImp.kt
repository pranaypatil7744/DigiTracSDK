package com.example.digitracksdk.data.repository.onboarding.insert

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.onboarding.insert.CandidateProfileAckModel
import com.example.digitracksdk.domain.model.onboarding.insert.CandidateProfileAckResponseModel
import com.example.digitracksdk.domain.repository.onboarding.insert.CandidateProfileAckRepository


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:59
 */
class CandidateProfileAckRepositoryImp
    (private val apiService: ApiService) : CandidateProfileAckRepository {

    override suspend fun callCandidateProfileAckApi(request: CandidateProfileAckModel): CandidateProfileAckResponseModel {
        return apiService.callCandidateProfileAckApi(request)
    }
}