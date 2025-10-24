package com.example.digitracksdk.presentation.home.jobs_and_refer_fds.referrals.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.ReferralsItemBinding
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.LstreferredDetailsItem

class ReferralsAdapter(val context: Context, private val referralList: ArrayList<LstreferredDetailsItem>, val listener: ReferralClickManager) :
    RecyclerView.Adapter<ReferralsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ReferralsItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.referrals_item, parent, false)
        val binding = ReferralsItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = referralList[position]
        holder.binding.apply {
            tvTitle.text = "${data.firstName} ${data.lastName}"
            tvDaysAgo.text = ""
            tvLocation.text = data.location
            tvSubTitle.text = data.skill
//            data.profileIcon?.let { imgProfile.setImageResource(it) }
        }
        holder.itemView.setOnClickListener {
            listener.onReferralClick(position)
        }
    }

    override fun getItemCount(): Int {
        return referralList.size
    }

    interface ReferralClickManager {
        fun onReferralClick(position: Int)
    }
}