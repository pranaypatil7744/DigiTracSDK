package com.example.digitracksdk.domain.repository.onboarding.pf_uan

import com.example.digitracksdk.domain.model.onboarding.pf_uan.GetPFESICRequestModel
import com.example.digitracksdk.domain.model.onboarding.pf_uan.GetPFESICResponseModel
import com.example.digitracksdk.domain.model.onboarding.pf_uan.InsertPFESICRequestModel
import com.example.digitracksdk.domain.model.onboarding.pf_uan.InsertPFESICResponseModel


/**
 * Created by Mo Khurseed Ansari on 18-10-2023.
 */
interface PaperlessPFESICRepository {
    suspend fun callInsertPFESICApi(request : InsertPFESICRequestModel) : InsertPFESICResponseModel
    suspend fun callGetPFESICApi(request : GetPFESICRequestModel) : GetPFESICResponseModel
}