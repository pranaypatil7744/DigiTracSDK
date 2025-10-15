package com.example.digitracksdk.presentation.home.jobs_and_refer_fds.refer_a_friend

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.refer_a_friend.BranchDetailsRequestModel
import com.example.digitracksdk.domain.model.refer_a_friend.BranchDetailsResponseModel
import com.example.digitracksdk.domain.model.refer_a_friend.ReferralFriendRequestModel
import com.example.digitracksdk.domain.model.refer_a_friend.ReferralFriendResponseModel
import com.example.digitracksdk.domain.model.refer_a_friend.SkillListResponseModel
import com.innov.digitrac.domain.model.refer_a_friend.*
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.refer_friend_usecase.BranchDetailsListUseCase
import com.example.digitracksdk.domain.usecase.refer_friend_usecase.ReferralFriendUseCase
import com.example.digitracksdk.domain.usecase.refer_friend_usecase.SkillListUseCase
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.utils.AppUtils

class ReferFriendViewModel constructor(
    private val branchDetailsListUseCase: BranchDetailsListUseCase,
    private val skillListUseCase: SkillListUseCase,
    private val referralFriendUseCase: ReferralFriendUseCase
) : ViewModel() {
    val branchListResponseData = MutableLiveData<BranchDetailsResponseModel>()
    val skillListResponseData = MutableLiveData<SkillListResponseModel>()
    val referralFriendResponseData = MutableLiveData<ReferralFriendResponseModel>()
    val showProgress =  MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    var validationListener: ValidationListener? = null

    fun validateReferralFriend(request: ReferralFriendRequestModel){
        if (TextUtils.isEmpty(request.FirstName)){
            validationListener?.onValidationFailure(Constant.ListenerConstants.NAME_ERROR, R.string.please_enter_candidate_name)
            return
        }
        if (TextUtils.isEmpty(request.Gender)){
            validationListener?.onValidationFailure(Constant.ListenerConstants.GENDER_ERROR, R.string.please_select_gender)
            return
        }
        if (request.SkillId == 0){
            validationListener?.onValidationFailure(Constant.ListenerConstants.SKILL_ERROR, R.string.please_select_skill)
            return
        }
        if (TextUtils.isEmpty(request.Location)){
            validationListener?.onValidationFailure(Constant.ListenerConstants.LOCATION_ERROR, R.string.please_enter_candidate_location)
            return
        }
        if (TextUtils.isEmpty(request.MobileNo)){
            validationListener?.onValidationFailure(Constant.ListenerConstants.PHONE_ERROR, R.string.please_enter_candidate_phone)
            return
        }
        if ((request.MobileNo?.length
                ?: 0) < 10 || AppUtils.INSTANCE?.isValidMobileNumber(request.MobileNo.toString()) == false
        ){
            validationListener?.onValidationFailure(Constant.ListenerConstants.PHONE_ERROR, R.string.please_enter_valid_candidate_phone)
            return
        }
        if (!request.EmailID.isNullOrEmpty()){
            if (AppUtils.INSTANCE?.isValidEmail(request.EmailID) == false){
                validationListener?.onValidationFailure(Constant.ListenerConstants.CANDIDATE_EMAIL_ERROR, R.string.please_enter_valid_email)
                return
            }
        }
        if (request.BranchId == 0){
            validationListener?.onValidationFailure(Constant.ListenerConstants.BRANCH_ERROR, R.string.please_select_branch_name)
            return
        }
        if (!request.AdharCard.isNullOrEmpty()){
            if (AppUtils.INSTANCE?.validateAadharNumber(request.AdharCard) == false){
                validationListener?.onValidationFailure(Constant.ListenerConstants.AADHAR_NUMBER_ERROR, R.string.please_enter_valid_adhar_card_number)
                return
            }
        }
        validationListener?.onValidationSuccess(Constant.SUCCESS,R.string.success)
    }

    fun callBranchDetailsListApi(request: BranchDetailsRequestModel){
        showProgress.value = true
        branchDetailsListUseCase.invoke(viewModelScope,request,object :
            UseCaseResponse<BranchDetailsResponseModel> {
            override fun onSuccess(result: BranchDetailsResponseModel) {
                showProgress.value = false
                branchListResponseData.value = result
            }

            override fun onError(apiError: ApiError?) {
               showProgress.value = false
                messageData.value = apiError?.message.toString()
            }
        })
    }

    fun callSkillListApi(){
        showProgress.value = true
        skillListUseCase.invoke(viewModelScope,null,object :
            UseCaseResponse<SkillListResponseModel> {
            override fun onSuccess(result: SkillListResponseModel) {
                showProgress.value = false
                skillListResponseData.value = result
            }

            override fun onError(apiError: ApiError?) {
                showProgress.value = false
                messageData.value = apiError?.message.toString()
            }
        })
    }

    fun callReferralFriendApi(request: ReferralFriendRequestModel){
        showProgress.value = true
        referralFriendUseCase.invoke(viewModelScope,request,object :
            UseCaseResponse<ReferralFriendResponseModel> {
            override fun onSuccess(result: ReferralFriendResponseModel) {
                showProgress.value = false
                referralFriendResponseData.value = result
            }

            override fun onError(apiError: ApiError?) {
                showProgress.value = false
                messageData.value = apiError?.message.toString()
            }
        })
    }
}