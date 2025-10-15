package com.example.digitracksdk.domain.model.my_letters

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class GetIncrementLetterResponseModel(
    @SerializedName("ImageArr") var ImageArr: String? = null,
    @SerializedName("Status") var Status: String? = null,
    @SerializedName("Message") var Message: String? = null
)
