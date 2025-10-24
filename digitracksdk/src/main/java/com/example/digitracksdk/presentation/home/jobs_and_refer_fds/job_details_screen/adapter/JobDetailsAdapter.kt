package com.example.digitracksdk.presentation.home.jobs_and_refer_fds.job_details_screen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.JobDetailsTopItemBinding
import com.example.digitracksdk.databinding.JobDetailsWithPointsItemBinding
import com.example.digitracksdk.databinding.JobDetailsWithoutPointsItemBinding
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.job_details_screen.model.JobDetailsModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.job_details_screen.model.JobDetailsType

class JobDetailsAdapter(
    val context: Context,
    private val jobDetailsList: ArrayList<JobDetailsModel>,
) : RecyclerView.Adapter<JobDetailsAdapter.ViewHolder>() {
    class ViewHolder : RecyclerView.ViewHolder {
        var jobDetailsTopItemBinding: JobDetailsTopItemBinding? = null
        var jobDetailsWithPointsItemBinding: JobDetailsWithPointsItemBinding? = null
        var jobDetailsWithoutPointsItemBinding: JobDetailsWithoutPointsItemBinding? = null

        constructor(binding: JobDetailsTopItemBinding) : super(binding.root) {
            jobDetailsTopItemBinding = binding
        }

        constructor(binding: JobDetailsWithPointsItemBinding) : super(binding.root) {
            jobDetailsWithPointsItemBinding = binding
        }

        constructor(binding: JobDetailsWithoutPointsItemBinding) : super(binding.root) {
            jobDetailsWithoutPointsItemBinding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            JobDetailsType.JOB_DETAILS_TOP.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.job_details_top_item, parent, false)
                val binding = JobDetailsTopItemBinding.bind(view)
                ViewHolder(binding)
            }
            JobDetailsType.JOB_DETAILS_WITHOUT_BULLETS.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.job_details_without_points_item, parent, false)
                val binding = JobDetailsWithoutPointsItemBinding.bind(view)
                ViewHolder(binding)
            }
            JobDetailsType.JOB_DETAILS_WITH_BULLETS.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.job_details_with_points_item, parent, false)
                val binding = JobDetailsWithPointsItemBinding.bind(view)
                ViewHolder(binding)
            }
            else -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.job_details_top_item, parent, false)
                val binding = JobDetailsTopItemBinding.bind(view)
                ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = jobDetailsList[position]
        when (holder.itemViewType) {
            JobDetailsType.JOB_DETAILS_TOP.value -> {
                holder.jobDetailsTopItemBinding?.apply {
                    tvTitle.text = data.title
                    tvSubTitle.text = data.subTitle
                    tvMinExpValue.text = data.min_exp+" "+context.getString(R.string.years)
                    tvLocationValue.text = data.location
                    data.profilePic?.let { imgProfile.setImageResource(it) }
                }
            }
            JobDetailsType.JOB_DETAILS_WITH_BULLETS.value -> {
                holder.jobDetailsWithPointsItemBinding?.apply {
                    tvTitle.text = data.title
                    recyclerPoints.adapter =
                        jobDetailsList[position].points?.let { JobBulletPointsAdapter(context, it) }
                }
            }
            JobDetailsType.JOB_DETAILS_WITHOUT_BULLETS.value -> {
                holder.jobDetailsWithoutPointsItemBinding?.apply {
                    tvTitle.text = data.title
                    tvDetails.text = data.subTitle
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return jobDetailsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return jobDetailsList[position].jobDetailsType.value
    }
}