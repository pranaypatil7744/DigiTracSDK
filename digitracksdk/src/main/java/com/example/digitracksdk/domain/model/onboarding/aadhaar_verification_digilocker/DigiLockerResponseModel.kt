package com.innov.digitrac.paperless.aadhaar_new.model


import androidx.annotation.Keep

@Keep
data class DigiLockerResponseModel(
    var expires_at: String? = "",
    var metadata: String? ="",
    var request_id: String? = "",
    var request_timestamp: String? = "",
    var response_code: String?="",
    var response_message: String?="",
    var sdk_url: String? ="",
    var success: Boolean?= false,
    var webhook_security_key: String? = ""
)



@Keep
data class DigiLockerRequestModel(
    var docs: List<String>?= arrayListOf(),
    var fast_track: String ? ="",
    var pinless: Boolean?= false,
    var purpose: String?="",
    var redirect_url: String?="",
    var response_url: String?=""
)