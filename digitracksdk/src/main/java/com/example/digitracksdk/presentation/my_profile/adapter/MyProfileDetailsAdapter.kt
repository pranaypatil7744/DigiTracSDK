package com.example.digitracksdk.presentation.my_profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.ProfileDetailItemsBinding
import com.example.digitracksdk.presentation.my_profile.model.ProfileDetailsModel

class MyProfileDetailsAdapter(val context: Context,val list:ArrayList<ProfileDetailsModel>)
    :RecyclerView.Adapter<MyProfileDetailsAdapter.ViewHolder>() {

    class ViewHolder(val binding:ProfileDetailItemsBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.profile_detail_items,parent,false)
        val binding = ProfileDetailItemsBinding.bind(view)
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