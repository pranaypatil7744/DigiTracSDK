package com.example.digitracksdk.presentation.onboarding.family_details_screen.model

import androidx.annotation.Keep


/**
 * To save & show values of multi edit text
 */

@Keep
data class MultiEditTextModel(
    var hint1:String? = "",
    var hint2:String? = "",
    var value1:String? = "",
    var value2:String? = "",
    var inputType: TxtInputType? = TxtInputType.NORMAL_TXT,
    var inputType2: TxtInputType? = TxtInputType.NORMAL_TXT,
    var maxLength1: Int? = null,
    var maxLength2: Int? = null,
    var error1:String? = null,
    var error2:String? = null,
    var isFocus1:Boolean? = null,
    var isFocus2:Boolean? = null,
    var isEnable1:Boolean? = null,
    var isEnable2:Boolean? = null
)

enum class TxtInputType(val value:Int){
    NORMAL_TXT(1),
    NUMBER(2),
}