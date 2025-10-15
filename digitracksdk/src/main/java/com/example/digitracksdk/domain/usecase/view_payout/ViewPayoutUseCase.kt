package com.example.digitracksdk.domain.usecase.view_payout

import com.example.digitracksdk.domain.model.view_payout.ViewPayoutRequest
import com.example.digitracksdk.domain.model.view_payout.ViewPayoutResponseModel
import com.example.digitracksdk.domain.repository.view_payout.ViewPayoutRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ViewPayoutUseCase constructor(private val viewPayoutRepository: ViewPayoutRepository):
    UseCase<ViewPayoutResponseModel, Any?>() {
    override suspend fun run(params: Any?): ViewPayoutResponseModel {
        return viewPayoutRepository.callViewPayoutApi(params as ViewPayoutRequest)
    }
}