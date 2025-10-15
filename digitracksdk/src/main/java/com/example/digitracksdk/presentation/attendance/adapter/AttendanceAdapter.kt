package com.example.digitracksdk.presentation.attendance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.AttendanceListItemBinding
import com.innov.digitrac.databinding.AttendanceTopItemBinding
import com.example.digitracksdk.presentation.attendance.AttendanceManagerListener
import com.example.digitracksdk.presentation.attendance.model.AttendanceListModel
import com.example.digitracksdk.presentation.attendance.model.AttendanceStatus
import com.example.digitracksdk.presentation.attendance.model.AttendanceType

class AttendanceAdapter(
    val context: Context,
    var attendanceItemList: ArrayList<AttendanceListModel>, var listener: AttendanceManagerListener
) : RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {
    class ViewHolder : RecyclerView.ViewHolder {

        var attendanceTopItemBinding: AttendanceTopItemBinding? = null
        var attendanceListItemBinding: AttendanceListItemBinding? = null

        constructor(binding: AttendanceTopItemBinding) : super(binding.root) {
            attendanceTopItemBinding = binding
        }

        constructor(binding: AttendanceListItemBinding) : super(binding.root) {
            attendanceListItemBinding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            AttendanceType.ATTENDANCE_TOP_ITEM.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.attendance_top_item, parent, false)
                val binding = AttendanceTopItemBinding.bind(view)
                ViewHolder(binding)
            }
            else -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.attendance_list_item, parent, false)
                val binding = AttendanceListItemBinding.bind(view)
                ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = attendanceItemList[position]
        when(holder.itemViewType){
            AttendanceType.ATTENDANCE_TOP_ITEM.value -> {
                holder.attendanceTopItemBinding?.apply {
                    tvTime.text = data.time
                    tvDayDate.text = data.dateDay
                    switchAttendanceAnywhere.isChecked = data.isAttendanceAnywhere == true
                    layoutAttendance.setOnClickListener {
                        if (data.attendanceStatus != AttendanceStatus.CHECK_OUT){
                            listener.clickOnAttendance(position)
                        }
                    }
                    switchAttendanceAnywhere.setOnCheckedChangeListener { compoundButton, b ->
                        listener.clickOnSwitch(compoundButton.isChecked)
                    }
                    when (data.attendanceStatus) {
                        AttendanceStatus.NONE -> {
                            tvCheckInTime.text = "--:--"
                            tvCheckOutTime.text = "--:--"
                            tvWorkingHrsTime.text = "--:--"
                            tvCheckInOut.text = context.getString(R.string.check_in)
                            tvCheckInOut.setTextColor(ContextCompat.getColor(context,R.color.jungle_green))
                            imgFinger.imageTintList = ContextCompat.getColorStateList(context,R.color.jungle_green)
                            bgAttendanceBtn.apply {
                                isEnabled=true
                                strokeColor = ContextCompat.getColorStateList(context,R.color.jungle_green_8)
                                setBackgroundColor(ContextCompat.getColor(context,R.color.jungle_green_15))
                            }
                        }
                        AttendanceStatus.CHECK_IN -> {
                            tvCheckInTime.text = data.checkInTime
                            tvCheckOutTime.text = "--:--"
                            tvWorkingHrsTime.text = data.workingHrs
                            tvCheckInOut.text = context.getString(R.string.check_out)
                            tvCheckInOut.setTextColor(ContextCompat.getColor(context,R.color.cinnabar))
                            imgFinger.imageTintList = ContextCompat.getColorStateList(context,R.color.cinnabar)
                            bgAttendanceBtn.apply {
                                isEnabled=true
                                strokeColor = ContextCompat.getColorStateList(context,R.color.cinnabar_8)
                                setBackgroundColor(ContextCompat.getColor(context,R.color.cinnabar_15))
                            }
                        }
                        AttendanceStatus.CHECK_OUT -> {
                            tvCheckInOut.text = context.getString(R.string.complete)
                            tvCheckInTime.text = data.checkInTime
                            tvCheckOutTime.text = data.checkOutTime
                            tvWorkingHrsTime.text = data.workingHrs
                            tvCheckInOut.setTextColor(ContextCompat.getColor(context,R.color.gray))
                            imgFinger.imageTintList = ContextCompat.getColorStateList(context,R.color.gray)
                            bgAttendanceBtn.apply {
                                isEnabled=false
                                strokeColor = ContextCompat.getColorStateList(context,R.color.gray_8)
                                setBackgroundColor(ContextCompat.getColor(context,R.color.gray_15))
                            }
                        }
                    }
                }
            }
            AttendanceType.ATTENDANCE_ITEM_LIST.value -> {
                holder.attendanceListItemBinding?.apply {
                    tvItemName.text = data.attendanceItemName
                    data.attendanceItemIcon?.let { imgIcon.setImageResource(it) }
                }
                holder.itemView.setOnClickListener {
                    listener.clickOnAttendanceItem(position)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return attendanceItemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return attendanceItemList[position].attendanceType.value
    }

}