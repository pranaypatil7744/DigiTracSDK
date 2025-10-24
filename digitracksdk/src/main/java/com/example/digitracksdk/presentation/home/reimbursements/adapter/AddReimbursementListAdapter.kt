package com.example.digitracksdk.presentation.home.reimbursements.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ItemReimbursementBinding
import com.example.digitracksdk.domain.model.reimbursement_model.AssociateReimbursementDetailListModel

class AddReimbursementListAdapter(
    var context: Context,
    private val reimbursementList: ArrayList<AssociateReimbursementDetailListModel>,
    var listener: ReimbursementListClickManager
) : RecyclerView.Adapter<AddReimbursementListAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemReimbursementBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_reimbursement, parent, false)
        val binding = ItemReimbursementBinding.bind(view)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = reimbursementList[position]
        holder.binding.apply {
            tvVoucher.visibility = GONE
            textAmount.text = if (data.GrossAmount.toString()
                    .isEmpty()
            ) context.getString(R.string.rupees_symbol) + data.Amount
            else context.getString(R.string.rupees_symbol) + data.GrossAmount
            textItem.text = data.reimbursementCategoryName
            textItem.setPadding(0, 15, 0, 0)
            textDescription.text = data.BillDate
            btnSelect.visibility = VISIBLE
            btnSelect.setImageResource(R.drawable.ic_delete)
            btnSelect.setOnClickListener {
                listener.clickOnDeleteBtn(position)
            }
        }
        holder.itemView.setOnClickListener {
            listener.clickOnItem(position)
        }
    }

    override fun getItemCount(): Int {
        return reimbursementList.size
    }

    interface ReimbursementListClickManager {
        fun clickOnDeleteBtn(position: Int)
        fun clickOnItem(position: Int)
    }
}