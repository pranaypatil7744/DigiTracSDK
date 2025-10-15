package com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class JobReferralResponseModel(@SerializedName("lstOpenDemandForCandidates")
                                    val lstOpenDemandForCandidates: List<LstOpenDemandForCandidatesItem>?)


@Keep
data class LstOpenDemandForCandidatesItem(@SerializedName("Status")
                                          val status: String? = "",
                                          @SerializedName("Designation")
                                          val designation: String? = "",
                                          @SerializedName("JDDetails")
                                          val jDDetails: String? = "",
                                          @SerializedName("RM_ClientRequirementID")
                                          val rMClientRequirementID: Int? = 0,
                                          @SerializedName("ApplicableAmount")
                                          val applicableAmount: String? = "",
                                          @SerializedName("ClientHOLocation")
                                          val clientHOLocation: String? = "",
                                          @SerializedName("End_Date")
                                          val endDate: String? = "",
                                          @SerializedName("LocationName")
                                          val locationName: String? = "",
                                          @SerializedName("IndustryType")
                                          val industryType: String? = "",
                                          @SerializedName("SalaryRange")
                                          val salaryRange: String? = "",
                                          @SerializedName("Role")
                                          val role: String? = "",
                                          @SerializedName("ClientName")
                                          val clientName: String? = "",
                                          @SerializedName("ISShift")
                                          val iSShift: String? = "",
                                          @SerializedName("Received_date")
                                          val receivedDate: String? = "",
                                          @SerializedName("Branch")
                                          val branch: String? = "",
                                          @SerializedName("CategoryName")
                                          val categoryName: String? = "",
                                          @SerializedName("DocumentPath")
                                          val documentPath: String? = "",
                                          @SerializedName("Position_Count")
                                          val positionCount: String? = "",
                                          @SerializedName("Created_By")
                                          val createdBy: String? = "",
                                          @SerializedName("PolicyPath")
                                          val policyPath: String? = "",
                                          @SerializedName("PolicyPathUrl")
                                          val policyPathUrl: String? = "",
                                          @SerializedName("ExperienceRange")
                                          val experienceRange: String? = "",
                                          @SerializedName("Created_Date")
                                          val createdDate: String? = ""):Serializable


