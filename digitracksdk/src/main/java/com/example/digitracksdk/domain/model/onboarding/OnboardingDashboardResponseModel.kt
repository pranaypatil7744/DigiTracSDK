package com.example.digitracksdk.domain.model.onboarding

import androidx.annotation.Keep

@Keep
data class OnboardingDashboardResponseModel(
    var CandidateDetails:String? = "",
    var Aadhaarverification:String? = "",
    var PanCardverification:String? = "1",
    var Bankverification:String? = "1",
    var PFUAN:String?="1",
    var EducationDetails:String? = "",
    var FamilyDetails:String? = "",
    var WorkExperienceDetails:String? = "",
    var CandidateBankDetails:String? = "",
    var CandidateReferenceDetails:String? = "",
    var CandidateSignature:String? = "",
    var KYCDocumentDetails:String? = "",
    var DocumentDetails:String? = "",
    var ESICDetails:String? = "",
    var EPFDetails:String? = "",
    var Status:String? = "",
    var Message:String? = ""
)
