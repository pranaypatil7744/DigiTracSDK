package com.example.digitracksdk.presentation.attendance

interface AttendanceManagerListener {
    fun clickOnPrevBtn(position: Int){}
    fun clickOnNextBtn(position: Int){}
    fun clickOnAttendance(position: Int){}
    fun clickOnAttendanceItem(position: Int){}
    fun clickOnSwitch(isCheck:Boolean){}

}