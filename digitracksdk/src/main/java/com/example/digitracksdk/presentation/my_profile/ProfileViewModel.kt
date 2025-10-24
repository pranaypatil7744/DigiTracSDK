package com.example.digitracksdk.presentation.my_profile

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.profile_model.CheckOtpRequestModel
import com.example.digitracksdk.domain.model.profile_model.CheckOtpResponseModel
import com.example.digitracksdk.domain.model.profile_model.CityListRequestModel
import com.example.digitracksdk.domain.model.profile_model.CityListResponseModel
import com.example.digitracksdk.domain.model.profile_model.StateListResponseModel
import com.example.digitracksdk.domain.model.profile_model.ValidCandidateRequestModel
import com.example.digitracksdk.domain.model.profile_model.ValidCandidateResponseModel
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.profile_model.InsertBasicDetailsRequestModel
import com.example.digitracksdk.domain.model.profile_model.InsertBasicDetailsResponseModel
import com.example.digitracksdk.domain.model.profile_model.NewInnovIdCreationRequestModel
import com.example.digitracksdk.domain.model.profile_model.NewInnovIdCreationResponseModel
import com.example.digitracksdk.domain.model.profile_model.UpdateProfileRequestModel
import com.example.digitracksdk.domain.model.profile_model.UpdateProfileResponseModel
import com.example.digitracksdk.domain.model.profile_model.*
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.profile_usecase.CheckOtpUseCase
import com.example.digitracksdk.domain.usecase.profile_usecase.CityListUseCase
import com.example.digitracksdk.domain.usecase.profile_usecase.InsertBasicDetailsUseCase
import com.example.digitracksdk.domain.usecase.profile_usecase.NewInnovIdCreationUseCase
import com.example.digitracksdk.domain.usecase.profile_usecase.StateListUseCase
import com.example.digitracksdk.domain.usecase.profile_usecase.UpdateProfileUseCase
import com.example.digitracksdk.domain.usecase.profile_usecase.ValidCandidateUseCase
import com.example.digitracksdk.domain.usecase.profile_usecase.*
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.DialogUtils
import kotlinx.coroutines.cancel

class ProfileViewModel constructor(
    private val stateListUseCase: StateListUseCase,
    private val cityListUseCase: CityListUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val insertBasicDetailsUseCase: InsertBasicDetailsUseCase,
    private val checkOtpUseCase: CheckOtpUseCase,
    private val newInnovIdCreationUseCase: NewInnovIdCreationUseCase,
    private val validCandidateUseCase: ValidCandidateUseCase
) : ViewModel(), DialogUtils.DialogManager {

    val stateListResponseData = MutableLiveData<StateListResponseModel>()
    val cityListResponseData = MutableLiveData<CityListResponseModel>()
    val updateProfileResponseData = MutableLiveData<UpdateProfileResponseModel>()
    val checkOtpResponseData = MutableLiveData<CheckOtpResponseModel>()
    val newInnovIdCreationResponseData = MutableLiveData<NewInnovIdCreationResponseModel>()
    val insertBasicDetailsResponseData = MutableLiveData<InsertBasicDetailsResponseModel>()
    val validCandidateResponseData = MutableLiveData<ValidCandidateResponseModel>()
    val messageData = MutableLiveData<String>()
    val showProgress = MutableLiveData<Boolean>()

    var validationListener: ValidationListener? = null

    fun validateBasicDetails(request: InsertBasicDetailsRequestModel, isCheck:Boolean) {
        if (TextUtils.isEmpty(request.NameAsPerAadhar) || request.NameAsPerAadhar == "null") {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.NAME_ERROR,
                R.string.please_enter_name_as_per_aadhar
            )
            return
        }
        if (request.NameAsPerAadhar.length < 2) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.NAME_ERROR,
                R.string.name_should_have_atleast_2_character
            )
            return
        }
        if (TextUtils.isEmpty(request.AadharNo)|| request.AadharNo == "null") {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.AADHAR_NUMBER_ERROR,
                R.string.please_enter_adhar_card_number
            )
            return
        }
        if (AppUtils.INSTANCE?.validateAadharNumber(request.AadharNo) == false) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.AADHAR_NUMBER_ERROR,
                R.string.please_enter_valid_adhar_card_number
            )
            return
        }
        if (!isCheck) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.TERM_CON_ERROR,
                R.string.please_select_term_condition
            )
            return
        }
        if (TextUtils.isEmpty(request.Gender)|| request.Gender == "null") {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.GENDER_ERROR,
                R.string.please_select_gender
            )
            return
        }
        if (TextUtils.isEmpty(request.DOB)|| request.DOB == "null") {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.DOB_ERROR,
                R.string.please_select_date_of_birth
            )
            return
        }
        if (TextUtils.isEmpty(request.Mobile)|| request.Mobile == "null") {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.MOBILE_ERROR,
                R.string.please_enter_mobile_number
            )
            return
        }
        if (AppUtils.INSTANCE?.isValidMobileNumber(request.Mobile) == false){
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.MOBILE_ERROR,
                R.string.please_enter_valid_mobile_number
            )
            return
        }
        if (TextUtils.isEmpty(request.FatherName)|| request.FatherName == "null") {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.FATHER_NAME_ERROR,
                R.string.please_enter_father_name
            )
            return
        }
        if (request.FatherName.length < 2) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.FATHER_NAME_ERROR,
                R.string.father_husband_name_should_have_atleast_2_character
            )
            return
        }
        if (TextUtils.isEmpty(request.AadharDocImageArr)|| request.AadharDocImageArr == "null") {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.IMAGE_ERROR,
                R.string.please_upload_adhar_card
            )
            return
        }
        validationListener?.onValidationSuccess(Constant.SUCCESS,R.string.success)
    }

    fun validateNewInnovIdRequest(request: NewInnovIdCreationRequestModel, context: Context, isFromEdit:Boolean = false, personalMobileNo:String? = null){
        if (!isFromEdit){
            if (TextUtils.isEmpty(request.FirstName)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.NAME_ERROR,
                    R.string.please_enter_first_name
                )
                return
            }
            if (TextUtils.isEmpty(request.LastName)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.LAST_NAME_ERROR,
                    R.string.please_enter_last_name
                )
                return
            }
        }
        if (isFromEdit){
            if (TextUtils.isEmpty(request.EmergencyContactName)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.EMERGENCY_CON_NAME_ERROR,
                    R.string.please_enter_email_id
                )
                return
            }
            if (AppUtils.INSTANCE?.isValidEmail(request.EmergencyContactName) == false){
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.EMERGENCY_CON_NAME_ERROR,
                    R.string.please_enter_valid_email
                )
                return
            }
        }else{
            if (TextUtils.isEmpty(request.EmergencyContactName)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.EMERGENCY_CON_NAME_ERROR,
                    R.string.please_enter_emergency_name
                )
                return
            }
            if (AppUtils.INSTANCE?.checkSpecialSymbol(request.EmergencyContactName) == true){
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.EMERGENCY_CON_NAME_ERROR,
                    R.string.special_symbols_numbers_not_allowed_here
                )
                return
            }
        }

        if (TextUtils.isEmpty(request.EmergencyContactNo)) {
            if (isFromEdit){
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.EMERGENCY_CON_NUMBER_ERROR,
                    R.string.please_enter_mobile_number
                )
                return
            }else{
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.EMERGENCY_CON_NUMBER_ERROR,
                    R.string.please_enter_emergency_number
                )
                return
            }

        }
        if (!isFromEdit){
            if (request.EmergencyContactNo == personalMobileNo) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.EMERGENCY_CON_NUMBER_ERROR,
                    R.string.emergency_contact_number_should_not_be_same_as_personal_contact_number
                )
                return
            }
        }
        if (AppUtils.INSTANCE?.isValidMobileNumber(request.EmergencyContactNo) == false || request.EmergencyContactNo.length < 10) {
            if (isFromEdit){
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.EMERGENCY_CON_NUMBER_ERROR,
                    R.string.please_enter_valid_mobile_number
                )
                return
            }else{
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.EMERGENCY_CON_NUMBER_ERROR,
                    R.string.please_enter_valid_emergency_number
                )
                return
            }
        }


        if (request.BloodGroup == context.getString(R.string.blood_group)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.BLOOD_GROUP_ERROR,
                R.string.please_select_blood_group
            )
            return
        }
        if (!isFromEdit){
            if (TextUtils.isEmpty(request.Skill)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.SKILL_ERROR,
                    R.string.please_enter_role_for_applying
                )
                return
            }
        }

        if (TextUtils.isEmpty(request.MaritalStatus.toString()) || request.MaritalStatus == 0) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.MARITAL_STATUS_ERROR,
                R.string.please_select_marital_status
            )
            return
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
        if (TextUtils.isEmpty(request.PIN)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.PIN_ERROR,
                R.string.please_enter_pin
            )
            return
        }
        if (request.PIN.length < 6) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.PIN_ERROR,
                R.string.please_enter_valid_pin_code
            )
            return
        }

        if (request.StateID == 0) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.STATE_ERROR,
                R.string.please_select_state
            )
            return
        }
        if (request.CityID == 0) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.CITY_ERROR,
                R.string.please_select_city
            )
            return
        }
        if (TextUtils.isEmpty(request.PermanentAddress1)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.PER_ADDRESS_1_ERROR,
                R.string.please_enter_per_house_building
            )
            return
        }
        if (TextUtils.isEmpty(request.PermanentAddress2)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.PER_ADDRESS_2_ERROR,
                R.string.please_enter_per_street
            )
            return
        }
        if (TextUtils.isEmpty(request.PermanentAddress3)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.PER_ADDRESS_3_ERROR,
                R.string.please_enter_per_landmark
            )
            return
        }
        if (TextUtils.isEmpty(request.PermanentAddressPIN) || request.PermanentAddressPIN.length < 6) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.PER_PIN_ERROR,
                R.string.please_enter_per_pin
            )
            return
        }

        if (request.PermanentStateID == 0) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.PER_STATE_ERROR,
                R.string.please_select_per_state
            )
            return
        }
        if (request.PermanentCityID == 0) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.PER_CITY_ERROR,
                R.string.please_select_per_city
            )
            return
        }

        if (!isFromEdit){
            if (TextUtils.isEmpty(request.Picture)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.IMAGE_ERROR,
                    R.string.please_upload_profile_pic
                )
                return
            }
        }

        validationListener?.onValidationSuccess(Constant.SUCCESS,R.string.success)
    }


    fun callInsertBasicDetailsApi(request: InsertBasicDetailsRequestModel) {
        showProgress.value = true
        insertBasicDetailsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<InsertBasicDetailsResponseModel> {
                override fun onSuccess(result: InsertBasicDetailsResponseModel) {
                    insertBasicDetailsResponseData.value = result
                    showProgress.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                    showProgress.value = false
                }
            })
    }


    fun callValidCandidateApi(request: ValidCandidateRequestModel) {
        showProgress.value = true
        validCandidateUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ValidCandidateResponseModel> {
                override fun onSuccess(result: ValidCandidateResponseModel) {
                    validCandidateResponseData.value = result
                    showProgress.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                    showProgress.value = false
                }
            })
    }

    fun callNewInnovIdCreationApi(request: NewInnovIdCreationRequestModel) {
        newInnovIdCreationUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<NewInnovIdCreationResponseModel> {
                override fun onSuccess(result: NewInnovIdCreationResponseModel) {
                    newInnovIdCreationResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callCheckOtpApi(request: CheckOtpRequestModel) {
        checkOtpUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<CheckOtpResponseModel> {
                override fun onSuccess(result: CheckOtpResponseModel) {
                    checkOtpResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callUpdateProfileApi(request: UpdateProfileRequestModel) {
        updateProfileUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<UpdateProfileResponseModel> {
                override fun onSuccess(result: UpdateProfileResponseModel) {
                    updateProfileResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callStateListApi(request: CommonRequestModel) {
        stateListUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<StateListResponseModel> {
                override fun onSuccess(result: StateListResponseModel) {
                    stateListResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callCityListApi(request: CityListRequestModel) {
        cityListUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<CityListResponseModel> {
                override fun onSuccess(result: CityListResponseModel) {
                    cityListResponseData.value = result
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