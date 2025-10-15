package com.example.digitracksdk.presentation.attendance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.model.attendance_regularization_model.InsertAttendanceRegularizationRequestModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.InsertAttendanceRegularizationResponseModel
import com.example.digitracksdk.domain.usecase.candidate_usecase.CandidateAttendanceUseCase
import kotlinx.coroutines.cancel

class CandidateViewModel constructor(
    private val candidateAttendanceUseCase: CandidateAttendanceUseCase
) : ViewModel() {

    val candidateAttendanceResponseData =
        MutableLiveData<InsertAttendanceRegularizationResponseModel>()
    val messageData = MutableLiveData<String>()
    val progressBar = MutableLiveData<Boolean>()

    fun callCandidateAttendanceApi(request: InsertAttendanceRegularizationRequestModel) {
        progressBar.value=true
        candidateAttendanceUseCase.invoke(viewModelScope,
            request,object : UseCaseResponse<InsertAttendanceRegularizationResponseModel> {

                override fun onSuccess(result: InsertAttendanceRegularizationResponseModel) {
                    result.date = request.RegularizationDate
                    progressBar.value = false
                    candidateAttendanceResponseData.value=result
                }

                override fun onError(apiError: ApiError?) {
                    progressBar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }
            }
        )
    }


    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}