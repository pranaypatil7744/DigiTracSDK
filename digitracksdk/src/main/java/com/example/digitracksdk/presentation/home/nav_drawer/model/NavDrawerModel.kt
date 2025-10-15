package com.example.digitracksdk.presentation.home.nav_drawer.model

import android.graphics.Bitmap
import androidx.annotation.Keep

@Keep
data class NavDrawerModel(
    var profileName:String? = "",
    var email:String? = "",
    var profilePic:Bitmap? = null,
    var backgroundPic:Int? = null,
    var itemIcon:Int? = null,
    var itemName:String? = "",
    var navDrawerType: NavDrawerType
)
enum class NavDrawerType(val value:Int){
    NAV_PROFILE(1),
    NAV_ITEMS(2),
    DIVIDER(3)
}
