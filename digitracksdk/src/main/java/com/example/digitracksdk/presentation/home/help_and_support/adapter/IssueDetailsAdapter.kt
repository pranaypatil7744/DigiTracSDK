package com.example.digitracksdk.presentation.home.help_and_support.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.DividerItemBinding
import com.innov.digitrac.databinding.IssueDetailsMultiLineItemBinding
import com.innov.digitrac.databinding.IssueDetailsSingleLineItemBinding
import com.innov.digitrac.databinding.IssueDetailsTwoLineItemBinding
import com.example.digitracksdk.presentation.home.help_and_support.model.HelpSupportStatus
import com.example.digitracksdk.presentation.home.help_and_support.model.IssueDetailsModel
import com.example.digitracksdk.presentation.home.help_and_support.model.IssueDetailsType

class IssueDetailsAdapter(val context: Context, private val issueDetailsList:ArrayList<IssueDetailsModel>):RecyclerView.Adapter<IssueDetailsAdapter.ViewHolder>(){

    class ViewHolder : RecyclerView.ViewHolder{
        var dividerItemBinding:DividerItemBinding? = null
        var issueDetailsSingleLineItemBinding:IssueDetailsSingleLineItemBinding? = null
        var issueDetailsTwoLineItemBinding: IssueDetailsTwoLineItemBinding? = null
        var issueDetailsMultiLineItemBinding: IssueDetailsMultiLineItemBinding? = null
        constructor(binding: DividerItemBinding):super(binding.root){
            dividerItemBinding = binding
        }
        constructor(binding: IssueDetailsTwoLineItemBinding):super(binding.root){
            issueDetailsTwoLineItemBinding = binding
        }
        constructor(binding: IssueDetailsSingleLineItemBinding):super(binding.root){
            issueDetailsSingleLineItemBinding = binding
        }
        constructor(binding: IssueDetailsMultiLineItemBinding):super(binding.root){
            issueDetailsMultiLineItemBinding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType){
            IssueDetailsType.DIVIDER.value -> {
                val view = LayoutInflater.from(context).inflate(R.layout.divider_item,parent,false)
                val binding = DividerItemBinding.bind(view)
                ViewHolder(binding)
            }
            IssueDetailsType.SINGLE_LINE.value -> {
                val view = LayoutInflater.from(context).inflate(R.layout.issue_details_single_line_item,parent,false)
                val binding = IssueDetailsSingleLineItemBinding.bind(view)
                ViewHolder(binding)
            }
            IssueDetailsType.TWO_LINE.value -> {
                val view = LayoutInflater.from(context).inflate(R.layout.issue_details_two_line_item,parent,false)
                val binding = IssueDetailsTwoLineItemBinding.bind(view)
                ViewHolder(binding)
            }
            IssueDetailsType.MULTI_LINE.value -> {
                val view = LayoutInflater.from(context).inflate(R.layout.issue_details_multi_line_item,parent,false)
                val binding = IssueDetailsMultiLineItemBinding.bind(view)
                ViewHolder(binding)
            }
            else -> {
                val view = LayoutInflater.from(context).inflate(R.layout.divider_item,parent,false)
                val binding = DividerItemBinding.bind(view)
                ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = issueDetailsList[position]
        when(holder.itemViewType){
            IssueDetailsType.SINGLE_LINE.value -> {
                holder.issueDetailsSingleLineItemBinding?.apply {
                    tvTitle.text = data.title
                    tvSubTitle.text = data.subTitle
                    if (data.issueStatus != null){
                        tvIssueStatus.visibility = VISIBLE
                        if (data.issueStatus == HelpSupportStatus.CLOSE){
                            tvIssueStatus.apply {
                                text = context.getString(R.string.closed)
                                setTextColor(ContextCompat.getColor(context, R.color.jungle_green))
                                backgroundTintList = ContextCompat.getColorStateList(
                                    context,
                                    R.color.mountain_meadow_35
                                )
                            }
                        }else{
                            tvIssueStatus.apply {
                                text = context.getString(R.string.open)
                                setTextColor(ContextCompat.getColor(context, R.color.neon_carrot))
                                backgroundTintList =
                                    ContextCompat.getColorStateList(context, R.color.neon_carrot_35)
                            }
                        }
                    }else{
                        tvIssueStatus.visibility = GONE
                    }

                }
            }
            IssueDetailsType.TWO_LINE.value->{
                holder.issueDetailsTwoLineItemBinding?.apply {
                    tvTitle.text = data.title
                    tvSubTitle.text = data.subTitle
                }
            }
            IssueDetailsType.MULTI_LINE.value->{
                holder.issueDetailsMultiLineItemBinding?.apply {
                    tvTitle.text = data.title
                    tvSubTitle.text = data.subTitle
                    tvTitle2.text = data.title2
                    tvSubTitle2.text = data.subTitle2
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return issueDetailsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return issueDetailsList[position].issueDetailsType.value
    }
}