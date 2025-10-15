package com.example.digitracksdk.presentation.home.jobs_and_refer_fds.referral_profile_details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.*
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.referral_profile_details.model.ProfileDetailsType
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.referral_profile_details.model.ReferralProfileDetailsModel

class ReferralProfileDetailsAdapter(
    val context: Context,
    var profileDetailsList: ArrayList<ReferralProfileDetailsModel>
) :
    RecyclerView.Adapter<ReferralProfileDetailsAdapter.ViewHolder>() {

    class ViewHolder : RecyclerView.ViewHolder {
        var profileDetailsTopItemBinding: ProfileDetailsTopItemBinding? = null
        var profileDetailsItemBinding: ProfileDetailsItemBinding? = null
        var profileMoreDetailsBinding: JobDetailsWithPointsItemBinding? = null
        var profileDividerBinding: DividerItemBinding? = null

        constructor(binding: ProfileDetailsTopItemBinding) : super(binding.root) {
            profileDetailsTopItemBinding = binding
        }

        constructor(binding: ProfileDetailsItemBinding) : super(binding.root) {
            profileDetailsItemBinding = binding
        }

        constructor(binding: JobDetailsWithPointsItemBinding) : super(binding.root) {
            profileMoreDetailsBinding = binding
        }

        constructor(binding: DividerItemBinding) : super(binding.root) {
            profileDividerBinding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ProfileDetailsType.PROFILE.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.profile_details_top_item, parent, false)
                val binding = ProfileDetailsTopItemBinding.bind(view)
                ViewHolder(binding)
            }
            ProfileDetailsType.PROFILE_DETAILS.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.profile_details_item, parent, false)
                val binding = ProfileDetailsItemBinding.bind(view)
                ViewHolder(binding)
            }
            ProfileDetailsType.DIVIDER.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.divider_item, parent, false)
                val binding = DividerItemBinding.bind(view)
                ViewHolder(binding)
            }
            ProfileDetailsType.WORK_EXPERIENCE.value, ProfileDetailsType.EDUCATION_DETAILS.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.job_details_with_points_item, parent, false)
                val binding = JobDetailsWithPointsItemBinding.bind(view)
                ViewHolder(binding)
            }
            else -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.profile_details_item, parent, false)
                val binding = ProfileDetailsItemBinding.bind(view)
                ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = profileDetailsList[position]
        when (holder.itemViewType) {
            ProfileDetailsType.PROFILE.value -> {
                holder.profileDetailsTopItemBinding?.apply {
                    tvName.text = data.name
                    tvDepartment.text = data.department
                    tvDesignation.visibility= GONE

                    data.profilePic?.let { imgProfile.setImageResource(it) }
                }
            }
            ProfileDetailsType.DIVIDER.value -> {
                holder.profileDividerBinding?.apply {

                }
            }
            ProfileDetailsType.PROFILE_DETAILS.value -> {
                holder.profileDetailsItemBinding?.apply {
                    tvTitle.text = data.title
                    tvTitleSub.text = data.subTitle
                    tvTitle2.text = data.title2
                    tvTitleSub2.text = data.subTitle2
                    data.icon1?.let { imgIcon1.setImageResource(it) }
                    data.icon2?.let { imgIcon2.setImageResource(it) }
                    if (data.icon2 == null){
                        imgIcon2.visibility = GONE
                    }
                }
            }
            ProfileDetailsType.WORK_EXPERIENCE.value, ProfileDetailsType.EDUCATION_DETAILS.value -> {
                holder.profileMoreDetailsBinding?.apply {
                    tvTitle.text = data.title
                    recyclerPoints.adapter = profileDetailsList[position].profileMoreDetailsModel?.let {
                        ReferralProfileMoreDetailsAdapter(context,
                            it,data.refDetailsType)
                    }
                }
            }

        }

    }

    override fun getItemCount(): Int {
        return profileDetailsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return profileDetailsList[position].refDetailsType.value
    }
}