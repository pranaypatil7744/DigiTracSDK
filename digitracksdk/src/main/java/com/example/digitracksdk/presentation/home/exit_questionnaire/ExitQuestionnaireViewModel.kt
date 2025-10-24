package com.example.digitracksdk.presentation.home.exit_questionnaire

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.exit_questionnaire_model.ExitQuestionnaireRequestModel
import com.example.digitracksdk.domain.model.exit_questionnaire_model.ExitQuestionnaireResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.exit_questionnaire_usecase.ExitQuestionnaireUseCase
import com.example.digitracksdk.listener.ValidationListener
import kotlinx.coroutines.cancel

class ExitQuestionnaireViewModel(private val exitQuestionnaireUseCase: ExitQuestionnaireUseCase) :
    ViewModel() {
    val exitQuestionnaireResponseData = MutableLiveData<ExitQuestionnaireResponseModel>()
    val showProgressBar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()
    var validationListener: ValidationListener? = null
    fun validateExitQuestionnaire(request: ExitQuestionnaireRequestModel) {

        if (TextUtils.isEmpty(request.ExitQuestionnaire1) || request.ExitQuestionnaire1 == "null") {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.EXIT_QUESTIONNAIRE_1_ERROR,
                R.string.please_select_your_choice_for_first_question
            )
            return
        }
        if (TextUtils.isEmpty(request.ExitQuestionnaire2) || request.ExitQuestionnaire2 == "null") {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.EXIT_QUESTIONNAIRE_2_ERROR,
                R.string.please_select_your_choice_for_second_question
            )
            return
        }
        if (TextUtils.isEmpty(request.ExitQuestionnaire3) || request.ExitQuestionnaire3 == "null") {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.EXIT_QUESTIONNAIRE_3_ERROR,
                R.string.please_select_your_choice_for_third_question
            )
            return
        }
        if (TextUtils.isEmpty(request.ExitQuestionnaire4) || request.ExitQuestionnaire4 == "null") {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.EXIT_QUESTIONNAIRE_4_ERROR,
                R.string.please_select_your_choice_for_fourth_question
            )
            return
        }
        if (TextUtils.isEmpty(request.ExitQuestionnaire5) || request.ExitQuestionnaire5 == "null") {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.EXIT_QUESTIONNAIRE_5_ERROR,
                R.string.please_select_your_choice_for_fifth_question
            )
            return
        }
        if (TextUtils.isEmpty(request.ExitRemark) || request.ExitRemark == "null") {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.EXIT_REMARK_ERROR,
                R.string.please_enter_exit_remark
            )
            return
        }
        validationListener?.onValidationSuccess(Constant.SUCCESS, R.string.success)
    }

    fun callExitQuestionnaireApi(request: ExitQuestionnaireRequestModel) {
        showProgressBar.value = true
        exitQuestionnaireUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ExitQuestionnaireResponseModel> {
                override fun onSuccess(result: ExitQuestionnaireResponseModel) {
                    exitQuestionnaireResponseData.value = result
                    showProgressBar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.getErrorMessage()
                    showProgressBar.value = false
                }


            }

        )

    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}