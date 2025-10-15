package com.example.digitracksdk.presentation.attendance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.TimeSheetDailyItemBinding
import com.example.digitracksdk.domain.model.attendance_model.AttendenceListModel

class AttendanceTimeSheetListAdapter(var context: Context,var list:ArrayList<AttendenceListModel>):RecyclerView.Adapter<AttendanceTimeSheetListAdapter.ViewHolder>() {

    class ViewHolder(var binding:TimeSheetDailyItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.time_sheet_daily_item, parent, false)
        val binding = TimeSheetDailyItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.binding.apply {
            tvCheckInTime.text = if(data.InDateTime.isNullOrEmpty()) "--:--:--" else data.InDateTime
            tvCheckOutTime.text = if(data.OutDateTime.isNullOrEmpty()) "--:--:--" else data.OutDateTime
            tvDate.text = data.AttendanceDate?.split("-")?.first()
            tvWorkingHrsTime.text = if(data.Workinghrs.isNullOrEmpty()) "--:--" else data.Workinghrs
            tvDay.text = data.AttendanceDate?.slice(3..5)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}