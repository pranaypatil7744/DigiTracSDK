package com.example.digitracksdk.presentation.payslip.payslip_year

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.ItemPayslipMonthBinding
import com.example.digitracksdk.presentation.payslip.adapter.PayslipBottomSheetAdapter
import com.example.digitracksdk.presentation.payslip.model.YearOrMonthListModel


/**
 * Created by Mo. Khurseed Ansari on 30-August-2021,10:58
 */
class PayslipMonthAdapter
    (
    val context: Context,
    private val monthsList: ArrayList<YearOrMonthListModel>,
    val listener: PayslipBottomSheetAdapter.PaySlipClickManager

) : RecyclerView.Adapter<PayslipMonthAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemPayslipMonthBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_payslip_month, parent, false)
        val binding = ItemPayslipMonthBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = monthsList[position]
        holder.binding.apply {
            tvItemMonthName.text = data.itemName
            if (data.isSelected == true) {
                tvItemMonthName.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    context.resources.getDimension(R.dimen.dimen20)
                )
                tvItemMonthName.setTextColor(Color.parseColor("#FFFFFF"))
                llMainYear.setBackgroundResource(R.drawable.bg_btn)
            } else {
                tvItemMonthName.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    context.getResources().getDimension(R.dimen.dimen13)
                )
                tvItemMonthName.setTextColor(Color.parseColor("#474747"))
                llMainYear.setBackgroundResource(R.drawable.bg_payslip_text)
            }
        }
        holder.itemView.setOnClickListener {
            listener.onMonthClick(position)
        }
    }

    override fun getItemCount(): Int {

        Log.e("TAG", "size" + monthsList.size)
        return monthsList.size
    }

}