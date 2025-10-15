package com.example.digitracksdk.domain.usecase.onboarding.aadhaar_verification_digilocker

import com.example.digitracksdk.domain.repository.onboarding.aadhaar_verification_digilocker.AadhaarVerificationDigiLockerRepository
import com.example.digitracksdk.domain.usecase.base.UseCase
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification_digilocker.GetAadhaarDetailDigiLockerRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification_digilocker.GetAadhaarDetailDigiLockerResponseModel

class AadhaarDataUseCase (val repository : AadhaarVerificationDigiLockerRepository)
    : UseCase<GetAadhaarDetailDigiLockerResponseModel, Any?>(){
    override suspend fun run(params: Any?): GetAadhaarDetailDigiLockerResponseModel {

        return repository.callGetAadhaarDataApi(params as GetAadhaarDetailDigiLockerRequestModel)

    }

}