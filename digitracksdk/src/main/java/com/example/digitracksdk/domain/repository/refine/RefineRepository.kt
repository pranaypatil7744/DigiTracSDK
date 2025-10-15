package com.example.digitracksdk.domain.repository.refine

import com.example.digitracksdk.domain.model.refine.RefineRequest
import com.example.digitracksdk.domain.model.refine.RefineResponseModel

interface RefineRepository {

    suspend fun callRefineUrlApi(request: RefineRequest): RefineResponseModel
}