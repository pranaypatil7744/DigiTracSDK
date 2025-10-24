package com.example.digitracksdk.presentation.home.jobs_and_refer_fds.job_details_screen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.databinding.JobDetailsBulletPointsItemBinding
import com.example.digitracksdk.R

class JobBulletPointsAdapter(val context: Context, var pointList: ArrayList<String>) :
    RecyclerView.Adapter<JobBulletPointsAdapter.ViewHolder>() {

    class ViewHolder(val binding: JobDetailsBulletPointsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.job_details_bullet_points_item,parent,false)
        val  binding = JobDetailsBulletPointsItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = pointList[position]
        holder.binding.apply {
            tvPoint.text = data
        }
    }

    override fun getItemCount(): Int {
        return pointList.size
    }
}