package com.example.digitracksdk.domain.repository.onboarding.insert

import com.example.digitracksdk.domain.model.onboarding.insert.CandidateProfileAckModel
import com.example.digitracksdk.domain.model.onboarding.insert.CandidateProfileAckResponseModel


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,15:44
 */
interface CandidateProfileAckRepository {

    suspend fun callCandidateProfileAckApi(request: CandidateProfileAckModel): CandidateProfileAckResponseModel

}

