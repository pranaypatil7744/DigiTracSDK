package com.example.digitracksdk.domain.model.onboarding.educational_details


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class EducationalStreamResponseModel(
    @SerializedName("lstResignationReason")
    var lstResignationReason: ArrayList<LstResignationReason>,
    @SerializedName("Message")
    var message: String? = "",
    @SerializedName("status")
    var status: String? = ""
)

@Keep
data class LstResignationReason(
    @SerializedName("StreamId")
    var streamId: Int? = 0,
    @SerializedName("StreamName")
    var streamName: String? = ""
)

@Keep
data class EducationalStreamRequestModel(
    var EducationCategoryID: String? = ""
)