package com.example.digitracksdk.domain.model.attendance_model


data class AttendanceCycleResponseModel(
    var GnetassociateId: String?="",
    var StartDate:String?="",
    var EndDate:String?="",
    var Status:String?="",
    var Message:String?="",
)

data class AttendanceCycleRequestModel(var GnetassociateId: String?="")