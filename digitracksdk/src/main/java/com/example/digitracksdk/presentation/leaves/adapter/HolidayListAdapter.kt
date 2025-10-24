package com.example.digitracksdk.presentation.leaves.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.HolidayListItemBinding
import com.example.digitracksdk.presentation.leaves.model.HolidayListModel

class HolidayListAdapter(var context: Context, var holidayList: ArrayList<HolidayListModel>) :
    RecyclerView.Adapter<HolidayListAdapter.ViewHolder>() {

        class ViewHolder(var binding:HolidayListItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.holiday_list_item,parent,false)
        val binding = HolidayListItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val data = holidayList[position]
        holder.binding.apply {
            tvSrNo.text = (position + 1).toString()
            tvDate.text = data.date.toString()
            tvHolidayName.text = data.holidayName.toString()
        }
    }

    override fun getItemCount(): Int {
       return holidayList.size
    }
}