package com.example.digitracksdk.presentation.home.help_and_support.model

import androidx.annotation.Keep

@Keep
data class HelpAndSupportModel(
    var helpSupportItemType: HelpSupportItemType,
    var date:String? = "",
    var helpSupportDetails: HelpSupportDetails? =  null
)
@Keep
data class HelpSupportDetails(
    var issueTitle:String? = "",
    var issueType:String? = "",
    var issueDate:String? = "",
    var issueDetails:String? = "",
    var resolvedDate:String? = "",
    var associateQueryId:String?="",
    var helpSupportStatus: HelpSupportStatus
)
enum class HelpSupportStatus(val value: Int){
    OPEN(10),
    CLOSE(20)
}
enum class HelpSupportItemType(val value:Int){
    TITLE(1),
    MAIN_ITEM(2)
}
