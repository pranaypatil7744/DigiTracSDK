package com.example.digitracksdk.domain.usecase.customer_id_card_usecase

import com.example.digitracksdk.domain.model.customer_id_card.CustomerIdCardRequestModel
import com.example.digitracksdk.domain.model.customer_id_card.CustomerIdCardResponseModel
import com.example.digitracksdk.domain.repository.customer_id_card_repository.CustomerIdCardRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

/**
 * Created by Mo Khurseed Ansari on 15-Sep-2022.
 */
class CustomerIdCardUseCase
constructor(private val repository: CustomerIdCardRepository) :
    UseCase<CustomerIdCardResponseModel, Any>() {
    override suspend fun run(params: Any?): CustomerIdCardResponseModel {
        return repository.callCustomerIdCardApi(params as CustomerIdCardRequestModel)
    }
}