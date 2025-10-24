package com.example.digitracksdk.presentation.home.home_fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.HomeItemsBinding
import com.example.digitracksdk.presentation.home.home_fragment.model.HomeDashboardMenu

class HomeDashboardAdapter(
    val context: Context,
    private val homeItems: ArrayList<HomeDashboardMenu>,
    val listener: HomeAdapter.HomeOnClickManager
) :
    RecyclerView.Adapter<HomeDashboardAdapter.ViewHolder>() {

    class ViewHolder(val binding: HomeItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.home_items, parent, false)
        val binding = HomeItemsBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = homeItems[position]
        holder.binding.apply {
            tvTitle.text = data.title
            data.icon1?.let { imgIcon.setImageResource(it) }
        }
        holder.itemView.setOnClickListener {
            listener.clickOnItem(position,data.title?:"")
        }
    }

    override fun getItemCount(): Int {
        return homeItems.size
    }
}