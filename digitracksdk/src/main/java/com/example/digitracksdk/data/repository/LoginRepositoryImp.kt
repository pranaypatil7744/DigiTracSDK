package com.example.digitracksdk.data.repository

import com.example.digitracksdk.domain.model.login_model.CheckVersionResponseModel
import com.example.digitracksdk.domain.model.login_model.FirebaseTokenUpdateRequestModel
import com.example.digitracksdk.domain.model.login_model.FirebaseTokenUpdateResponseModel
import com.example.digitracksdk.domain.model.login_model.LoginRequestModel
import com.example.digitracksdk.domain.model.login_model.LoginResponseModel
import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.repository.login_repository.LoginRepository

class LoginRepositoryImp(private val NormalApiService: ApiService, private val digiOneApiService: ApiService):
    LoginRepository {

    override suspend fun callLoginApi(request: LoginRequestModel): LoginResponseModel {
        return NormalApiService.callLoginApi(request)
    }

    override suspend fun callFirebaseTokenUpdateApi(request: FirebaseTokenUpdateRequestModel): FirebaseTokenUpdateResponseModel {
        return NormalApiService.callFirebaseTokenUpdateApi(request)
    }

    override suspend fun callCheckAppVersionApi(): CheckVersionResponseModel {
        return digiOneApiService.callCheckAppVersionApi()
    }
}