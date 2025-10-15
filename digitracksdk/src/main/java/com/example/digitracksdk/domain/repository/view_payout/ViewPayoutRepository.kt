package com.example.digitracksdk.domain.repository.view_payout

import com.example.digitracksdk.domain.model.view_payout.ViewPayoutRequest
import com.example.digitracksdk.domain.model.view_payout.ViewPayoutResponseModel

interface ViewPayoutRepository {
    suspend fun callViewPayoutApi(request: ViewPayoutRequest): ViewPayoutResponseModel
}