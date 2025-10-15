package com.example.digitracksdk.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Mo Khurseed Ansari on 03-10-2023.
 */
data class CommonApiErrorModel(

   var success: Boolean? = null,
   @SerializedName("Message")
   var message: String? = null
)