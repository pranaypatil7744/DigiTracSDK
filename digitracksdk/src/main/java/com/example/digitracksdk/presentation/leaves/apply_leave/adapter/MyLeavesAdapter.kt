package com.example.digitracksdk.presentation.leaves.apply_leave.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.ItemMyLeavesBinding
import com.example.digitracksdk.presentation.leaves.apply_leave.model.MyLeaveDashboardModel

class MyLeavesAdapter(
    val context: Context,
    private val myLeaveList: ArrayList<MyLeaveDashboardModel>,
    val isClickable: Boolean = false,
    var listener: ApplyLeavesAdapter.LeaveClickManager? = null
) :
    RecyclerView.Adapter<MyLeavesAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemMyLeavesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_my_leaves, parent, false)
        val binding = ItemMyLeavesBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = myLeaveList[position]
        holder.binding.apply {
            tvLeavesCounts.text = data.remainingLeaves
            tvLeaveTitle.text = data.leaveMsg
            data.leavesIcon?.let { imgIcon1.setImageResource(it) }
        }
        if (isClickable) {
            holder.itemView.setOnClickListener {
                listener?.onItemClick(position,data.leaveMsg.toString())
            }
        }
    }

    override fun getItemCount(): Int {
        return myLeaveList.size
    }
}