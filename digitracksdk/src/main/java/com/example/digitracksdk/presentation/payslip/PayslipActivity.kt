package com.example.digitracksdk.presentation.payslip

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.domain.model.pay_slip.ListYearMonthName
import com.example.digitracksdk.domain.model.pay_slip.PaySlipDownloadRequestModelNew
import com.example.digitracksdk.domain.model.pay_slip.PaySlipMonthYearRequestModel
import com.example.digitracksdk.domain.model.pay_slip.SalaryReleaseStatusRequestModel
import com.example.digitracksdk.Constant
import com.example.digitracksdk.domain.model.pay_slip.PaySlipDownloadRequestModel
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityPayslipBinding
import com.innov.digitrac.domain.model.pay_slip.*
import com.example.digitracksdk.presentation.payslip.adapter.PayslipBottomSheetAdapter
import com.example.digitracksdk.presentation.payslip.model.PayslipBottomSheetModel
import com.example.digitracksdk.presentation.payslip.model.YearOrMonthListModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.ImageUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class PayslipActivity : BaseActivity(),
    PayslipBottomSheetAdapter.PaySlipClickManager {
    lateinit var binding: ActivityPayslipBinding
    private val paySlipViewModel: PaySlipViewModel by viewModel()
    lateinit var preferenceUtils: PreferenceUtils
    var innovId: String = ""
    var gnetAssociateId: String = ""
    private lateinit var payslipBottomSheetAdapter: PayslipBottomSheetAdapter
    private val monthYearList: ArrayList<ListYearMonthName> = ArrayList()
    private var summaryList: ArrayList<PayslipBottomSheetModel> = ArrayList()
    private val monthList: ArrayList<YearOrMonthListModel> = ArrayList()
    private val yearList: ArrayList<YearOrMonthListModel> = ArrayList()
    var selectedMonth: String = ""
    var selectedYear: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPayslipBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        preferenceUtils = PreferenceUtils(this)
        getPreferenceData()
        setUpToolbar()
        setUpSummaryAdapter()
        setUpObserver()
        callYearMonthApi()
        setUpListener()
    }

    private fun setUpObserver() {
        binding.apply {
            with(paySlipViewModel) {
                paySlipMonthYearsResponseData.observe(this@PayslipActivity
                ) { it ->
                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        if (!it.lstYearMonthName.isNullOrEmpty()) {
                            binding.layoutNoData.root.visibility = GONE
                            monthYearList.clear()
                            monthYearList.addAll(it.lstYearMonthName ?: arrayListOf())
                            yearList.clear()
                            for (i in monthYearList) {
                                yearList.add(
                                    YearOrMonthListModel(
                                        i.Year
                                    )
                                )
                            }
                            setUpViewData()
                            payslipBottomSheetAdapter.notifyDataSetChanged()
                        } else {
                            binding.layoutNoData.apply {
                                root.visibility = VISIBLE
                                tvNoData.text = getString(R.string.no_data_found)
                            }
                        }

                    } else {
                        showToast(getString(R.string.something_went_wrong))
                    }
                }

                paySlipDownloadResponseData.observe(this@PayslipActivity
                ) { it ->
                    toggleLoader(false)
                    if (it.FileData != null) {
                        val file = it.FileData.let { it1 ->
                            ImageUtils.INSTANCE?.writePDFToFile(
                                it1,
                                "${getString(R.string.payslip_)}_${it.Month}_${it.Year}"
                            )
                        }
                        ImageUtils.INSTANCE?.openPdfFile(
                            this@PayslipActivity,
                            file?.absolutePath.toString()
                        )
                        showToast(getString(R.string.pay_slip_download_successfully))
                    } else {
                        showToast(it.Status)
                    }
                }

                paySlipDownloadNewResponseData.observe(this@PayslipActivity) {
                    toggleLoader(false)
                    if (it.Payslip != null) {
                        val file = ImageUtils.INSTANCE?.writePDFToFile(
                            it.Payslip.toString(),
                            "${getString(R.string.payslip_)}_${selectedMonth}_${selectedYear}"
                        )
                        ImageUtils.INSTANCE?.openPdfFile(
                            this@PayslipActivity,
                            filePath = file?.absolutePath.toString()
                        )
                        showToast(getString(R.string.pay_slip_download_successfully))
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                salaryReleaseStatusResponseData.observe(this@PayslipActivity
                ) {
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        callPaySlipDownloadApiNew()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(this@PayslipActivity) { t ->
                    toggleLoader(false)
                    showToast(t)
                }
            }
        }
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callYearMonthApi()
            }
        }
    }

    private fun getPreferenceData() {
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
        gnetAssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
    }

    private fun callYearMonthApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            binding.layoutNoInternet.root.visibility = GONE
            paySlipViewModel.callPaySlipMonthYearApi(requestModel())
        } else {
            binding.layoutNoInternet.root.visibility = VISIBLE
        }
    }

    private fun requestModel(): PaySlipMonthYearRequestModel {

        val  result = PaySlipMonthYearRequestModel()
        result.InnovID = innovId
        return result

    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.download_payslip)
            divider.visibility = VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun setUpViewData() {
        summaryList.clear()
        summaryList.add(
            PayslipBottomSheetModel(
                name = getString(R.string.select_a_year),
                summaryDetailsType = PayslipBottomSheetAdapter.SummaryDetailsType.PAYSLIP_YEAR
            )
        )
        summaryList.add(
            PayslipBottomSheetModel(
                name = getString(R.string.select_a_month),
                summaryDetailsType = PayslipBottomSheetAdapter.SummaryDetailsType.PAYSLIP_MONTH
            )
        )
        summaryList.add(
            PayslipBottomSheetModel(
                name = getString(R.string.payslips),
                summaryDetailsType = PayslipBottomSheetAdapter.SummaryDetailsType.PAYSLIP_DOWNLOAD
            )
        )
    }

    private fun setUpSummaryAdapter() {
        payslipBottomSheetAdapter =
            PayslipBottomSheetAdapter(
                this.applicationContext,
                summaryList,
                monthList,
                yearList,
                this
            )
        binding.recyclerPayslip.adapter = payslipBottomSheetAdapter
    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.root,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    override fun onYearClick(position: Int) {
        selectedYear = yearList[position].itemName.toString()
        selectedMonth = ""
        for (i in yearList) {
            i.isSelected = false
        }
        yearList[position].isSelected = true
        monthList.clear()

        val data = monthYearList[position]

        val currentYear = AppUtils.INSTANCE?.getCurrentYear()
        val currentMonth = AppUtils.INSTANCE?.getCurrentMonthText()?.uppercase(Locale.ROOT)
        if (selectedYear == currentYear.toString()){
            for (i in data.lstMonthName ?: arrayListOf()) {
                val month = AppUtils.INSTANCE?.getFullMonthName(this, i)
                if (i != currentMonth){
                    monthList.add(YearOrMonthListModel(month))
                }else{
                    break
                }
            }
        }else{
            for (i in data.lstMonthName ?: arrayListOf()) {
                val month = AppUtils.INSTANCE?.getFullMonthName(this, i)
                monthList.add(YearOrMonthListModel(month))
            }
        }
        payslipBottomSheetAdapter.notifyDataSetChanged()
    }

    override fun onMonthClick(position: Int) {
        for (i in monthList) {
            i.isSelected = false
        }
        monthList[position].isSelected = true
        selectedMonth = monthList[position].itemName.toString()
        summaryList[2].month = selectedMonth
        summaryList[2].year = selectedYear
        payslipBottomSheetAdapter.selectedPositionMonth = position
        payslipBottomSheetAdapter.notifyDataSetChanged()
    }

    override fun onClickPaySlip(position: Int) {
        if (selectedYear.isEmpty()) {
            showToast(getString(R.string.please_select_year))
        } else if (selectedMonth.isEmpty()) {
            showToast(getString(R.string.please_select_month))
        } else {
//            callSalaryReleaseStatusApi()
            callPaySlipDownloadApiNew()
        }
    }

    private fun callPaySlipDownloadApiNew() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                paySlipViewModel.callPaySlipDownloadNewApi(
                    request = PaySlipDownloadRequestModelNew(
                        gnetAssociateId,
                        selectedMonth,
                        selectedYear
                    )
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    private fun callPaySlipDownloadApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            paySlipViewModel.callPaySlipDownloadApi(
                request = PaySlipDownloadRequestModel(
                    innovId,
                    selectedMonth,
                    selectedYear
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun callSalaryReleaseStatusApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            paySlipViewModel.callSalaryReleaseStatusApi(
                request = SalaryReleaseStatusRequestModel(
                    GNETAssociateID = gnetAssociateId, selectedMonth, selectedYear
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

}