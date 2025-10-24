package com.example.digitracksdk.presentation.attendance.mileage_tracking.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.MileageTrackingItemsBinding
import com.example.digitracksdk.presentation.attendance.mileage_tracking.model.AttendanceMileageTrackingModel
import com.example.digitracksdk.presentation.leaves.apply_leave.model.LeavesStatus

class MileageTrackingStatusAdapter(
    var context: Context,
    private val mileageTrackingList: ArrayList<AttendanceMileageTrackingModel>
) : RecyclerView.Adapter<MileageTrackingStatusAdapter.ViewHolder>() {

    class ViewHolder(val binding: MileageTrackingItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.mileage_tracking_items, parent, false)
        val binding = MileageTrackingItemsBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mileageTrackingList[position]
        holder.binding.apply {
            tvDate.text = data.date
            tvStartReadingValue.text = data.startReading ?: "-----"
            tvCloseReadingValue.text = data.closeReading ?: "-----"
            when (data.mileageTrackingStatus) {
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
                }
                LeavesStatus.PENDING -> {
                    tvRequestStatus.apply {
                        text = context.getString(R.string.pending)
                        setTextColor(ContextCompat.getColorStateList(context, R.color.neon_carrot))
                        backgroundTintList =
                            ContextCompat.getColorStateList(context, R.color.neon_carrot_35)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mileageTrackingList.size
    }
}