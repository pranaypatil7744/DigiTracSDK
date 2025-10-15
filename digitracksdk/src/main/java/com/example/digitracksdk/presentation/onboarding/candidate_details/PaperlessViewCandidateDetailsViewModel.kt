package com.example.digitracksdk.presentation.onboarding.candidate_details

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewCandidateDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.PaperlessUpdateCandidateBasicDetailsRequestModel
import com.example.digitracksdk.domain.model.onboarding.insert.PaperlessUpdateCandidateBasicDetailsResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.onboarding.PaperlessViewCandidateDetailsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.insert.PaperlessUpdateCandidateBasicDetailsUseCase
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.utils.AppUtils

class PaperlessViewCandidateDetailsViewModel
constructor(
    private val viewCandidateDetailsUseCase: PaperlessViewCandidateDetailsUseCase,
    private val updateCandidateBasicDetailsUseCase: PaperlessUpdateCandidateBasicDetailsUseCase,
    ) : ViewModel() {
    val updateCandidateBasicDetailsResponseData =
        MutableLiveData<PaperlessUpdateCandidateBasicDetailsResponseModel>()
    val viewCandidateDetailsResponseData = MutableLiveData<PaperlessViewCandidateDetailsResponseModel>()
    val showProgressBar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    var validationListener: ValidationListener? = null

    fun callViewCandidateDetailsApi(request: InnovIDRequestModel) {
        showProgressBar.value = true
        viewCandidateDetailsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<PaperlessViewCandidateDetailsResponseModel> {
                override fun onSuccess(result: PaperlessViewCandidateDetailsResponseModel) {
                    showProgressBar.value = false
                    viewCandidateDetailsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callUpdateCandidateBasicDetailsApi(request: PaperlessUpdateCandidateBasicDetailsRequestModel) {
        showProgressBar.value = true
        updateCandidateBasicDetailsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<PaperlessUpdateCandidateBasicDetailsResponseModel> {
                override fun onSuccess(result: PaperlessUpdateCandidateBasicDetailsResponseModel) {
                    showProgressBar.value = false
                    updateCandidateBasicDetailsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }

            })
    }

    fun validateCandidateDetailsModel(request: PaperlessUpdateCandidateBasicDetailsRequestModel, personalMobileNo:String) {

        if (TextUtils.isEmpty(request.AadharNo)|| request.AadharNo == "null") {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.CANDIDATE_AADHAAR_ERROR,
                R.string.please_enter_adhar_card_number
            )
            return
        }
        if (AppUtils.INSTANCE?.validateAadharNumber(request.AadharNo) == false) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.CANDIDATE_AADHAAR_ERROR,
                R.string.please_enter_valid_adhar_card_number
            )
            return
        }

        if (TextUtils.isEmpty(request.EmergencyContactName)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.CANDIDATE_EMERGENCY_CONTACT_NAME_ERROR,
                R.string.please_enter_emergency_contact_name
            )
            return
        }

        if (request.EmergencyContactName.toString().length < 2) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.CANDIDATE_EMERGENCY_CONTACT_NAME_ERROR,
                R.string.emergency_contact_name_should_have_atleast_2_character
            )
            return
        }

        if (TextUtils.isEmpty(request.EmergencyContactNo)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.CANDIDATE_EMERGENCY_CONTACT_NO_ERROR,
                R.string.please_enter_emergency_contact_no
            )
            return
        }

        if (AppUtils.INSTANCE?.isValidMobileNumber(request.EmergencyContactNo.toString()) == false){
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.CANDIDATE_EMERGENCY_CONTACT_NO_ERROR,
                R.string.please_enter_valid_emergency_number
            )
            return
        }

        if (request.EmergencyContactNo == personalMobileNo) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.CANDIDATE_EMERGENCY_CONTACT_NO_ERROR,
                R.string.emergency_contact_number_should_not_be_same_as_personal_contact_number
            )
            return
        }

        if (TextUtils.isEmpty(request.EmailId)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.CANDIDATE_EMAIL_ERROR,
                R.string.please_enter_email_id
            )
            return
        }
        if (AppUtils.INSTANCE?.isValidEmail(request.EmailId) == false){
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.CANDIDATE_EMAIL_ERROR,
                R.string.please_enter_valid_email
            )
            return
        }
        if (TextUtils.isEmpty(request.BloodGroup)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.CANDIDATE_BLOOD_GROUP_ERROR,
                R.string.please_select_blood_group
            )
            return
        }
        if (request.MartialStatusID == "0") {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.MARITAL_STATUS_ERROR,
                R.string.please_select_marital_status
            )
            return
        }
        if (TextUtils.isEmpty(request.NoOfChildren)) {
//            if (request.MartialStatusID != "1"){
//                validationListener?.onValidationFailure(
//                    Constant.ListenerConstants.CANDIDATE_NO_OF_CHILDREN_ERROR,
//                    R.string.please_enter_no_of_children
//                )
//                return
//            }
        }
        if (TextUtils.isEmpty(request.Address1)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.ADDRESS_1_ERROR,
                R.string.please_enter_house_building
            )
            return
        }
        if (TextUtils.isEmpty(request.Address2)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.ADDRESS_2_ERROR,
                R.string.please_enter_street
            )
            return
        }
        if (TextUtils.isEmpty(request.Address3)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.ADDRESS_3_ERROR,
                R.string.please_enter_landmark
            )
            return
        }
        if (TextUtils.isEmpty(request.Location)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.LOCATION_ERROR,
                R.string.please_enter_location
            )
            return
        }
        if (TextUtils.isEmpty(request.Pincode)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.PIN_ERROR,
                R.string.please_enter_pin
            )
            return
        }

        if(TextUtils.isEmpty(request.StateName))
        {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.STATE_ERROR,
                R.string.please_select_state
            )

            return
        }

        validationListener?.onValidationSuccess(Constant.success, R.string.success)
    }
}