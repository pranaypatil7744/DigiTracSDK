package com.example.digitracksdk.presentation.attendance.view_attendance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.ViewAttendanceItemBinding
import com.example.digitracksdk.domain.model.attendance_model.ListViewAttendanceModel
import com.example.digitracksdk.presentation.my_profile.model.ProfileDetailsModel

class ViewAttendanceAdapter(val context:Context, val list:ArrayList<ListViewAttendanceModel>,
                            private val listener: ViewAttendanceClickManager
):RecyclerView.Adapter<ViewAttendanceAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewAttendanceItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_attendance_item,parent,false)
        val binding = ViewAttendanceItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.binding.apply {
            tvDayDate.text = data.Day+","+data.AttendanceDate
            val attendanceDetailsList:ArrayList<ProfileDetailsModel> = ArrayList()
            attendanceDetailsList.clear()
            attendanceDetailsList.add(ProfileDetailsModel(title = context.getString(R.string.check_in),value = data.InsTime.toString().split(".").first()))
            attendanceDetailsList.add(ProfileDetailsModel(title = context.getString(R.string.check_out),value = data.OutTime.toString().split(".").first()))
            attendanceDetailsList.add(ProfileDetailsModel(title = context.getString(R.string.working_hr_s),value = data.WorkHours.toString()))
            attendanceDetailsList.add(ProfileDetailsModel(title = context.getString(R.string.shift),value = data.Shift.toString()))
            attendanceDetailsList.add(ProfileDetailsModel(title = context.getString(R.string.status),value = data.AttendanceStatus.toString()))
            recyclerViewAttendanceDetails.adapter = ViewAttendanceDetailsAdapter(context,attendanceDetailsList)

            btnAttendanceRegularization.setOnClickListener {
                listener.onClickAttendanceRegularization(position)
            }

            btnApplyLeave.setOnClickListener {
                listener.onClickApplyLeave(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ViewAttendanceClickManager{
        fun onClickAttendanceRegularization(position: Int)
        fun onClickApplyLeave(position: Int)
    }
}