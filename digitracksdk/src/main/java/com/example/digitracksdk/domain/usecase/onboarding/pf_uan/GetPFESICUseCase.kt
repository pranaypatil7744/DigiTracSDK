package com.example.digitracksdk.domain.usecase.onboarding.pf_uan

import com.example.digitracksdk.domain.model.onboarding.pf_uan.GetPFESICRequestModel
import com.example.digitracksdk.domain.model.onboarding.pf_uan.GetPFESICResponseModel
import com.example.digitracksdk.domain.repository.onboarding.pf_uan.PaperlessPFESICRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

/**
 * Created by Mo Khurseed Ansari on 18-10-2023.
 */
class GetPFESICUseCase
   constructor (private val repository: PaperlessPFESICRepository) :
    UseCase<GetPFESICResponseModel, Any?>() {
    override suspend fun run(params: Any?): GetPFESICResponseModel {
        return repository.callGetPFESICApi(params as GetPFESICRequestModel)
    }
}