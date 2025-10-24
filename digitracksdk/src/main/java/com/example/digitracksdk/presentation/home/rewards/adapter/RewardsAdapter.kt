package com.example.digitracksdk.presentation.home.rewards.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.RewardsItemBinding
import com.example.digitracksdk.domain.model.rewards.AssociateRewardDetail

class RewardsAdapter(
    val context: Context,
    private val rewardList: ArrayList<AssociateRewardDetail>,
    val listener: RewardClickManager
) : RecyclerView.Adapter<RewardsAdapter.ViewHolder>() {
    class ViewHolder(var binding: RewardsItemBinding) : RecyclerView.ViewHolder(binding.root)


    interface RewardClickManager {
        fun clickOnViewBtn(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rewards_item, parent, false)
        val binding = RewardsItemBinding.bind(view)
        return ViewHolder(binding)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = rewardList[position]
        holder.binding.apply {
            tvName.text = context.getString(R.string.category_) + "  " + data.RewardCategory
            tvPurpose.text = data.Purpose
            tvMonth.text = context.getString(R.string.month) + " :   " +data.Month
            tvYear.text = context.getString(R.string.year) + " :    " + data.Year
            containerRewardsDetails.setOnClickListener {
                listener.clickOnViewBtn(position)
            }
        }

    }

    override fun getItemCount(): Int {
        return rewardList.size
    }
}