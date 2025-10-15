package com.example.digitracksdk.data.repository.onboarding

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.repository.onboarding.aadhaar_verification_digilocker.AadhaarVerificationDigiLockerRepository
import com.innov.digitrac.paperless.aadhaar_new.model.DigiLockerRequestModel
import com.innov.digitrac.paperless.aadhaar_new.model.DigiLockerResponseModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification_digilocker.GetAadhaarDetailDigiLockerRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification_digilocker.GetAadhaarDetailDigiLockerResponseModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification_digilocker.SaveDigiLockerRequestIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification_digilocker.SaveDigiLockerRequestIDResponseModel

class AadhaarVerificationDigiLockerRepositoryImp(

    private val apiServiceAssociate: ApiService,
    private val apiServiceNormal: ApiService


) :
    AadhaarVerificationDigiLockerRepository {
    override suspend fun callSaveRequestIDApi(request: SaveDigiLockerRequestIDRequestModel): SaveDigiLockerRequestIDResponseModel {

        return apiServiceNormal.callAadhaarSaveRequestIdApi(request)
    }

    override suspend fun callGetAadhaarDataApi(request: GetAadhaarDetailDigiLockerRequestModel): GetAadhaarDetailDigiLockerResponseModel {

        return apiServiceNormal.callGetAadhaarDataApi(request)
    }

    override suspend fun callAadhaarVerificationDigiLockerApi(request: DigiLockerRequestModel): DigiLockerResponseModel {

        return apiServiceAssociate.callAadhaarVerificationApi(request)
    }


}