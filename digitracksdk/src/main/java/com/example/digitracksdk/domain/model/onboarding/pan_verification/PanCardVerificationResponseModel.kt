package com.example.digitracksdk.domain.model.onboarding.pan_verification

import androidx.annotation.Keep

@Keep
data class PanCardVerificationResponseModel(
    val Message: String?="",
//    val Status: String?="",
//    val group_id: String?="",
//    val metadata: Metadata?= Metadata(),
//    val request_id: String?="",
//    val request_timestamp: String?="",
//    val response_code: String?="",
    val response_message: String?="",
//    val response_timestamp: String?="",
    val result: Result?= Result(),
    val success: Boolean?=false
//    val task_id: String?=""
)

@Keep
data class Metadata(
    val billable: String?="",
    val reason_message: String?=""
)

@Keep
data class Result(
    val aadhaar_linked_status: Boolean?=false,
    val masked_aadhaar: String?="",
    val pan_number: String?="",
    val pan_type: String?="",
    val user_address: UserAddress?= UserAddress(),
    val user_dob: String?="",
    val user_email: String?="",
    val user_full_name: String?="",
    val user_full_name_split: ArrayList<String>? = ArrayList(),
    val user_gender: String?="",
    val user_phone_number: String?=""
)
@Keep
data class UserAddress(
    val city: String?="",
    val country: String?="",
    val full: String?="",
    val line_1: String?="",
    val line_2: String?="",
    val state: String?="",
    val street_name: String?="",
    val zip: String?=""
)