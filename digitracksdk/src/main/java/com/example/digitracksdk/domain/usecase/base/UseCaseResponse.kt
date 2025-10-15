package com.example.digitracksdk.domain.usecase.base

import com.example.digitracksdk.domain.model.ApiError

interface UseCaseResponse<Type> {

    fun onSuccess(result:Type)

    fun onError(apiError: ApiError?)
}