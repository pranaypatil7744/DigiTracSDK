package com.example.digitracksdk.presentation.home.training.document_fragment.model

import androidx.annotation.Keep

@Keep
data class TrainingDocumentModel(
    var docName:String? ="",
    var docType:String? ="",
    var docDetails:String? ="",
    var docIcon:String? = null,
    var docLink:String? = null,
    var clientTrainingID:Int? = null
)
