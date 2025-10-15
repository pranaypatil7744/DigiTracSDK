package com.example.digitracksdk.presentation.onboarding.bank_details

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.ChequeValidationResponseModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.GetBankListResponseModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.PaperlessViewBankDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.UpdateBankDetailsRequestModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.UpdateBankDetailsResponseModel
import com.innov.digitrac.domain.model.onboarding.bank_model.*
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.onboarding.bank_usecase.ChequeValidationUseCase
import com.example.digitracksdk.domain.usecase.onboarding.bank_usecase.GetBankListUseCase
import com.example.digitracksdk.domain.usecase.onboarding.bank_usecase.PaperlessViewBankDetailsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.bank_usecase.UpdateBankDetailsUseCase
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.utils.AppUtils
import kotlinx.coroutines.cancel

class BankDetailsViewModel
constructor(
    private val viewBankDetailsUseCase: PaperlessViewBankDetailsUseCase,
    private val getBankDetailsUseCase: GetBankListUseCase,
    private val chequeValidationUseCase: ChequeValidationUseCase,
    private val updateBankDetailsUseCase: UpdateBankDetailsUseCase

) : ViewModel() {

    val viewBankDetailsResponseData = MutableLiveData<PaperlessViewBankDetailsResponseModel>()
    val getBankListResponseData = MutableLiveData<GetBankListResponseModel>()
    val chequeValidationResponseData = MutableLiveData<ChequeValidationResponseModel>()
    val updateBankDetailsResponseData = MutableLiveData<UpdateBankDetailsResponseModel>()
    val showProgressBar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    var validationListener: ValidationListener? = null

    fun validateBankDetails(request: UpdateBankDetailsRequestModel, isChequeUpload:Boolean){
        if (TextUtils.isEmpty(request.PANNumber)){
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.PAN_CARD_ERROR,
                R.string.please_enter_pan_number
            )
            return
        }
        if (AppUtils.INSTANCE?.isValidPanCard(request.PANNumber.toString()) == false){
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.PAN_CARD_ERROR,
                R.string.please_enter_valid_pan_number
            )
            return
        }
        if (request.BankName == "--Select--") {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.BANK_NAME_ERROR,
                R.string.please_choose_bank_name
            )
            return
        }
        if (TextUtils.isEmpty(request.AccountNumber)){
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.ACCOUNT_NUMBER_ERROR,
                R.string.please_enter_acc_number
            )
            return
        }
        if (AppUtils.INSTANCE?.isValidAccountNumber(request.AccountNumber.toString()) == false){
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.ACCOUNT_NUMBER_ERROR,
                R.string.please_enter_valid_acc_number
            )
            return
        }
        if (TextUtils.isEmpty(request.IFSCcode)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.IFSC_ERROR,
                R.string.please_enter_ifsc_code
            )
            return
        }
        if (AppUtils.INSTANCE?.isValidIFSCCode(request.IFSCcode.toString()) == false) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.IFSC_ERROR,
                R.string.please_enter_valid_ifsc_code
            )
            return
        }
        if (!isChequeUpload){
            if (TextUtils.isEmpty(request.ChequeDocImageArr)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.CHEQUE_ERROR,
                    R.string.please_upload_cheque
                )
                return
            }
        }
        validationListener?.onValidationSuccess(Constant.success, R.string.success)
    }

    fun callUpdateBankDetailsApi(request: UpdateBankDetailsRequestModel) {
        showProgressBar.value = true
        updateBankDetailsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<UpdateBankDetailsResponseModel> {
                override fun onSuccess(result: UpdateBankDetailsResponseModel) {
                    updateBankDetailsResponseData.value = result
                    showProgressBar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                    showProgressBar.value = false
                }
            })
    }

    fun callViewBankApi(request: InnovIDRequestModel) {
        showProgressBar.value = true
        viewBankDetailsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<PaperlessViewBankDetailsResponseModel> {
                override fun onSuccess(result: PaperlessViewBankDetailsResponseModel) {
                    viewBankDetailsResponseData.value = result
                    showProgressBar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                    showProgressBar.value = false
                }
            })
    }

    fun callBankListApi() {
        showProgressBar.value = true
        getBankDetailsUseCase.invoke(
            viewModelScope,
            null,
            object : UseCaseResponse<GetBankListResponseModel> {
                override fun onSuccess(result: GetBankListResponseModel) {
                    getBankListResponseData.value = result
                    showProgressBar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                    showProgressBar.value = false
                }
            })
    }

    fun callChequeValidationApi(request: CommonRequestModel) {
        chequeValidationUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ChequeValidationResponseModel> {
                override fun onSuccess(result: ChequeValidationResponseModel) {
                    chequeValidationResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}