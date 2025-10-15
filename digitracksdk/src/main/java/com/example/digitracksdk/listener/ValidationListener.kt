package com.example.digitracksdk.listener

interface ValidationListener {
    fun onValidationSuccess(type:String,msg:Int)
    fun onValidationFailure(type:String,msg:Int)
}