package com.example.digitracksdk.domain.model.home_model.innov_id_card

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Mo Khurseed Ansari on 03-10-2023.
 */

@Keep
data class QrCodeResponseModel(

    @SerializedName("QRCodeImagePath")
    @Expose
    var qRCodeImagePath: String = "",

    @SerializedName("Status")
    @Expose
    var status: String = "",
    @SerializedName("Message")
    @Expose
    var message: String = ""

)


@Keep
data class QrCodeRequestModel(
    var GNETAssociateID: String = ""
)

