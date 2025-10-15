package com.example.digitracksdk.domain.model.training_model

import androidx.annotation.Keep

@Keep
data class TrainingResponseModel(
    var lstClientTrainingDetails:ArrayList<ClientTrainingModel>? = ArrayList(),
    var status:String? = "",
    var Message:String? = ""
)

@Keep
data class TrainingRequestModel(
    var InnovId:String? = ""
)

@Keep
data class ClientTrainingModel(
    var InnovID:String? = "",
    var ClientName:String? = "",
    var ClientTrainingID:Int? = 0,
    var ClientTrainingType:String? = "",
    var ClientTrainingName:String? = "",
    var ClientTrainingFileName:String? = "",
    var ClientTrainingFilePath:String? = "",
    var CreatedDate:String? = "",
    var TrainingTypeFilePath:String? = "",
    var TrainingTypeImageArr:String? = "",
    var VideoLink:String? = "",
)