package com.example.digitracksdk.presentation.home.help_and_support.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.HelpSupportItemTitleBinding
import com.innov.digitrac.databinding.HelpSupportItemsBinding
import com.example.digitracksdk.presentation.home.help_and_support.model.HelpAndSupportModel
import com.example.digitracksdk.presentation.home.help_and_support.model.HelpSupportItemType
import com.example.digitracksdk.presentation.home.help_and_support.model.HelpSupportStatus

class HelpAndSupportAdapter(
    val context: Context,
    private var helpSupportList: ArrayList<HelpAndSupportModel>,
    var listener: HelpClickManager
) : RecyclerView.Adapter<HelpAndSupportAdapter.ViewHolder>() {

    class ViewHolder : RecyclerView.ViewHolder {
        var helpSupportItemTitleBinding: HelpSupportItemTitleBinding? = null
        var helpSupportItemsBinding: HelpSupportItemsBinding? = null

        constructor(binding: HelpSupportItemTitleBinding) : super(binding.root) {
            helpSupportItemTitleBinding = binding
        }

        constructor(binding: HelpSupportItemsBinding) : super(binding.root) {
            helpSupportItemsBinding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            HelpSupportItemType.TITLE.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.help_support_item_title, parent, false)
                val binding = HelpSupportItemTitleBinding.bind(view)
                ViewHolder(binding)
            }
            HelpSupportItemType.MAIN_ITEM.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.help_support_items, parent, false)
                val binding = HelpSupportItemsBinding.bind(view)
                ViewHolder(binding)
            }
            else -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.help_support_item_title, parent, false)
                val binding = HelpSupportItemTitleBinding.bind(view)
                ViewHolder(binding)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = helpSupportList[position]
        when (holder.itemViewType) {
            HelpSupportItemType.TITLE.value -> {
                holder.helpSupportItemTitleBinding?.apply {
                    tvDate.text = data.date
                }
            }
            HelpSupportItemType.MAIN_ITEM.value -> {
                holder.helpSupportItemsBinding?.apply {
                    tvType.text =
                        context.getString(R.string.issue_type) + data.helpSupportDetails?.issueType
                    tvRequestTitle.text = data.helpSupportDetails?.issueTitle
                    tvIssueDate.text = context.getString(R.string.ticket_raised_on) + data.helpSupportDetails?.issueDate
                    when (data.helpSupportDetails?.helpSupportStatus) {
                        HelpSupportStatus.CLOSE -> {
                            tvRequestStatus.apply {
                                text = context.getString(R.string.closed)
                                setTextColor(ContextCompat.getColor(context, R.color.jungle_green))
                                backgroundTintList = ContextCompat.getColorStateList(
                                    context,
                                    R.color.mountain_meadow_35
                                )
                            }
                            tvResolvedDate.visibility = GONE
//                            tvResolvedDate.text =
//                                context.getString(R.string.closed_on)+ " : " + data.helpSupportDetails?.resolvedDate
                        }
                        HelpSupportStatus.OPEN -> {
                            tvRequestStatus.apply {
                                text = context.getString(R.string.open)
                                setTextColor(ContextCompat.getColor(context, R.color.neon_carrot))
                                backgroundTintList =
                                    ContextCompat.getColorStateList(context, R.color.neon_carrot_35)
                            }
                            tvResolvedDate.visibility = GONE
                        }
                        else -> {}
                    }
                }
                holder.itemView.setOnClickListener {
                    listener.onItemClick(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return helpSupportList.size
    }

    override fun getItemViewType(position: Int): Int {
        return helpSupportList[position].helpSupportItemType.value
    }

    interface HelpClickManager {
        fun onItemClick(position: Int)
    }
}