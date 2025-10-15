package com.example.digitracksdk.data.repository

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.customer_id_card.CustomerIdCardRequestModel
import com.example.digitracksdk.domain.model.customer_id_card.CustomerIdCardResponseModel
import com.example.digitracksdk.domain.repository.customer_id_card_repository.CustomerIdCardRepository

/**
 * Created by Mo Khurseed Ansari on 15-Sep-2022.
 */
class CustomerIdCardUseRepositoryImp
    (var apiService: ApiService) : CustomerIdCardRepository
{
    override suspend fun callCustomerIdCardApi(request: CustomerIdCardRequestModel): CustomerIdCardResponseModel {
        return apiService.callCustomerIdCardApi(request)
    }
}