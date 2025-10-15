package com.example.digitracksdk.domain.model.attendance_model

data class LeaveHexCodeResponseModel(

    var lstAttendance: ArrayList<LeaveHexData> = ArrayList(),
    var status: String? = "",
    var Message: String? = ""

)

data class LeaveHexData(
    var LeaveType: String? = "",
    var HexCode: String? = ""
)

data class LeaveHexCodeRequestModel(
    var GnetassociateId :String=""
)