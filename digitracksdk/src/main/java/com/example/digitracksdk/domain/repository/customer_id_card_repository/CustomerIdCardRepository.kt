package com.example.digitracksdk.domain.repository.customer_id_card_repository

import com.example.digitracksdk.domain.model.customer_id_card.CustomerIdCardRequestModel
import com.example.digitracksdk.domain.model.customer_id_card.CustomerIdCardResponseModel

/**
 * Created by Mo Khurseed Ansari on 15-Sep-2022.
 */
interface CustomerIdCardRepository {
    suspend fun callCustomerIdCardApi (request : CustomerIdCardRequestModel) : CustomerIdCardResponseModel
}