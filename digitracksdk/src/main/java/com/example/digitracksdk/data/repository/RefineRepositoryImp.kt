package com.example.digitracksdk.data.repository

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.refine.RefineRequest
import com.example.digitracksdk.domain.model.refine.RefineResponseModel
import com.example.digitracksdk.domain.repository.refine.RefineRepository

class RefineRepositoryImp constructor(private val apiService: ApiService): RefineRepository {
    override suspend fun callRefineUrlApi(request: RefineRequest): RefineResponseModel {
        return apiService.callGetRefineApi(request)
    }
}