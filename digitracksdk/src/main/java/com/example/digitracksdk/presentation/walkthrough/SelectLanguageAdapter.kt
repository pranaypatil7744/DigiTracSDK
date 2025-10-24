package com.example.digitracksdk.presentation.walkthrough

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.AttendanceListItemBinding

class SelectLanguageAdapter(var context: Context,var langList:ArrayList<String>,val listener: LanguageManager):RecyclerView.Adapter<SelectLanguageAdapter.ViewHolder>() {

    class ViewHolder(var binding: AttendanceListItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.attendance_list_item,parent,false)
        val binding = AttendanceListItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.binding.apply {
           tvItemName.text = langList[position]
       }
        holder.itemView.setOnClickListener{
            listener.ClickOnLanguage(position)
        }
    }

    override fun getItemCount(): Int {
        return langList.size
    }

    interface LanguageManager{
        fun ClickOnLanguage(position: Int)
    }
}