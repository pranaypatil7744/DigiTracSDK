package com.example.digitracksdk.domain.model.my_letters

import androidx.annotation.Keep

@Keep
data class OtherLettersResponseModel(
    var AssociateAllLetterslist:ArrayList<AssociateAllLettersListModel>? = ArrayList(),
    var Status:String? ="",
    var Message:String? =""
)
@Keep
data class AssociateAllLettersListModel(
    var LetterType:String? ="",
    var LetterDate:String? ="",
    var FilePath:String? ="",
    var Remarks:String? ="",
)

@Keep
data class OtherLettersRequestModel(
    var AssociateID:String ="",
    var InnovID:String =""
)
