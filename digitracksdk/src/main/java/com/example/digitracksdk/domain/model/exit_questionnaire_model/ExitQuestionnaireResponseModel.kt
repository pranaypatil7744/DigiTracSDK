package com.example.digitracksdk.domain.model.exit_questionnaire_model


import androidx.annotation.Keep

@Keep
data class ExitQuestionnaireResponseModel(
    var Message: String? = "",
    var status: String? = ""
)

@Keep
data class ExitQuestionnaireRequestModel(
    var ExitQuestionnaire1: String? = "",
    var ExitQuestionnaire2: String? = "",
    var ExitQuestionnaire3: String? = "",
    var ExitQuestionnaire4: String? = "",
    var ExitQuestionnaire5: String? = "",
    var ExitRemark: String? = "",
    var GnetAssosiateId: String? = ""
)