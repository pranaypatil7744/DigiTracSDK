package com.example.digitracksdk.presentation.home.jobs_and_refer_fds.job_details_screen.model

import androidx.annotation.Keep

@Keep
data class JobDetailsModel(
    val jobDetailsType: JobDetailsType,
    var title:String? = "",
    var subTitle:String? = "",
    var min_exp:String? = "",
    var location:String? = "",
    var profilePic:Int? = null,
    var isRemote:Boolean? = false,
    var isFullTime:Boolean? = false,
    var points: ArrayList<String>?= ArrayList()
)
enum class JobDetailsType(val value:Int){
    JOB_DETAILS_TOP(1),
    JOB_DETAILS_WITH_BULLETS(2),
    JOB_DETAILS_WITHOUT_BULLETS(3),
}
