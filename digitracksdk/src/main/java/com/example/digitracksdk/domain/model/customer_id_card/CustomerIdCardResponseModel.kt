package com.example.digitracksdk.domain.model.customer_id_card

import androidx.annotation.Keep

/**
 * Created by Mo Khurseed Ansari on 15-Sep-2022.
 */
@Keep
data class CustomerIdCardResponseModel(
    var Message: String?="",
    var lstBanners: List<LstBanner>?= null,
    var status: String?=""
)


@Keep
data class LstBanner(
    var BannerId: Int?=0,
    var BannerName: String?="",
    var BannerUrl: String?="",
    var CategoryId: Int?=0,
    var ClientId: Int?=0,
    var CreatedBy: String?="",
    var CreatedDate: String?="",
    var Hyperlink: String?="",
    var UpdatedBy: String?="",
    var Updatedon: String?="",
    var ValidTill: String?=""
)
@Keep
data class CustomerIdCardRequestModel
    (
    var EmployeeId: String = "",
    var InnovID: String = "",
    var Source: String = ""
)
