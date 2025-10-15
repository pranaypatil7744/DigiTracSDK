package com.example.digitracksdk.data.source.remote

import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingUpdateRequestModel
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingUpdateResponseModel
import com.example.digitracksdk.domain.model.login_model.FirebaseTokenUpdateRequestModel
import com.example.digitracksdk.domain.model.login_model.FirebaseTokenUpdateResponseModel
import io.reactivex.rxjava3.core.Flowable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitApi {

    @POST(ApiNames.FirebaseTokenUpdateApi)
    fun callFirebaseTokenUpdateApi(@Body request: FirebaseTokenUpdateRequestModel): Call<FirebaseTokenUpdateResponseModel>?

    @POST(ApiNames.GeoTrackingUpdateApi)
    fun callGeoTrackingUpdateApi(@Body data: GeoTrackingUpdateRequestModel?): Flowable<GeoTrackingUpdateResponseModel>?

}