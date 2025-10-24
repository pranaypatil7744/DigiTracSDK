package com.example.digitracksdk.presentation.attendance.attendance_regularization.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.AttendanceRegularizationItemBinding
import com.example.digitracksdk.presentation.attendance.attendance_regularization.model.AttendanceRegularizationModel
import com.example.digitracksdk.presentation.leaves.apply_leave.model.LeavesStatus

class AttendanceRegularizationAdapter(
    var context: Context,
    var regularizationList: ArrayList<AttendanceRegularizationModel>
) : RecyclerView.Adapter<AttendanceRegularizationAdapter.ViewHolder>() {

    class ViewHolder(val binding: AttendanceRegularizationItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.attendance_regularization_item, parent, false)
        val binding = AttendanceRegularizationItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = regularizationList[position]
        holder.binding.apply {
            tvRequestMsg.text = data.requestMsg
            tvRequestType.text = data.requestType
            tvRequestDate.text = data.requestDate
            when (data.requestStatus) {
                LeavesStatus.APPROVED -> {
                    tvRequestStatus.apply {
                        text = context.getString(R.string.approved)
                        setTextColor(
                            ContextCompat.getColorStateList(
                                context,
                                R.color.mountain_meadow
                            )
                        )
                        backgroundTintList =
                            ContextCompat.getColorStateList(context, R.color.mountain_meadow_35)

                    }
                    tvDateResponse.apply {
                        text = data.responseDate
                        visibility = VISIBLE
                        setTextColor(ContextCompat.getColor(context, R.color.mountain_meadow))
                    }
                }
                LeavesStatus.REJECTED -> {
                    tvRequestStatus.apply {
                        text = context.getString(R.string.rejected)
                        setTextColor(
                            ContextCompat.getColorStateList(
                                context,
                                R.color.alizarin_crimson
                            )
                        )
                        backgroundTintList =
                            ContextCompat.getColorStateList(context, R.color.alizarin_crimson_35)
                    }
                    tvDateResponse.apply {
                        text = data.responseDate
                        visibility = VISIBLE
                        setTextColor(ContextCompat.getColor(context, R.color.alizarin_crimson))
                    }
                }
                LeavesStatus.PENDING -> {
                    tvRequestStatus.apply {
                        text = context.getString(R.string.pending)
                        setTextColor(ContextCompat.getColorStateList(context, R.color.neon_carrot))
                        backgroundTintList =
                            ContextCompat.getColorStateList(context, R.color.neon_carrot_35)
                    }
                    tvDateResponse.visibility = GONE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return regularizationList.size
    }
}