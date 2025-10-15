package com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class ReferredUsersResponseModel(@SerializedName("LstreferredDetails")
                                      val lstreferredDetails: List<LstreferredDetailsItem>?
)


@Keep
data class LstreferredDetailsItem(@SerializedName("MobileNo")
                                  val mobileNo: String = "",
                                  @SerializedName("RoleProfileApplyingFor")
                                  val roleProfileApplyingFor: String = "",
                                  @SerializedName("Status")
                                  val status: String = "",
                                  @SerializedName("EmailID")
                                  val emailID: String = "",
                                  @SerializedName("FirstName")
                                  val firstName: String = "",
                                  @SerializedName("ApplicableAmount")
                                  val applicableAmount: String = "",
                                  @SerializedName("Gender")
                                  val gender: String = "",
                                  @SerializedName("MiddleName")
                                  val middleName: String = "",
                                  @SerializedName("AdharCard")
                                  val adharCard: String = "",
                                  @SerializedName("Skill")
                                  val skill: String = "",
                                  @SerializedName("LastName")
                                  val lastName: String = "",
                                  @SerializedName("BranchName")
                                  val branchName: String = "",
                                  @SerializedName("Location")
                                  val location: String = ""):Serializable


