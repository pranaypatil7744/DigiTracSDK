package com.example.digitracksdk.domain.model.attendance_regularization_model

import androidx.annotation.Keep

@Keep
data class AttendanceRegularizationListResponseModel(
    var lstAttendanceRegularize:ArrayList<ListAttendanceRegularizeModel>? = ArrayList(),
    var status:String? = "",
    var Message:String? = "",
)

@Keep
data class ListAttendanceRegularizeModel(
    var AssociateName:String? = "",
    var GNETAssociateID:String? = "",
    var AssociateID:String? = "",
    var RegularizationDate:String? = "",
    var RegularizationType:String? = "",
    var Status:String? = "",
    var Remarks:String? = "",
    var ApprovedDate:String? = "",
    var ApprovedRemark:String? = ""
)

@Keep
data class AttendanceRegularizationListRequestModel(
    var FromDate:String = "",
    var GNETAssociateID:String = "",
    var InnovId:String = "",
    var ToDate:String = "",
)
