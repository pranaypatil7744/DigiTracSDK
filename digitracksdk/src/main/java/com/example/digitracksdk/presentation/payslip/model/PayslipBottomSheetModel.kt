package com.example.digitracksdk.presentation.payslip.model

import androidx.annotation.Keep
import com.example.digitracksdk.presentation.payslip.adapter.PayslipBottomSheetAdapter


/**
 * Created by Mo. Khurseed Ansari on 27-August-2021,16:23
 */
@Keep
data class PayslipBottomSheetModel(
    var name: String? = "",
    var year: String? = "",
    var month: String? = "",
    var summaryDetailsType: PayslipBottomSheetAdapter.SummaryDetailsType
)