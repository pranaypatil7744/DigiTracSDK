package com.example.digitracksdk.presentation.leaves.apply_leave.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.ItemApplyLeaveTitleBinding
import com.innov.digitrac.databinding.ItemLeavesHistoryBinding
import com.innov.digitrac.databinding.ItemMyLeavesDashboardBinding
import com.example.digitracksdk.presentation.leaves.apply_leave.model.ApplyLeavesModel
import com.example.digitracksdk.presentation.leaves.apply_leave.model.LeavesStatus
import com.example.digitracksdk.presentation.leaves.apply_leave.model.LeavesType
import com.example.digitracksdk.presentation.leaves.apply_leave.model.MyLeaveDashboardModel

class ApplyLeavesAdapter(
    val context: Context,
    private val applyLeaveList: ArrayList<ApplyLeavesModel>,
    val myLeavesList: ArrayList<MyLeaveDashboardModel>,
    val listener: LeaveClickManager? = null
) :
    RecyclerView.Adapter<ApplyLeavesAdapter.ViewHolder>() {

    class ViewHolder : RecyclerView.ViewHolder {
        var itemApplyLeavesTitleBinding: ItemApplyLeaveTitleBinding? = null
        var itemLeavesHistoryBinding: ItemLeavesHistoryBinding? = null
        var itemMyLeavesDashboardBinding: ItemMyLeavesDashboardBinding? = null

        constructor(binding: ItemApplyLeaveTitleBinding) : super(binding.root) {
            itemApplyLeavesTitleBinding = binding
        }

        constructor(binding: ItemLeavesHistoryBinding) : super(binding.root) {
            itemLeavesHistoryBinding = binding
        }

        constructor(binding: ItemMyLeavesDashboardBinding) : super(binding.root) {
            itemMyLeavesDashboardBinding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            LeavesType.TITLE.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_apply_leave_title, parent, false)
                val binding = ItemApplyLeaveTitleBinding.bind(view)
                ViewHolder(binding)
            }
            LeavesType.MY_LEAVES.value , LeavesType.LEAVE_STATUS.value-> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_my_leaves_dashboard, parent, false)
                val binding = ItemMyLeavesDashboardBinding.bind(view)
                ViewHolder(binding)
            }
            LeavesType.LEAVES_HISTORY.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_leaves_history, parent, false)
                val binding = ItemLeavesHistoryBinding.bind(view)
                ViewHolder(binding)
            }
            else -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_apply_leave_title, parent, false)
                val binding = ItemApplyLeaveTitleBinding.bind(view)
                ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = applyLeaveList[position]
        if (holder.itemViewType == LeavesType.TITLE.value) {
            holder.itemApplyLeavesTitleBinding?.apply {
                tvTitle.text = data.leaveMsg
            }
        } else if (holder.itemViewType == LeavesType.MY_LEAVES.value) {
            holder.itemMyLeavesDashboardBinding?.apply {
                val myLeavesAdapter = MyLeavesAdapter(context, myLeavesList)
                recyclerMyLeavesDashboard.adapter = myLeavesAdapter
            }
        } else if (holder.itemViewType == LeavesType.LEAVE_STATUS.value) {
            holder.itemMyLeavesDashboardBinding?.apply {
                val myLeavesAdapter = MyLeavesAdapter(context, data?.myLeaveDashboardList?:arrayListOf(),isClickable = true,listener = listener)
                recyclerMyLeavesDashboard.adapter = myLeavesAdapter
            }
        } else if (holder.itemViewType == LeavesType.LEAVES_HISTORY.value) {
            holder.itemLeavesHistoryBinding?.apply {
                tvLeaveMsg.text = data.leaveMsg
                tvLeaveTitle.text = data.leaveTitle
                tvDate.text = data.startDate
                tvTotalDays.text = data.totalDays
                when (data.leaveStatus) {
                    LeavesStatus.APPROVED -> {
                        tvRequestStatus.apply {
                            text = context.getString(R.string.approved)
                            setTextColor(ContextCompat.getColor(context, R.color.mountain_meadow))
                            backgroundTintList =
                                ContextCompat.getColorStateList(context, R.color.mountain_meadow_15)
                        }
                    }
                    LeavesStatus.REJECTED -> {
                        tvRequestStatus.apply {
                            text = context.getString(R.string.rejected)
                            setTextColor(ContextCompat.getColor(context, R.color.alizarin_crimson))
                            backgroundTintList =
                                ContextCompat.getColorStateList(context, R.color.alizarin_crimson_15)
                        }
                    }
                    LeavesStatus.PENDING -> {
                        tvRequestStatus.apply {
                            text = context.getString(R.string.pending)
                            setTextColor(ContextCompat.getColor(context, R.color.neon_carrot))
                            backgroundTintList =
                                ContextCompat.getColorStateList(context, R.color.neon_carrot_15)
                        }
                    }

                    else -> {

                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return applyLeaveList.size
    }

    override fun getItemViewType(position: Int): Int {
        return applyLeaveList[position].leavesDisplayType.value
    }

    interface LeaveClickManager{
        fun onItemClick(position: Int,type:String)
    }
}