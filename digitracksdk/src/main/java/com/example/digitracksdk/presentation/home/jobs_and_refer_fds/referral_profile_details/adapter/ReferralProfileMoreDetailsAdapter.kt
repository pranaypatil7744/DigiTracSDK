package com.example.digitracksdk.presentation.home.jobs_and_refer_fds.referral_profile_details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ProfileEduWorkDetailsItemBinding
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.referral_profile_details.model.ProfileDetailsType
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.referral_profile_details.model.ProfileMoreDetailsModel

class ReferralProfileMoreDetailsAdapter(
    val context: Context,
    private val moreProfileList: ArrayList<ProfileMoreDetailsModel>,
    private val currentItemType: ProfileDetailsType
):RecyclerView.Adapter<ReferralProfileMoreDetailsAdapter.ViewHolder>() {
    class ViewHolder(val binding: ProfileEduWorkDetailsItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.profile_edu_work_details_item,parent,false)
        val binding = ProfileEduWorkDetailsItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = moreProfileList[position]
        holder.binding.apply {
            when(currentItemType){
                ProfileDetailsType.WORK_EXPERIENCE ->{
                    tvTitle.text = data.designation
                    tvTitleSub.text = "${data.compName} - ${data.jobType}"
                    tvTitleSub2.text = "${data.timePeriod} - ${data.totalYear}"
                    tvTitleSub3.visibility = View.VISIBLE
                    tvTitleSub3.text = data.location
                    data.icon?.let { imgIcon.setImageResource(it) }
                }
                ProfileDetailsType.EDUCATION_DETAILS ->{
                    tvTitle.text = data.clgName
                    tvTitleSub.text = data.educationName
                    tvTitleSub2.text = data.timePeriod
                    data.icon?.let {
                        imgIcon.setImageResource(it)
                    }
                }
                else -> {}
            }
        }
    }

    override fun getItemCount(): Int {
       return moreProfileList.size
    }
}