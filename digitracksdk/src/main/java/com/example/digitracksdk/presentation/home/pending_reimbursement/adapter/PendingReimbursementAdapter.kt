package com.example.digitracksdk.presentation.home.pending_reimbursement.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ItemReimbursementBinding
import com.example.digitracksdk.presentation.home.pending_reimbursement.model.PendingReimbursementModel

class PendingReimbursementAdapter(
    val context: Context, var list: ArrayList<PendingReimbursementModel>,
    var listener: PendingReimbursementClickManager, val from: Int = 0
) : RecyclerView.Adapter<PendingReimbursementAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemReimbursementBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_reimbursement, parent, false)
        val binding = ItemReimbursementBinding.bind(view)
        if (from == 1)
            binding.apply {
                icItem.visibility = GONE
                textAmount.visibility = GONE
                btnSelect.visibility = VISIBLE
                tvPaidStatus.visibility = VISIBLE
                tvVoucher.visibility = GONE
                tvPaidDate.visibility = VISIBLE
            }
        else
            binding.apply {
                tvVoucher.visibility = GONE
                tvPaidDate.visibility = GONE
                tvPaidStatus.visibility = GONE
                btnSelect.visibility = GONE
                tvApproverName.visibility= VISIBLE
                tvApprovedDate.visibility= VISIBLE
            }
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.binding.apply {
            if (data.isSelected) {
                btnSelect.setImageResource(R.drawable.ic_check)
                containerReimbursement.setCardForegroundColor(
                    ContextCompat.getColorStateList(
                        context,
                        R.color.blue_ribbon_10
                    )
                )
            } else {
                btnSelect.setImageResource(R.drawable.ic_uncheck)
                containerReimbursement.setCardForegroundColor(null)
            }
            textItem.text = "AttendanceDate   :${data.title}" //AttendanceDate
//            textItem.setPadding(0, 15, 0, 0)
            textDescription.text = "Day                         :${data.date}" //Day
            if (from == 1)
            {
                tvPaidStatus.text= "Punch In              :${data.inTime}"
                tvPaidDate.text="Status                   :${data.month}"

            }else{
                textAmount.text = data.amount//

                tvApproverName.apply {
                    text = context.getString(R.string.month) + " : " + data.month
                    setTextColor(ContextCompat.getColor(context, R.color.mine_shaft))
                }
                tvApprovedDate.apply {
                    text = context.getString(R.string.year) + " : " + data.year
                    setTextColor(ContextCompat.getColor(context, R.color.mine_shaft))
                }
            }

        }
        holder.itemView.setOnClickListener {
            listener.clickOnItem(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface PendingReimbursementClickManager {
        fun clickOnItem(position: Int)
        fun clickOnRemove(position: Int) {}
    }
}