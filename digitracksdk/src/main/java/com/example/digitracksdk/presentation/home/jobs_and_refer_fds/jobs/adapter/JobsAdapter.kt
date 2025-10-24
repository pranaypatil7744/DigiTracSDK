package com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.JobsItemBinding
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.LstOpenDemandForCandidatesItem

class JobsAdapter(val context: Context, private var jobsList: ArrayList<LstOpenDemandForCandidatesItem>, var listener: JobsClickManager) :
    RecyclerView.Adapter<JobsAdapter.ViewHolder>() {

    class ViewHolder(var binding: JobsItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.jobs_item, parent, false)
        val binding = JobsItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = jobsList[position]
        holder.binding.apply {
            tvTitle.text = data.designation
            tvSubTitle.text = data.locationName
            tvSalary.text = data.salaryRange
            imgProfile.setImageResource(R.drawable.info2)
//            ImageUtils.INSTANCE?.loadRemoteImage(imgProfile,data.documentPath)
            btnReferFriend.setOnClickListener {
                listener.onClickReferFriendBtn(position)
            }
            btnViewPolicy.setOnClickListener {
                listener.onClickViewPolicy(position)
            }

        }
    }

    override fun getItemCount(): Int {
        return jobsList.size
    }

    interface JobsClickManager {
        fun onClickReferFriendBtn(position: Int)
        fun onClickViewPolicy(position: Int)
    }
}