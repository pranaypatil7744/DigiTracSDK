package com.innov.digitrac.domain.model.home_model

import androidx.annotation.Keep

@Keep
data class HomeBannerResponseModel(
    var lstBanners:ArrayList<ListBannerModel> = ArrayList(),
    var status:String? = "",
    var Message:String? = ""
)

@Keep
data class ListBannerModel(

    var BannerId:String? = "",
    var BannerName:String? = "",
    var BannerUrl:String? = "",
    var ClientId:String? = "",
    var Hyperlink:String? = "",
    var ValidTill:String? = "",
    var CategoryId:String? = "",
    var CreatedDate:String? = "",
    var CreatedBy:String? = "",
    var Updatedon:String? = "",
    var UpdatedBy:String? = "",
    var IsInnovId:String?=""
)

@Keep
data class HomeBannerRequestModel(
    var EmployeeId:String = "",
    var InnovId:String? = "",
    var Source:String? = "",
)
