package com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ReferredUsersRequestModel(@SerializedName("InnovID")
                                     val innovID: String? = "")