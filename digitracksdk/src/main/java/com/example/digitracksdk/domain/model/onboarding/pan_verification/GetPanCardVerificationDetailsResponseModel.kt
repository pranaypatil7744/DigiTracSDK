package com.example.digitracksdk.domain.model.onboarding.pan_verification

import androidx.annotation.Keep

/**
 * Created by Mo Khurseed Ansari on 03-05-2023.
 */


@Keep
data class GetPanCardVerificationDetailsResponseModel(
  val InnovId : String ? = "",
  val Pan_number  : String ? = "",
  val user_full_name : String ? = "",
  val status  : String ? = "",
  val Message : String ? = "",

)

@Keep
data class GetPanCardVerificationDetailsRequestModel(
var InnovID : String ? ="",
var PanNo : String ? = ""
)

