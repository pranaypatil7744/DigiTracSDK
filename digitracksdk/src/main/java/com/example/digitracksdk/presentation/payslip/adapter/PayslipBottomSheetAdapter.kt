package com.example.digitracksdk.presentation.payslip.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.BottomSheetPayslipDownloadBinding
import com.innov.digitrac.databinding.BottomSheetPayslipYearBinding
import com.example.digitracksdk.presentation.payslip.model.PayslipBottomSheetModel
import com.example.digitracksdk.presentation.payslip.model.YearOrMonthListModel
import com.example.digitracksdk.presentation.payslip.payslip_year.PayslipMonthAdapter
import com.example.digitracksdk.presentation.payslip.payslip_year.PayslipYearMonthAdapter


/**
 * Created by Mo. Khurseed Ansari on 27-August-2021,16:32
 */
class PayslipBottomSheetAdapter
    (
    val context: Context,
    var summaryList: ArrayList<PayslipBottomSheetModel>,
    private val monthList: ArrayList<YearOrMonthListModel>,
    private val yearOrMonthList: ArrayList<YearOrMonthListModel>,
    val listener: PaySlipClickManager,
    var selectedPositionYear: Int = 0,
    var selectedPositionMonth: Int = 0
) : RecyclerView.Adapter<PayslipBottomSheetAdapter.ViewHolder>() {

    lateinit var payslipYearMonthAdapter: PayslipYearMonthAdapter
    lateinit var payslipMonthAdapter: PayslipMonthAdapter

    enum class SummaryDetailsType(val value: Int) {
        PAYSLIP_YEAR(1),
        PAYSLIP_MONTH(2),
        PAYSLIP_DOWNLOAD(3),

    }

    class ViewHolder : RecyclerView.ViewHolder {
        var itemSheetPayslipYearBinding: BottomSheetPayslipYearBinding? = null

        constructor(binding: BottomSheetPayslipYearBinding) : super(binding.root) {
            itemSheetPayslipYearBinding = binding
        }

        var itemBottomSheetPayslipDownloadBinding: BottomSheetPayslipDownloadBinding? = null

        constructor(binding: BottomSheetPayslipDownloadBinding) : super(binding.root) {
            itemBottomSheetPayslipDownloadBinding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            SummaryDetailsType.PAYSLIP_YEAR.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.bottom_sheet_payslip_year, parent, false)
                val binding = BottomSheetPayslipYearBinding.bind(view)
                ViewHolder(binding)
            }
            SummaryDetailsType.PAYSLIP_MONTH.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.bottom_sheet_payslip_year, parent, false)
                val binding = BottomSheetPayslipYearBinding.bind(view)
                ViewHolder(binding)

            }

            else -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.bottom_sheet_payslip_download, parent, false)
                val binding = BottomSheetPayslipDownloadBinding.bind(view)
                ViewHolder(binding)
            }
        }


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = summaryList[position]
        when (holder.itemViewType) {
            SummaryDetailsType.PAYSLIP_YEAR.value -> {
                holder.itemSheetPayslipYearBinding?.apply {
                    tvPayslipYr.text = data.name

                    payslipYearMonthAdapter =
                        PayslipYearMonthAdapter(context, yearOrMonthList, listener)
                    holder.itemSheetPayslipYearBinding!!.recyclerPaySlipYear.adapter =
                        payslipYearMonthAdapter


                }
            }
            SummaryDetailsType.PAYSLIP_MONTH.value -> {
                holder.itemSheetPayslipYearBinding?.apply {
                    tvPayslipYr.text = data.name
                    payslipMonthAdapter = PayslipMonthAdapter(context, monthList, listener)
                    recyclerPaySlipYear.adapter = payslipMonthAdapter
                    recyclerPaySlipYear.layoutManager?.scrollToPosition(selectedPositionMonth)
//                    holder.itemSheetPayslipYearBinding?.recyclerPaySlipYear?.addOnScrollListener(object :
//                        RecyclerView.OnScrollListener() {
//                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                            super.onScrolled(recyclerView, dx, dy)
//                            val layoutManager = (recyclerView.layoutManager as LinearLayoutManager)
//                            val firstPos: Int = layoutManager.findFirstVisibleItemPosition()
//                            val lastPos: Int = layoutManager.findLastVisibleItemPosition()
//                            val middle = Math.abs(lastPos - firstPos) / 2 + firstPos
//
//                            var selectedPos = -1
//                            for (i in 0 until monthList.size) {
//                                if (i == middle) {
//                                    monthList[i].isSelected = true
//                                    selectedPos = i
//                                } else {
//                                    monthList[i].isSelected = false
//                                }
//                            }
//                            payslipMonthAdapter.notifyDataSetChanged()
//                        }
//                    })
                }
            }
            SummaryDetailsType.PAYSLIP_DOWNLOAD.value -> {
                holder.itemBottomSheetPayslipDownloadBinding?.apply {
                    tvPayslip.text = data.name
                    tvPayslipMonthName.text = data.month + context.getString(R.string._payslip_pdf)
                    textView11.text = data.month + " " + data.year
                }
                holder.itemView.setOnClickListener {
                    listener.onClickPaySlip(position)
                }
            }

        }

    }


    override fun getItemCount(): Int {
        return summaryList.size
    }

    override fun getItemViewType(position: Int): Int {
        return summaryList[position].summaryDetailsType.value
    }


    interface PaySlipClickManager {
        fun onYearClick(position: Int)
        fun onMonthClick(position: Int)
        fun onClickPaySlip(position: Int)
    }
}