package com.example.digitracksdk.domain.model.reimbursement_model

import androidx.annotation.Keep

@Keep
data class ReimbursementCategoryResponseModel(
    var ReimbursementCategoryDetails: ArrayList<ReimbursementCategoryDetailsModel>? = ArrayList(),
    var status: String? = "",
    var Message: String? = ""
)

@Keep
data class ReimbursementCategoryDetailsModel(
    var ReimbursementCategoryId: Int? = 0,
    var ReimbursementCategory: String? = "",
    var IsTaxApplicable:String? = ""
)

@Keep
data class ReimbursementCategoryRequestModel(
    var EmployeeID: String = ""
)



@Keep
data class ReimbursementSubCategoryResponseModel(
    var ReimbursementSubCategoryDetails: ArrayList<ReimbursementSubCategoryDetailsModel>? = ArrayList(),
    var status: String? = "",
    var Message: String? = ""
)

@Keep
data class ReimbursementSubCategoryDetailsModel(
    var ReimbursementSubCategoryId: Int? = 0,
    var ReimbursementSubCategory: String? = "",
)

@Keep
data class ReimbursementSubCategoryRequestModel(
    var ReimbursementCategoryId: Int = 0
)