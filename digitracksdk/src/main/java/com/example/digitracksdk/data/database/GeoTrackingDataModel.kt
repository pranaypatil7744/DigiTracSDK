package com.example.digitracksdk.data.database

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

/**
 * Created by Mo Khurseed Ansari on 25-Jul-2022.
 */
open class GeoTrackingDataModel  : RealmObject() {
    @PrimaryKey
    var id :String =""
    @Required
    var trackingLatitude: String? = ""
    @Required
    var trackingLongitude: String? = ""
    @Required
    var  trackingTimeStamp  : String ? = ""
}