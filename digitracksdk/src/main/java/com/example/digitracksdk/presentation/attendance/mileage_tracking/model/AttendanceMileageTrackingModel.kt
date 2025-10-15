package com.example.digitracksdk.presentation.attendance.mileage_tracking.model

import androidx.annotation.Keep
import com.example.digitracksdk.presentation.leaves.apply_leave.model.LeavesStatus

@Keep
data class AttendanceMileageTrackingModel(
    var date:String? ="",
    var startReading:String? ="",
    var closeReading:String? ="",
    var mileageTrackingStatus: LeavesStatus

)
enum class MileageTrackingStatus(val value:Int){
    REJECTED(1),
    APPROVED(2),
    PENDING(3)
}
