package com.example.digitracksdk.presentation.onboarding.family_details_screen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.ItemFamilyDetailsBinding
import com.example.digitracksdk.domain.model.onboarding.ListFamily

class FamilyDetailsAdapter(
    val context: Context,
    var list: ArrayList<ListFamily>,
    var listener: FamilyListener
) : RecyclerView.Adapter<FamilyDetailsAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemFamilyDetailsBinding) : RecyclerView.ViewHolder(binding.root) {

        var itemFamilyDetailsBinding: ItemFamilyDetailsBinding? = binding

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_family_details, parent, false)
        val binding = ItemFamilyDetailsBinding.bind(view)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.itemFamilyDetailsBinding?.apply {
            textName.text = data.FamilyMemberName+" (${data.Relation})"
            textDesignation.text = data.Occupation
            textDob.text = data.DateOfBirth
            if (data.IsNominee == context.getString(R.string.yes)){
                txtIsNominee.visibility = VISIBLE
            }else{
                txtIsNominee.visibility = GONE
            }
            icGender.setImageResource(
                when (data.Relation) {
                    Constant.Relation.Father, Constant.Relation.Son, Constant.Relation.Husband -> {
                        R.drawable.ic_male
                    }
                    Constant.Relation.Mother, Constant.Relation.Daughter, Constant.Relation.Wife -> {
                        R.drawable.ic_female
                    }
                    else -> {
                        R.drawable.profile_placeholder
                    }
                }
            )
            containerFamilyDetails.setOnClickListener {
                listener.onFamilyDetailClicked(position, data)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface FamilyListener {
        fun onFamilyDetailClicked(position: Int, data: ListFamily)
    }
}