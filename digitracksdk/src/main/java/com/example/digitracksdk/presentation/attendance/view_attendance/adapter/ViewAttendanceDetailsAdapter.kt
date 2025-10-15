package com.example.digitracksdk.presentation.attendance.view_attendance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.ViewAttendanceDetailsItemBinding
import com.example.digitracksdk.presentation.my_profile.model.ProfileDetailsModel

class ViewAttendanceDetailsAdapter(val context:Context,val list:ArrayList<ProfileDetailsModel>):RecyclerView.Adapter<ViewAttendanceDetailsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewAttendanceDetailsItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_attendance_details_item,parent,false)
        val binding = ViewAttendanceDetailsItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.binding.apply {
            tvTitle.text = data.title
            tvValue.text = data.value
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}