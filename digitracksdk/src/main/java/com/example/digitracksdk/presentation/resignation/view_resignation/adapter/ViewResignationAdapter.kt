package com.example.digitracksdk.presentation.resignation.view_resignation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.ItemResignationViewBinding
import com.example.digitracksdk.domain.model.resignation.ResignationsListModel

class ViewResignationAdapter(
    private val context: Context,
    private val resignationViewList: ArrayList<ResignationsListModel>,
    private var listener: RevokeClickManager
) : RecyclerView.Adapter<ViewResignationAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemResignationViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_resignation_view, parent, false)
        val binding = ItemResignationViewBinding.bind(v)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = resignationViewList[position]
        holder.binding.apply {
            tvType.text =
                context.getString(R.string.resignation_category) + " : " + data.ResignationCategory
            tvDateOfResignationValue.text = " : " + data.DateOfResignation
            tvPreferredLWDValue.text = " : " + data.PreferredLWD
            tvExpectedLastWrkDateValue.text = " : " + data.ExpectedLastWorkingDate
            tvRMApprovedLwdValue.text = " : " + data.RMApprovedLWD
            tvActionDateValue.text = " : " + data.ActionDate
            tvActionByValue.text = " : " + data.ActionBy
            tvRemarkValue.text = " : " + data.Remarks
            btnRevokeResignation.setOnClickListener {
                listener.onClickRevokeBtn(position)
            }

            when (data.ActionStatus) {
                Constant.Approved -> {
                    tvRequestStatus.apply {
                        text = context.getString(R.string.approved)
                        setTextColor(ContextCompat.getColor(context, R.color.jungle_green))
                        backgroundTintList = ContextCompat.getColorStateList(
                            context,
                            R.color.mountain_meadow_35
                        )
                    }
                }

                Constant.Rejected -> {
                    tvRequestStatus.apply {
                        text = context.getString(R.string.rejected)
                        setTextColor(ContextCompat.getColor(context, R.color.alizarin_crimson))
                        backgroundTintList =
                            ContextCompat.getColorStateList(context, R.color.alizarin_crimson_15)
                    }
                }

                else -> {
                    tvRequestStatus.apply {
                        text = context.getString(R.string.pending)
                        setTextColor(ContextCompat.getColor(context, R.color.neon_carrot))
                        backgroundTintList =
                            ContextCompat.getColorStateList(context, R.color.neon_carrot_35)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return resignationViewList.size
    }

    interface RevokeClickManager {
        fun onClickRevokeBtn(position: Int)
    }
}