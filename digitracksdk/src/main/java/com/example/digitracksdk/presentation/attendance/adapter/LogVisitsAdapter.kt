package com.example.digitracksdk.presentation.attendance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.LogVisitItemBinding
import com.example.digitracksdk.domain.model.attendance_model.LogDataListModel

class LogVisitsAdapter(val context: Context, private val visitList:ArrayList<LogDataListModel>, val listener: VisitClickManager):RecyclerView.Adapter<LogVisitsAdapter.ViewHolder>() {

    class ViewHolder(val binding:LogVisitItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.log_visit_item, parent, false)
        val binding = LogVisitItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = visitList[position]
        holder.binding.apply {
            tvVisit.text = context.getString(R.string.visit)+data.SrNo
            tvVisitTimeValue.text = data.LogTime?.split(".")?.first()
        }
        holder.itemView.setOnClickListener {
            listener.clickOnVisit(position)
        }
    }

    override fun getItemCount(): Int {
        return visitList.size
    }

    interface VisitClickManager{
        fun clickOnVisit(position: Int)
    }
}