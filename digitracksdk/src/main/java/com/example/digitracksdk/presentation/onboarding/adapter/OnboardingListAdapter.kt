package com.example.digitracksdk.presentation.onboarding.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.OnboardingItemsBinding
import com.example.digitracksdk.presentation.onboarding.model.OnboardingListModel
import com.example.digitracksdk.presentation.onboarding.model.OnboardingStatus

class OnboardingListAdapter(
    val context: Context,
    var onboardingList: ArrayList<OnboardingListModel>, var listener: OnboardingClickManager
) : RecyclerView.Adapter<OnboardingListAdapter.ViewHolder>() {

    class ViewHolder(val binding: OnboardingItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.onboarding_items,parent,false)
        val binding = OnboardingItemsBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = onboardingList[position]
        holder.binding.apply {
            tvItemName.text = data.itemName
            data.itemIcon?.let { imgIcon.setImageResource(it) }
            if (data.status == OnboardingStatus.COMPLETED){
                imgStatus.setImageResource(R.drawable.ic_checkmark_circle)
//                tvLastUpdate.visibility = GONE
//                tvLastUpdate.text = context.getString(R.string.last_updated)+" : "+data?.lastUpdateDate
//                tvItemName.updateLayoutParams<ConstraintLayout.LayoutParams> { verticalBias = 0.20f }
            }else{
                imgStatus.setImageResource(R.drawable.ic_close_red)
                tvLastUpdate.visibility = GONE
                tvItemName.updateLayoutParams<ConstraintLayout.LayoutParams> { verticalBias = 0.50f }
            }
        }
        holder.itemView.setOnClickListener {
            listener.clickOnboardingItem(position)
        }
    }

    override fun getItemCount(): Int {
        return onboardingList.size
    }

    interface OnboardingClickManager{
        fun clickOnboardingItem(position: Int)
    }
}