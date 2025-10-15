package com.example.digitracksdk.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.customer_id_card.CustomerIdCardRequestModel
import com.example.digitracksdk.domain.model.customer_id_card.CustomerIdCardResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.customer_id_card_usecase.CustomerIdCardUseCase

/**
 * Created by Mo Khurseed Ansari on 15-Sep-2022.
 */
class CustomerIdCardViewModel
constructor(
    private val customerIdCardUseCase: CustomerIdCardUseCase
) : ViewModel() {
    val customerIdCardResponseData = MutableLiveData<CustomerIdCardResponseModel>()
    var messageData = MutableLiveData<String>()

    fun callCustomerIdCardApi(request: CustomerIdCardRequestModel) {
        customerIdCardUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<CustomerIdCardResponseModel> {
                override fun onSuccess(result: CustomerIdCardResponseModel) {
                    customerIdCardResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }

            }
        )
    }
}