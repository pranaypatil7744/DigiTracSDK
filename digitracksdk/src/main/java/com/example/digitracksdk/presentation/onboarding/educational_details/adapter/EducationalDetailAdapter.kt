package com.example.digitracksdk.presentation.onboarding.educational_details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.ItemEducationalDetailsBinding
import com.example.digitracksdk.domain.model.onboarding.ListEducationDetails

class EducationalDetailAdapter(
    val context: Context,

    var list: ArrayList<ListEducationDetails>,
    var listener: EducationListener
) : RecyclerView.Adapter<EducationalDetailAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemEducationalDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var itemEducationalDetailsBinding: ItemEducationalDetailsBinding? = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_educational_details, parent, false)
        val binding = ItemEducationalDetailsBinding.bind(view)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.itemEducationalDetailsBinding?.apply {
            textQualification.text = data.CategoryName
            textMonthYear.text = data.PassYear
            textUniversity.text = data.BoardName
            textPercentage.text = data.Percentage
            containerEducationalDetails.setOnClickListener {
                listener.onEducationItemClicked(position,data)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface EducationListener{
        fun onEducationItemClicked(position: Int,data: ListEducationDetails)
    }

}