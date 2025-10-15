package com.example.digitracksdk.domain.usecase.onboarding.pf_uan

import com.example.digitracksdk.domain.model.onboarding.pf_uan.InsertPFESICRequestModel
import com.example.digitracksdk.domain.model.onboarding.pf_uan.InsertPFESICResponseModel
import com.example.digitracksdk.domain.repository.onboarding.pf_uan.PaperlessPFESICRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

/**
 * Created by Mo Khurseed Ansari on 18-10-2023.
 */
class InsertPFESICUseCase
  constructor (private val repository: PaperlessPFESICRepository) :
    UseCase<InsertPFESICResponseModel, Any>() {
    override suspend fun run(params: Any?): InsertPFESICResponseModel {
        return repository.callInsertPFESICApi(params as InsertPFESICRequestModel)
    }
}