package com.example.digitracksdk.presentation.home.home_fragment.model

import androidx.annotation.Keep
import com.innov.digitrac.domain.model.home_model.ListBannerModel
import com.example.digitracksdk.presentation.home.home_fragment.adapter.HomeAdapter

@Keep
data class HomeModel(
    var homeItemType: HomeAdapter.HomeItemType,
    var title:String? ="",
    var subTitle:String?="",
    var count1:String? ="",
    var icon1:Int?=null,
    var icon2:Int?=null,
    var icon3:Int?=null,
    var bannerImage:Int? = 0,
    var bannerList:ArrayList<ListBannerModel>?= ArrayList(),
    var btnText:String? ="",
    var progress:Int ? = null,
    var homeDashboardMenu: ArrayList<HomeDashboardMenu>? = ArrayList()
)
@Keep
data class HomeDashboardMenu(
    var title:String? ="",
    var icon1:Int?=null
)
