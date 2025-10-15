package com.example.digitracksdk.presentation.home.geo_tracking_2.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.io.Serializable

open class GeoTrackingDataBaseModel : RealmObject(),Serializable {
    @PrimaryKey
    var geoTrackingId: String = ""

    @Required
    var latitude: String = ""

    @Required
    var longitude: String = ""
}
data class GeoDatabaseModel(
    var latitude:Double=0.0,
    var longitude:Double=0.0
):Serializable