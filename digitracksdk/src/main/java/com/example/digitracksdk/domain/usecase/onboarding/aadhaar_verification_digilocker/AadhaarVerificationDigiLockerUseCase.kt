package com.example.digitracksdk.domain.usecase.onboarding.aadhaar_verification_digilocker

import com.example.digitracksdk.domain.repository.onboarding.aadhaar_verification_digilocker.AadhaarVerificationDigiLockerRepository
import com.example.digitracksdk.domain.usecase.base.UseCase
import com.innov.digitrac.paperless.aadhaar_new.model.DigiLockerRequestModel
import com.innov.digitrac.paperless.aadhaar_new.model.DigiLockerResponseModel

class AadhaarVerificationDigiLockerUseCase (val repository: AadhaarVerificationDigiLockerRepository)
    : UseCase<DigiLockerResponseModel, Any?>()
{
    override suspend fun run(params: Any?): DigiLockerResponseModel {

        return repository.callAadhaarVerificationDigiLockerApi(params as DigiLockerRequestModel)
    }
}
