package com.example.digitracksdk.domain.usecase.onboarding.aadhaar_verification_digilocker

import com.example.digitracksdk.domain.repository.onboarding.aadhaar_verification_digilocker.AadhaarVerificationDigiLockerRepository
import com.example.digitracksdk.domain.usecase.base.UseCase
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification_digilocker.SaveDigiLockerRequestIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification_digilocker.SaveDigiLockerRequestIDResponseModel

class AadhaarSaveRequestIdUseCase(val repository : AadhaarVerificationDigiLockerRepository)
    : UseCase<SaveDigiLockerRequestIDResponseModel, Any?>() {
    override suspend fun run(params: Any?): SaveDigiLockerRequestIDResponseModel {

        return repository.callSaveRequestIDApi(params as SaveDigiLockerRequestIDRequestModel)
    }
}