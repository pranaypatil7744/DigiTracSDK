package com.example.digitracksdk.presentation.onboarding.bank_account_verification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.onboarding.bank_account_verification.BankAccountVerificationDetailsRequestModel
import com.example.digitracksdk.domain.model.onboarding.bank_account_verification.BankAccountVerificationDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.bank_account_verification.BankAccountVerificationRequestModel
import com.example.digitracksdk.domain.model.onboarding.bank_account_verification.BankAccountVerificationResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.onboarding.bank_account_verification.BankAccountVerificationDetailsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.bank_account_verification.BankAccountVerificationUseCase
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.utils.AppUtils

class BankAccountVerificationViewModel
constructor(
    private val bankAccountVerificationUseCase: BankAccountVerificationUseCase,
    private val bankAccountVerificationDetailsUseCase: BankAccountVerificationDetailsUseCase
) :
    ViewModel() {
    var listener: ValidationListener? = null
    val bankAccountVerificationResponseData =
        MutableLiveData<BankAccountVerificationResponseModel>()
    val bankAccountVerificationDetailsResponseData =
        MutableLiveData<BankAccountVerificationDetailsResponseModel>()
    val showProgressBar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()


    fun verifyBankDetailsDetails(request: BankAccountVerificationRequestModel) {

        if (AppUtils.INSTANCE?.isValidAccountNumber(request.Account) == false) {
            listener?.onValidationFailure(
                Constant.ListenerConstants.ACCOUNT_NUMBER_ERROR,
                R.string.please_enter_valid_acc_number
            )
            return
        }
        if (AppUtils.INSTANCE?.isValidIFSCCode(request.IFSC) == false) {
            listener?.onValidationFailure(
                Constant.ListenerConstants.IFSC_ERROR,
                R.string.please_enter_valid_ifsc_code
            )
            return
        }

        listener?.onValidationSuccess(Constant.SUCCESS, R.string.success)
    }


    fun callBankAccountVerificationApi(request: BankAccountVerificationRequestModel) {
        showProgressBar.value = true
        bankAccountVerificationUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<BankAccountVerificationResponseModel> {
                override fun onSuccess(result: BankAccountVerificationResponseModel) {
                    showProgressBar.value = false
                    bankAccountVerificationResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callBankAccountVerificationDetailsApi(request: BankAccountVerificationDetailsRequestModel) {
        showProgressBar.value = true
        bankAccountVerificationDetailsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<BankAccountVerificationDetailsResponseModel> {
                override fun onSuccess(result: BankAccountVerificationDetailsResponseModel) {
                    showProgressBar.value = false
                    bankAccountVerificationDetailsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            }
        )
    }

}