package com.example.digitracksdk.domain.repository.pf_uan_repository

import com.example.digitracksdk.presentation.pf_esic_insurance.model.PfEsicRequestModel
import com.example.digitracksdk.presentation.pf_esic_insurance.model.PfEsicResponseModel

interface PfEsicInsuranceRepository {

    suspend fun callEsicCardApi(request: PfEsicRequestModel): PfEsicResponseModel

    suspend fun callEsicMedicalCardApi(request: PfEsicRequestModel): PfEsicResponseModel

    suspend fun callMedicalCardApi(request: PfEsicRequestModel): PfEsicResponseModel
}