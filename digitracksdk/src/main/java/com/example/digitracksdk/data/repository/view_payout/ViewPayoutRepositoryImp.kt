package com.example.digitracksdk.data.repository.view_payout

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.view_payout.ViewPayoutRequest
import com.example.digitracksdk.domain.model.view_payout.ViewPayoutResponseModel
import com.example.digitracksdk.domain.repository.view_payout.ViewPayoutRepository

class ViewPayoutRepositoryImp constructor(private val apiService: ApiService):
    ViewPayoutRepository {

    override suspend fun callViewPayoutApi(request: ViewPayoutRequest): ViewPayoutResponseModel {
        return apiService.callViewPayoutApi(request)
    }
}