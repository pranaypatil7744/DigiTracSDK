package com.example.digitracksdk.domain.model.geo_tracking_model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Mo Khurseed Ansari on 27-Apr-2022.
 */

@Keep
data class GeoTrackingSummaryListResponseModel(
    @SerializedName("Status")
    val status: String = "",
    @SerializedName("Message")
    val message: String = "",
    var lstGeoTrackingList: ArrayList<GeoTrackingList> = ArrayList(),
)

@Keep
data class GeoTrackingList
    (
    var AssociateGeoTrackingID: String = "",
    var AssociateID: String = "",
    var CreatedDate: String = "",
    var StartDateTime: String = "",
    var EndDateTime: String = "",
    var StartLat: String = "",
    var StartLon: String = "",
    var EndLat: String = "",
    var EndLon: String = "",
    var StartAddress: String = "",
    var EndAddress: String = ""

) : Serializable

@Keep
data class GeoTrackingSummaryListRequestModel(
    var GNETAssociateID: String = "",
    var FromDate: String = "",
    var ToDate: String = ""
)