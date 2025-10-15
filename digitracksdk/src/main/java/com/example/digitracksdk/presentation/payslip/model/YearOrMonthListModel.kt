package com.example.digitracksdk.presentation.payslip.model

import androidx.annotation.Keep

@Keep
data class YearOrMonthListModel(
    var itemName:String? = "",
    var isSelected:Boolean?= false
)
