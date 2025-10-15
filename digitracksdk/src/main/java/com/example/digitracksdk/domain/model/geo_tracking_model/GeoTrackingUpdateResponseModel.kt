package com.example.digitracksdk.domain.model.geo_tracking_model

import androidx.annotation.Keep

/**
 * Created by Mo Khurseed Ansari on 22-Jul-2022.
 */

@Keep
data class GeoTrackingUpdateResponseModel(
    var AssociateGeoTrackingID: String? = "",
    var Timeinterval: String? = "",
    var Status: String? = "",
    var Message: String? = "",

    )

@Keep
data class GeoTrackingUpdateRequestModel(
    var AssociateGeoTrackingID: String? = "",
    var GNETAssociateID: String? = "",
    var GeotrackingDateTime: String? = "",
    var InputType: String? = "",
    var Latitude: String? = "",
    var Longitude: String? = "",
    var VehicleType: String? = "",
    var Address: String? = ""
)
