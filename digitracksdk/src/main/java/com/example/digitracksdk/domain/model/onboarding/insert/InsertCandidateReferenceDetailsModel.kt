package com.example.digitracksdk.domain.model.onboarding.insert

import androidx.annotation.Keep

@Keep
data class InsertCandidateReferenceDetailsModel(
  var Address: String = "" ,
  var ContactNo: String = "",
  var EmailId: String = "",
  var InnovId: String = "",
  var Name: String = "",
  var ReferenceCategoryID: Int = 0,
  var ReferenceCategory: String = "",
  var Source: String = ""
)

@Keep
data class InsertCandidateReferenceDetailsResponseModel(
  var status: String? = "",
  var Message: String? = "",
  var OTP: String? = "",
  var TokenID: String? = "",
  var InnovID: String? = ""
)