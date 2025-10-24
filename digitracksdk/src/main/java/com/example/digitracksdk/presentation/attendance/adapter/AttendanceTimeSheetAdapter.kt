package com.example.digitracksdk.presentation.attendance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.TimeSheetHolidayItemBinding
import com.example.digitracksdk.databinding.TimeSheetListItemsBinding
import com.example.digitracksdk.databinding.TimeSheetTopItemBinding
import com.example.digitracksdk.presentation.attendance.model.AttendanceTimeSheetModel
import com.example.digitracksdk.presentation.attendance.model.TimeSheetItemType

class AttendanceTimeSheetAdapter(
    val context: Context,
    var timeSheetList: ArrayList<AttendanceTimeSheetModel>
) : RecyclerView.Adapter<AttendanceTimeSheetAdapter.ViewHolder>() {

    class ViewHolder : RecyclerView.ViewHolder {

        var itemTimeSheetTopItemBinding: TimeSheetTopItemBinding? = null
        var itemTimeSheetHolidayItemBinding: TimeSheetHolidayItemBinding? = null
        var itemTimeSheetListItemsBinding: TimeSheetListItemsBinding? = null

        constructor(binding: TimeSheetTopItemBinding) : super(binding.root) {
            itemTimeSheetTopItemBinding = binding
        }

        constructor(binding: TimeSheetHolidayItemBinding) : super(binding.root) {
            itemTimeSheetHolidayItemBinding = binding
        }

        constructor(binding: TimeSheetListItemsBinding) : super(binding.root) {
            itemTimeSheetListItemsBinding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TimeSheetItemType.TIME_SHEET_TOP_ITEM.value -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.time_sheet_top_item, parent, false)
                val binding = TimeSheetTopItemBinding.bind(view)
                ViewHolder(binding)
            }

            TimeSheetItemType.TIME_SHEET_DAILY_ITEM.value -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.time_sheet_list_items, parent, false)
                val binding = TimeSheetListItemsBinding.bind(view)
                ViewHolder(binding)
            }

            TimeSheetItemType.TIME_SHEET_HOLIDAY_ITEM.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.time_sheet_holiday_item, parent, false)
                val binding = TimeSheetHolidayItemBinding.bind(view)
                ViewHolder(binding)
            }

            else -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.time_sheet_holiday_item, parent, false)
                val binding = TimeSheetHolidayItemBinding.bind(view)
                ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = timeSheetList[position]
        when (holder.itemViewType) {
            TimeSheetItemType.TIME_SHEET_TOP_ITEM.value -> {
                holder.itemTimeSheetTopItemBinding?.apply {
                    tvCurrentMonthYear.text = data.monthYear
//                    btnPrev.setOnClickListener {
//                        listener.clickOnPrevBtn(position)
//                    }
//                    btnNext.setOnClickListener {
//                        listener.clickOnNextBtn(position)
//                    }
                }
            }

            TimeSheetItemType.TIME_SHEET_DAILY_ITEM.value -> {
                holder.itemTimeSheetListItemsBinding?.apply {
                    recyclerHistoryList.adapter =
                        data.thisMonthHistory?.let { AttendanceTimeSheetListAdapter(context, it) }
                }
            }

            TimeSheetItemType.TIME_SHEET_HOLIDAY_ITEM.value -> {
                holder.itemTimeSheetHolidayItemBinding?.apply {
                    tvHoliday.text = data.monthYear
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return timeSheetList.size
    }

    override fun getItemViewType(position: Int): Int {
        return timeSheetList[position].timeSheetItemType.value
    }
}