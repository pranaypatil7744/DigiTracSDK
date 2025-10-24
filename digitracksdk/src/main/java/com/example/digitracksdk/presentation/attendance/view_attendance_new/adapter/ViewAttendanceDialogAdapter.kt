package com.example.digitracksdk.presentation.attendance.view_attendance_new.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.ItemViewAttendanceNewBinding
import com.example.digitracksdk.presentation.attendance.view_attendance_new.model.DialogModel

class ViewAttendanceDialogAdapter(private val list: ArrayList<DialogModel>) :
    RecyclerView.Adapter<ViewAttendanceDialogAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemViewAttendanceNewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_view_attendance_new, parent, false)
        val binding=ItemViewAttendanceNewBinding.bind(v)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.binding.apply {
            textLabel.text = data.title
            textDate.text = data.subTitle
        }
    }
}