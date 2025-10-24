package com.example.digitracksdk.presentation.home.reimbursements.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ItemReimbursementBinding
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementCategoryType
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementModel
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementType

class ReimbursementAdapter(
    val context: Context,
    var list: ArrayList<ReimbursementModel>,
    var listener: ReimbursementClickManager
) : RecyclerView.Adapter<ReimbursementAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemReimbursementBinding) : RecyclerView.ViewHolder(binding.root) {

        var itemReimbursementBinding: ItemReimbursementBinding? = binding

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_reimbursement, parent, false)
        val binding = ItemReimbursementBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.itemReimbursementBinding?.apply {
            val reimbursementType = data.type
            val categoryType = data.category
            textItem.text = data.title
            textDescription.text = data.description
            textAmount.text = data.amount
            tvVoucher.text = data.voucherNo
            tvPaidStatus.text = data.paidStatus
            tvPaidDate.text = data.paidDate
            icItem.apply {
                setImageResource(
                    when (categoryType.value) {
                        ReimbursementCategoryType.PUBLIC_TRANSPORT.value -> {
                            R.drawable.ic_public_transport

                        }
                        ReimbursementCategoryType.SELF_VEHICLE.value -> {
                            R.drawable.ic_self_vehicle
                        }
                        ReimbursementCategoryType.FOOD.value -> {
                            R.drawable.ic_food
                        }
//                    ReimbursementCategoryType.LODGING.value -> {
                        else -> {
                            R.drawable.ic_lodging
                        }
                    }
                )
            }
            when (reimbursementType.value) {
                ReimbursementType.APPROVED.value -> {
                    tvApproverName.apply {
                        text = data.approverName1
                        setTextColor(ContextCompat.getColor(context, R.color.jungle_green))
                        visibility = View.VISIBLE
                    }
                    tvApproverName2.apply {
                        text = data.approverName2
                        setTextColor(ContextCompat.getColor(context, R.color.jungle_green))
                        visibility = View.VISIBLE
                    }
                     tvApproverName3.apply {
                        text = data.approverName3
                        setTextColor(ContextCompat.getColor(context, R.color.jungle_green))
                        visibility = View.VISIBLE
                    }
                    tvApprovedDate.apply {
                        text = data.approvedDate1
                        setTextColor(ContextCompat.getColor(context, R.color.jungle_green))
                        visibility = View.VISIBLE
                    }
                    tvApprovedDate2.apply {
                        text = data.approvedDate2
                        setTextColor(ContextCompat.getColor(context, R.color.jungle_green))
                        visibility = View.VISIBLE
                    }
                    tvApprovedDate3.apply {
                        text = data.approvedDate3
                        setTextColor(ContextCompat.getColor(context, R.color.jungle_green))
                        visibility = View.VISIBLE
                    }
                    tvApprovalStatus.apply {
                        text = data.approvalStatus1
                        setTextColor(ContextCompat.getColor(context, R.color.jungle_green))
                        visibility = View.VISIBLE
                    }
                    tvApprovalStatus2.apply {
                        text = data.approvalStatus2
                        setTextColor(ContextCompat.getColor(context, R.color.jungle_green))
                        visibility = View.VISIBLE
                    }
                    tvApprovalStatus3.apply {
                        text = data.approvalStatus3
                        setTextColor(ContextCompat.getColor(context, R.color.jungle_green))
                        visibility = View.VISIBLE
                    }
                    tvApprovalRemarks.apply {
                        text = data.approvalRemark1
                        setTextColor(ContextCompat.getColor(context, R.color.jungle_green))
                        visibility = View.VISIBLE
                    }
                    tvApprovalRemarks2.apply {
                        text = data.approvalRemark2
                        setTextColor(ContextCompat.getColor(context, R.color.jungle_green))
                        visibility = View.VISIBLE
                    }
                    tvApprovalRemarks3.apply {
                        text = data.approvalRemark3
                        setTextColor(ContextCompat.getColor(context, R.color.jungle_green))
                        visibility = View.VISIBLE
                    }

                }
                ReimbursementType.REJECTED.value -> {
                    tvApproverName.apply {
                        text = data.approverName1
                        setTextColor(ContextCompat.getColor(context, R.color.rejected_color))
                        visibility = View.VISIBLE
                    }

                    tvApproverName2.apply {
                        text = data.approverName2
                        setTextColor(ContextCompat.getColor(context, R.color.rejected_color))
                        visibility = View.VISIBLE
                    }
                     tvApproverName3.apply {
                        text = data.approverName3
                        setTextColor(ContextCompat.getColor(context, R.color.rejected_color))
                        visibility = View.VISIBLE
                    }
                    tvApprovedDate.apply {
                        text = data.approvedDate1
                        setTextColor(ContextCompat.getColor(context, R.color.rejected_color))
                        visibility = View.VISIBLE
                    }
                    tvApprovedDate2.apply {
                        text = data.approvedDate2
                        setTextColor(ContextCompat.getColor(context, R.color.rejected_color))
                        visibility = View.VISIBLE
                    }
                    tvApprovedDate3.apply {
                        text = data.approvedDate3
                        setTextColor(ContextCompat.getColor(context, R.color.rejected_color))
                        visibility = View.VISIBLE
                    }
                    tvApprovalStatus.apply {
                        text = data.approvalStatus1
                        setTextColor(ContextCompat.getColor(context, R.color.rejected_color))
                        visibility = View.VISIBLE
                    }
                    tvApprovalStatus2.apply {
                        text = data.approvalStatus2
                        setTextColor(ContextCompat.getColor(context, R.color.rejected_color))
                        visibility = View.VISIBLE
                    }
                    tvApprovalStatus3.apply {
                        text = data.approvalStatus3
                        setTextColor(ContextCompat.getColor(context, R.color.rejected_color))
                        visibility = View.VISIBLE
                    }
                    tvApprovalRemarks.apply {
                        text = data.approvalRemark1
                        setTextColor(ContextCompat.getColor(context, R.color.rejected_color))
                        visibility = View.VISIBLE
                    }
                    tvApprovalRemarks2.apply {
                        text = data.approvalRemark2
                        setTextColor(ContextCompat.getColor(context, R.color.rejected_color))
                        visibility = View.VISIBLE
                    }
                    tvApprovalRemarks3.apply {
                        text = data.approvalRemark3
                        setTextColor(ContextCompat.getColor(context, R.color.rejected_color))
                        visibility = View.VISIBLE
                    }

                }
                else -> {
                    tvApproverName.apply {
                        visibility = View.GONE
                    }

                }
            }
        }
        holder.itemView.setOnClickListener {
            listener.onItemClick(
                position,
                data.AssociateReimbursementId.toString(),
                data.createdDate ?: ""
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ReimbursementClickManager {
        fun onItemClick(position: Int, associateReimbursementId: String, createdDate: String)
    }


}