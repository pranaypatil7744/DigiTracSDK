package com.example.digitracksdk.presentation.home.reimbursements.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.IssueDetailsTwoLineItemBinding
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementDetailsListModel

class ReimbursementDetailsAdapter(
    val context: Context,
    private val detailsList: ArrayList<ReimbursementDetailsListModel>
) : RecyclerView.Adapter<ReimbursementDetailsAdapter.ViewHolder>() {

    class ViewHolder(val binding: IssueDetailsTwoLineItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.issue_details_two_line_item, parent, false)
        val binding = IssueDetailsTwoLineItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = detailsList[position]
        holder.binding.apply {
            tvTitle.text = data.title
            tvSubTitle.text = data.subTitle
        }
    }

    override fun getItemCount(): Int {
        return detailsList.size
    }
}