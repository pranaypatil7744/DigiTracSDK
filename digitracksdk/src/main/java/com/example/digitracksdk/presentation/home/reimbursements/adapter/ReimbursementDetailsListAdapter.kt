package com.example.digitracksdk.presentation.home.reimbursements.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.DividerItemBinding
import com.example.digitracksdk.databinding.IssueDetailsTwoLineItemBinding
import com.example.digitracksdk.databinding.ReimbursementDetailsMultiLineItemBinding
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementDetailsListModel
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementDetailsType

class ReimbursementDetailsListAdapter(
    val context: Context,
    private val detailsList: ArrayList<ReimbursementDetailsListModel>,
    var listener: ReimbursementClickManager,
    var isRejected:Boolean
) :
    RecyclerView.Adapter<ReimbursementDetailsListAdapter.ViewHolder>() {

    class ViewHolder : RecyclerView.ViewHolder {
        var reimbursementDetailsTwoLineItemBinding: IssueDetailsTwoLineItemBinding? = null
        var reimbursementDetailsMultiLineItemBinding: ReimbursementDetailsMultiLineItemBinding? = null
        var dividerItemBinding: DividerItemBinding? = null

        constructor(binding: IssueDetailsTwoLineItemBinding) : super(binding.root) {
            reimbursementDetailsTwoLineItemBinding = binding
        }

        constructor(binding: ReimbursementDetailsMultiLineItemBinding) : super(binding.root) {
            reimbursementDetailsMultiLineItemBinding = binding
        }

        constructor(binding: DividerItemBinding) : super(binding.root) {
            dividerItemBinding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ReimbursementDetailsType.TWO_LINE.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.issue_details_two_line_item, parent, false)
                val binding = IssueDetailsTwoLineItemBinding.bind(view)
                ViewHolder(binding)
            }
            ReimbursementDetailsType.MULTI_LINE.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.reimbursement_details_multi_line_item, parent, false)
                val binding = ReimbursementDetailsMultiLineItemBinding.bind(view)
                ViewHolder(binding)
            }
            ReimbursementDetailsType.DIVIDER.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.divider_item, parent, false)
                val binding = DividerItemBinding.bind(view)
                ViewHolder(binding)
            }
            else -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.issue_details_two_line_item, parent, false)
                val binding = IssueDetailsTwoLineItemBinding.bind(view)
                ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = detailsList[position]

        when(holder.itemViewType){
            ReimbursementDetailsType.TWO_LINE.value -> {
                holder.reimbursementDetailsTwoLineItemBinding?.apply {
                    tvTitle.text = data.title
                    tvSubTitle.text = data.subTitle
                    if (data.title ==context.getString(R.string.bill)){
                        imgDownload.visibility = VISIBLE
                        imgUpload.visibility = VISIBLE
                        imgDownload.setOnClickListener {
                            listener.clickOnBill(position,data.associateId.toString())
                        }
                        imgUpload.setOnClickListener {
                            listener.clickOnUploadBill(position,data.associateId.toString())
                        }
                        if (isRejected){
                            imgEdit.apply {
                                visibility = VISIBLE
                                setOnClickListener{
                                    listener.clickOnEdit(position,data.associateId.toString())
                                }
                            }
                        }else{
                            imgEdit.visibility = GONE
                        }
                    }else{
                        imgDownload.visibility = GONE
                        imgUpload.visibility = GONE
                    }
                }
            }
            ReimbursementDetailsType.MULTI_LINE.value -> {
                holder.reimbursementDetailsMultiLineItemBinding?.apply {
                    tvTitle.text = data.title
                    tvSubTitle.text = data.subTitle
                    tvTitle2.text = data.title2
                    tvSubTitle2.text = data.subTitle2
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return detailsList[position].reimbursementDetailsType.value
    }

    override fun getItemCount(): Int {
        return detailsList.size
    }

    interface ReimbursementClickManager{
        fun clickOnBill(position: Int,associateId:String)
        fun clickOnUploadBill(position: Int,associateId:String)
        fun clickOnEdit(position: Int,associateId: String)
    }

}