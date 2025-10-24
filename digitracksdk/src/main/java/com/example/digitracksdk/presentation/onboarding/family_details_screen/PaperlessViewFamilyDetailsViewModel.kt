package com.example.digitracksdk.presentation.onboarding.family_details_screen

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewFamilyDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.PaperlessFamilyDetailsModel
import com.example.digitracksdk.domain.model.onboarding.insert.PaperlessFamilyDetailsResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.onboarding.family_details.ViewFamilyDetailsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.family_details.InsertFamilyDetailsUseCase
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.utils.AppUtils

class PaperlessViewFamilyDetailsViewModel
constructor(
    private val viewFamilyDetailsUseCase: ViewFamilyDetailsUseCase,
    private val insertFamilyDetailsUseCase: InsertFamilyDetailsUseCase
) : ViewModel() {

    val viewFamilyDetailsResponseData = MutableLiveData<PaperlessViewFamilyDetailsResponseModel>()
    val insertFamilyDetailsResponseData = MutableLiveData<PaperlessFamilyDetailsResponseModel>()
    val showProgressBar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    /**
     *  for Validation
     */
    var validationListener: ValidationListener? = null

    fun validateFamilyRequestModel(request: PaperlessFamilyDetailsModel, context: Context, associateDOB:String) {
        if (request.RelationshipId == "0") {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.RELATIONSHIP_ERROR,
                R.string.please_select_the_relationship
            )
            return
        }
        if (TextUtils.isEmpty(request.FamilyMemberName)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.FAMILY_NAME_ERROR,
                R.string.please_enter_name
            )
            return
        }
        if (request.FamilyMemberName.toString().length < 2) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.FAMILY_NAME_ERROR,
                R.string.name_should_have_atleast_2_character
            )
            return
        }
        if (TextUtils.isEmpty(request.DateOfBirth)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.FAMILY_DOB_ERROR,
                R.string.please_enter_dob
            )
            return
        }
        if (request.RelationshipId == "1"){
            val selectedBirthDate = AppUtils.INSTANCE?.convertStringToDate("dd MMM yyyy",request.DateOfBirth.toString())
            val associateBirthDate = AppUtils.INSTANCE?.convertStringToDate("dd-MMM-yyyy",associateDOB)
            val isSmaller = associateBirthDate?.after(selectedBirthDate)
            if (isSmaller == false){
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.FAMILY_DOB_ERROR,
                    R.string.father_s_date_of_birth_should_be_greater_then_associate_s_dob
                )
                return
            }
        }
        if (request.RelationshipId == "2"){
            val selectedBirthDate = AppUtils.INSTANCE?.convertStringToDate("dd MMM yyyy",request.DateOfBirth.toString())
            val associateBirthDate = AppUtils.INSTANCE?.convertStringToDate("dd-MMM-yyyy",associateDOB)
            val isSmaller = associateBirthDate?.after(selectedBirthDate)
            if (isSmaller == false){
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.FAMILY_DOB_ERROR,
                    R.string.mother_s_date_of_birth_should_be_greater_then_associate_s_dob
                )
                return
            }
        }
        if (TextUtils.isEmpty(request.Occupation)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.FAMILY_OCCUPATION_ERROR,
                R.string.please_enter_occupation
            )
            return
        }
        if (request.Occupation.toString().length < 3) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.FAMILY_OCCUPATION_ERROR,
                R.string.occupation_should_have_atleast_3_character
            )
            return
        }
        if (TextUtils.isEmpty(request.IsResidingWithYou)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.IS_RESIDING_WITH_YOU_ERROR,
                R.string.please_select_the_residing
            )
            return
        }
        if (request.IsResidingWithYou == Constant.No){
            if (TextUtils.isEmpty(request.CurrentAddress)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.ADDRESS_1_ERROR,
                    R.string.please_enter_address
                )
                return
            }
        }
        if (TextUtils.isEmpty(request.IsNominee)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.NOMINEE_ERROR,
                R.string.please_select_the_nominee
            )
            return
        }
        if (request.IsNominee == Constant.Yes){
            val pf:Int = request.PF?.toIntOrNull()?:0
            val esic:Int = request.ESIC?.toIntOrNull()?:0
            val insurance = request.Insurance?.toIntOrNull()?:0
            val gratuity = request.Gratuity?.toIntOrNull()?:0
//            var total = pf + esic + insurance + gratuity
            if (TextUtils.isEmpty(request.PF)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.FAMILY_PF_ERROR,
                    R.string.please_enter_pf
                )
                return
            }
            if (pf > 100) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.FAMILY_PF_ERROR,
                    R.string.nominee_pf_total_not_allowed_more_than_100
                )
                return
            }
//            total += request.PF?.toIntOrNull()?:0
//            if (total <= 0){
//                validationListener?.onValidationFailure(
//                    Constant.ListenerConstants.INSURANCE_ERROR,
//                    R.string.total_of_pf_esic_insurance_and_gratuity_not_allow_less_than_0
//                )
//                return
//            }
//            if (total > 100){
//                validationListener?.onValidationFailure(
//                    Constant.ListenerConstants.INSURANCE_ERROR,
//                    R.string.total_of_pf_esic_insurance_and_gratuity_not_allow_more_than_100
//                )
//                return
//            }
            if (TextUtils.isEmpty(request.ESIC)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.FAMILY_ESIC_ERROR,
                    R.string.please_enter_esic
                )
                return
            }
            if (esic > 100) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.FAMILY_ESIC_ERROR,
                    R.string.nominee_esic_total_not_allowed_more_than_100
                )
                return
            }
//            total += request.ESIC?.toInt()?:0
//            if (total <= 0){
//                validationListener?.onValidationFailure(
//                    Constant.ListenerConstants.INSURANCE_ERROR,
//                    R.string.total_of_pf_esic_insurance_and_gratuity_not_allow_less_than_0
//                )
//                return
//            }
//            if (total > 100){
//                validationListener?.onValidationFailure(
//                    Constant.ListenerConstants.INSURANCE_ERROR,
//                    R.string.total_of_pf_esic_insurance_and_gratuity_not_allow_more_than_100
//                )
//                return
//            }
            if (TextUtils.isEmpty(request.Insurance)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.FAMILY_INSURANCE_ERROR,
                    R.string.please_enter_insurance
                )
                return
            }
            if (insurance > 100) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.FAMILY_INSURANCE_ERROR,
                    R.string.nominee_insurance_total_not_allowed_more_than_100
                )
                return
            }
//            total += request.Insurance?.toInt()?:0
//            if (total <= 0){
//                validationListener?.onValidationFailure(
//                    Constant.ListenerConstants.INSURANCE_ERROR,
//                    R.string.total_of_pf_esic_insurance_and_gratuity_not_allow_less_than_0
//                )
//                return
//            }
//            if (total > 100){
//                validationListener?.onValidationFailure(
//                    Constant.ListenerConstants.INSURANCE_ERROR,
//                    R.string.total_of_pf_esic_insurance_and_gratuity_not_allow_more_than_100
//                )
//                return
//            }
            if (TextUtils.isEmpty(request.Gratuity)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.FAMILY_GRATUITY_ERROR,
                    R.string.please_enter_gratuity
                )
                return
            }
            if (gratuity > 100) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.FAMILY_GRATUITY_ERROR,
                    R.string.nominee_total_not_allowed_more_than_100
                )
                return
            }
//            total += request.Gratuity?.toInt()?:0
//            if (total <= 0){
//                validationListener?.onValidationFailure(
//                    Constant.ListenerConstants.INSURANCE_ERROR,
//                    R.string.total_of_pf_esic_insurance_and_gratuity_not_allow_less_than_0
//                )
//                return
//            }
//            if (total > 100){
//                validationListener?.onValidationFailure(
//                    Constant.ListenerConstants.INSURANCE_ERROR,
//                    R.string.total_of_pf_esic_insurance_and_gratuity_not_allow_more_than_100
//                )
//                return
//            }
        }

        validationListener?.onValidationSuccess(Constant.success, R.string.success)
    }


    /**
     *  for Insert Family Details
     */

    fun callInsertFamilyDetailsApi(request: PaperlessFamilyDetailsModel) {
        showProgressBar.value = true
        insertFamilyDetailsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<PaperlessFamilyDetailsResponseModel> {
                override fun onSuccess(result: PaperlessFamilyDetailsResponseModel) {
                    showProgressBar.value = false
                    insertFamilyDetailsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }

            })
    }

    fun callViewFamilyDetailsApi(request: InnovIDRequestModel) {
        showProgressBar.value = true
        viewFamilyDetailsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<PaperlessViewFamilyDetailsResponseModel> {
                override fun onSuccess(result: PaperlessViewFamilyDetailsResponseModel) {
                    showProgressBar.value = false
                    viewFamilyDetailsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }
            })
    }
}