package com.example.digitracksdk.domain.model.refer_a_friend

import androidx.annotation.Keep

@Keep
data class SkillListResponseModel(
    var lstSkillset:ArrayList<SkillListModel>? = ArrayList(),
    var status:String? ="",
    var Message:String? =""
)

@Keep
data class SkillListModel(
    var SkillSetID:Int? =0,
    var SkillName:String? =""
)