package com.example.digitracksdk.domain.model.geo_tracking_model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * Created by Mo Khurseed Ansari on 26-Jul-2022.
 */
@Keep
data class GeoTrackingDetailsResponseModel(
    var lstGeoTrackingDetails :ArrayList<lstGeoTrackingDetails> = ArrayList(),
    var Status:String? = "",
    var Message:String? = "",



    )

@Keep
data class lstGeoTrackingDetails (
    /*var Latitute: String? = "",
    var Longitude: String? = "",
    var CreatedDate: String? = "",
    var AssociateGeoTrackingDetailID: String? = "",*/

    @SerializedName("Latitute") var Latitute: String? = "",
    @SerializedName("Longitude") var Longitude: String? = "",
    @SerializedName("CreatedDate") var CreatedDate: String? = "",
    @SerializedName("AssociateGeoTrackingDetailID")
    var AssociateGeoTrackingDetailID: String? = null


)

@Keep
data class GeoTrackingDetailsRequestModel(
    var AssociateGeoTrackingID: String = ""
)
