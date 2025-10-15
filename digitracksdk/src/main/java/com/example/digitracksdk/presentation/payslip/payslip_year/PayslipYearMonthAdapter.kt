package com.example.digitracksdk.presentation.payslip.payslip_year

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.ItemPayslipYearBinding
import com.example.digitracksdk.presentation.payslip.adapter.PayslipBottomSheetAdapter
import com.example.digitracksdk.presentation.payslip.model.YearOrMonthListModel
import kotlin.collections.ArrayList


/**
 * Created by Mo. Khurseed Ansari on 30-August-2021,10:58
 */
class PayslipYearMonthAdapter
    (
    val context: Context,
    private val yearOrMonthList: ArrayList<YearOrMonthListModel>,
    val listener: PayslipBottomSheetAdapter.PaySlipClickManager

) : RecyclerView.Adapter<PayslipYearMonthAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemPayslipYearBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_payslip_year, parent, false)
        val binding = ItemPayslipYearBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = yearOrMonthList[position]

        holder.binding.apply {
            tvItemName.text = yearOrMonthList[position].itemName

            if (data.isSelected == true) {
                tvItemName.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    context.getResources().getDimension(R.dimen.dimen20)
                )
                tvItemName.setTextColor(Color.parseColor("#FFFFFF"))
                llMainYear.setBackgroundResource(R.drawable.bg_btn)
            } else {
                tvItemName.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    context.getResources().getDimension(R.dimen.dimen13)
                )
                tvItemName.setTextColor(Color.parseColor("#474747"))
                llMainYear.setBackgroundResource(R.drawable.bg_payslip_text)
            }

        }
        holder.itemView.setOnClickListener {
            listener.onYearClick(position)
        }
    }

    override fun getItemCount(): Int {

        Log.e("TAG", "size" + yearOrMonthList.size)
        return yearOrMonthList.size
    }

}