package com.example.digitracksdk.presentation.home.help_and_support.model

import androidx.annotation.Keep

@Keep
data class IssueDetailsModel(
    val issueDetailsType: IssueDetailsType,
    val issueStatus: HelpSupportStatus? = null,
    val title:String? = "",
    val subTitle:String? = "",
    val title2:String? = "",
    val subTitle2:String? = ""
)

enum class IssueDetailsType(val value:Int){
    SINGLE_LINE(1),
    TWO_LINE(2),
    MULTI_LINE(3),
    DIVIDER(4)
}
