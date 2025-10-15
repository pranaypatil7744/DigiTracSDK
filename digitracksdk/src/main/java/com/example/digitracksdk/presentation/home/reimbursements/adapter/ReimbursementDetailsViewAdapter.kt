package com.example.digitracksdk.presentation.home.reimbursements.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.ReimbursementDetailsViewItemBinding
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimburseDetailsModel
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementDetailsListModel

class ReimbursementDetailsViewAdapter(
    val context: Context,
    private val detailsList: ArrayList<ReimburseDetailsModel>,
    var listener: ReimbursementDetailsListAdapter.ReimbursementClickManager,
    var isRejected:Boolean
) :
    RecyclerView.Adapter<ReimbursementDetailsViewAdapter.ViewHolder>() {

    class ViewHolder(val binding: ReimbursementDetailsViewItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.reimbursement_details_view_item, parent, false)
        val binding = ReimbursementDetailsViewItemBinding.bind(view)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val list: ArrayList<ReimbursementDetailsListModel> = ArrayList()
            list.clear()
            for (i in detailsList[0].reimbursementDetailsList ?: arrayListOf()) {
                list.add(
                    ReimbursementDetailsListModel(
                        title = i.title,
                        subTitle = i.subTitle,
                        title2 = i.title2,
                        subTitle2 = i.subTitle2,
                        associateId = i.associateId,
                        reimbursementDetailsType = i.reimbursementDetailsType
                    )
                )
            }
            val adapter = ReimbursementDetailsListAdapter(context, list, listener,isRejected)
            recyclerReimbursementList.adapter = adapter
        }
    }

    override fun getItemCount(): Int {
        return detailsList.size
    }
}