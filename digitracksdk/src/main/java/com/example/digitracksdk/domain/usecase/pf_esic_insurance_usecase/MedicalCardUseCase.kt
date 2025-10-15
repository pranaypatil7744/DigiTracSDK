package com.example.digitracksdk.domain.usecase.pf_esic_insurance_usecase

import com.example.digitracksdk.domain.repository.pf_uan_repository.PfEsicInsuranceRepository
import com.example.digitracksdk.domain.usecase.base.UseCase
import com.example.digitracksdk.presentation.pf_esic_insurance.model.PfEsicRequestModel
import com.example.digitracksdk.presentation.pf_esic_insurance.model.PfEsicResponseModel

class MedicalCardUseCase constructor(private var pfEsicInsuranceRepository: PfEsicInsuranceRepository) :
    UseCase<PfEsicResponseModel, Any?>() {
    override suspend fun run(params: Any?): PfEsicResponseModel {
        return pfEsicInsuranceRepository.callMedicalCardApi(params as PfEsicRequestModel)
    }
}