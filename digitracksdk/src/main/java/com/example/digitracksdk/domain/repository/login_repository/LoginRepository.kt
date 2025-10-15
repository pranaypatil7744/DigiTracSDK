package com.example.digitracksdk.domain.repository.login_repository

import com.example.digitracksdk.domain.model.login_model.CheckVersionResponseModel
import com.example.digitracksdk.domain.model.login_model.FirebaseTokenUpdateRequestModel
import com.example.digitracksdk.domain.model.login_model.FirebaseTokenUpdateResponseModel
import com.example.digitracksdk.domain.model.login_model.LoginRequestModel
import com.example.digitracksdk.domain.model.login_model.LoginResponseModel

interface LoginRepository {

    suspend fun callLoginApi(request: LoginRequestModel): LoginResponseModel

    suspend fun callFirebaseTokenUpdateApi(request: FirebaseTokenUpdateRequestModel): FirebaseTokenUpdateResponseModel

    suspend fun callCheckAppVersionApi(): CheckVersionResponseModel

}