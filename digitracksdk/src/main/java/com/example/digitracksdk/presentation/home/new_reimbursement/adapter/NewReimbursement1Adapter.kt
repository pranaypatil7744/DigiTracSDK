package com.example.digitracksdk.presentation.home.new_reimbursement.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.ItemReimbursementBinding
import com.example.digitracksdk.domain.model.new_reimbursement.LstReimbdetailsforVoucherGenModel
import com.example.digitracksdk.presentation.home.pending_reimbursement.adapter.PendingReimbursementAdapter
import com.example.digitracksdk.utils.AppUtils

class NewReimbursement1Adapter(
    val context: Context, var list: ArrayList<LstReimbdetailsforVoucherGenModel>,
    var listener: PendingReimbursementAdapter.PendingReimbursementClickManager
) : RecyclerView.Adapter<NewReimbursement1Adapter.ViewHolder>() {

    class ViewHolder(val binding: ItemReimbursementBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_reimbursement, parent, false)
        val binding = ItemReimbursementBinding.bind(view)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.binding.apply {
            setClickButton(holder,data)
//            if (data.isSelected) {
//                btnSelect.setImageResource(R.drawable.ic_check)
//                containerReimbursement.setCardForegroundColor(
//                    ContextCompat.getColorStateList(
//                        context,
//                        R.color.blue_ribbon_10
//                    )
//                )
//            } else {
//                btnSelect.setImageResource(R.drawable.ic_uncheck)
//                containerReimbursement.setCardForegroundColor(null)
//            }
            tvVoucher.visibility = GONE
            btnSelect.visibility = VISIBLE
            btnRemove.visibility = VISIBLE
            textItem.text = context.getString(R.string.category_) + data.ReimbursementCategory
            textItem.setPadding(0, 15, 0, 0)
            textDescription.text = context.getString(R.string.sub_category_) + data.ReimbursementSubCategory
            textAmount.text = context.getString(R.string.rupees_symbol) + data.GrossAmount
            tvPaidStatus.apply {
                text = context.getString(R.string.bill_number) + " : " + data.BillNo
                visibility = VISIBLE
                setTextColor(ContextCompat.getColor(context, R.color.mine_shaft))
            }
            tvPaidDate.apply {
                val date = AppUtils.INSTANCE?.convertDateFormat(
                    "MM/dd/yyyy HH:mm:ss",
                    data.BillDate.toString(),
                    "dd MMM yyyy"
                )
                text = context.getString(R.string.bill_date) + " : " + date
                visibility = VISIBLE
                setTextColor(ContextCompat.getColor(context, R.color.mine_shaft))
            }
            tvApproverName.apply {
                text = context.getString(R.string.from) + " : " + data.FromLocation
                visibility = if (data.FromLocation.isNullOrEmpty()) GONE else VISIBLE
                setTextColor(ContextCompat.getColor(context, R.color.mine_shaft))
            }
            tvApprovedDate.apply {
                text = context.getString(R.string.to) + " : " + data.ToLocation
                visibility = if (data.ToLocation.isNullOrEmpty()) GONE else VISIBLE
                setTextColor(ContextCompat.getColor(context, R.color.mine_shaft))
            }

            btnSelect.setOnClickListener {
//                list[position].isSelected = !list[position].isSelected
////                notifyItemChanged(position)
//                setClickButton(holder,list[position])

                listener.clickOnItem(position)
            }
            btnRemove.setOnClickListener {
                listener.clickOnRemove(position)
            }
        }
    }

    private fun setClickButton(holder: ViewHolder, data: LstReimbdetailsforVoucherGenModel) {
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
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}