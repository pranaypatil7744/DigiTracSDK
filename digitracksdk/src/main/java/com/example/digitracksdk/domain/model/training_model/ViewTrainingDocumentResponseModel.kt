package com.example.digitracksdk.domain.model.training_model

import androidx.annotation.Keep

@Keep
data class ViewTrainingDocumentResponseModel(
    var ClientTrainingID:String? ="",
    var ClientTrainingFilePath:String? ="",
    var ClientTrainingImageArr:String? =""
)

@Keep
data class ViewTrainingDocumentRequestModel(
    var ClientTrainingID:Int = 0
)
