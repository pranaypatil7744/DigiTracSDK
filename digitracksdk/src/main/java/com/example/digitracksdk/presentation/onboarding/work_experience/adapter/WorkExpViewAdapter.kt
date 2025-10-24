package com.example.digitracksdk.presentation.onboarding.work_experience.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.ItemEducationalDetailsBinding
import com.example.digitracksdk.domain.model.onboarding.ListWorkExpModel

class WorkExpViewAdapter(val context: Context, val list: ArrayList<ListWorkExpModel>,
                         val listener: WorkExpClickManager
) :
    RecyclerView.Adapter<WorkExpViewAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemEducationalDetailsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_educational_details, parent, false)
        val binding = ItemEducationalDetailsBinding.bind(view)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.binding.apply {
            textQualification.text = data.Designation
            textUniversity.text = data.CompanyName
            textMonthYear.text = data.Payment
            textPercentage.text = data.StartDate + " - " + data.EndDate
        }
        holder.itemView.setOnClickListener {
            listener.clickOnItem(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface WorkExpClickManager {
        fun clickOnItem(position: Int)
    }
}