package com.example.digitracksdk.domain.model.resignation

import androidx.annotation.Keep

@Keep
data class ResignationRequestModel(
    var AssociateId:String = "",
    var DateOfResignation:String = "",
    var EmployeeId:String = "",
    var ExpectedLastWorkingDate:String = "",
    var Extn:String = "",
    var InnovId:String = "",
    var Reason:String = "",
    var ResgImageArr:String = "",
    var ResignationCategoryId:Int = 0,
)

@Keep
data class ResignationResponseModel(
    var status:String? = "",
    var Message:String? = ""
)
