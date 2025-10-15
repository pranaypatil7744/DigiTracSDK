package com.example.digitracksdk.data.repository.pf_esic_insurance_repo_imp

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.repository.pf_uan_repository.PfEsicInsuranceRepository
import com.example.digitracksdk.presentation.pf_esic_insurance.model.PfEsicRequestModel
import com.example.digitracksdk.presentation.pf_esic_insurance.model.PfEsicResponseModel

class PfEsicInsuranceRepositoryImp(var apiService: ApiService): PfEsicInsuranceRepository {
    override suspend fun callEsicCardApi(request: PfEsicRequestModel): PfEsicResponseModel {
        return apiService.callEsicCardApi(request)
    }

    override suspend fun callEsicMedicalCardApi(request: PfEsicRequestModel): PfEsicResponseModel {
        return apiService.callEsicMedicalCardApi(request)
    }

    override suspend fun callMedicalCardApi(request: PfEsicRequestModel): PfEsicResponseModel {
       return apiService.callMedicalCardApi(request)
    }
}