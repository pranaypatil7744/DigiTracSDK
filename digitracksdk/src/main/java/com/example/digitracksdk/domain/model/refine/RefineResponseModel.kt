package com.example.digitracksdk.domain.model.refine

import androidx.annotation.Keep

@Keep
data class RefineRequest(var EmployeeID: String)

@Keep
data class RefineResponseModel(var URL: String)