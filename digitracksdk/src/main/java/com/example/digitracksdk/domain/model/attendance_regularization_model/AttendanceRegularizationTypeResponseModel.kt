package com.example.digitracksdk.domain.model.attendance_regularization_model

import androidx.annotation.Keep

@Keep
data class AttendanceRegularizationTypeResponseModel(
    var LstAttnRegularizationType:ArrayList<ListAttnRegularizationTypeModel>?= ArrayList(),
    var Status:String? = "",
    var Message:String? = ""
)
@Keep
data class ListAttnRegularizationTypeModel(
    var RegularizationTypeID:Int? = 0,
    var RegularizationType:String? = "",
)

