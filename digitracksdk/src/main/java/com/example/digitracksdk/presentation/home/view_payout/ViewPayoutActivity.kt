package com.example.digitracksdk.presentation.home.view_payout

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityViewPayoutBinding
import com.example.digitracksdk.domain.model.view_payout.LSTReimbDetail
import com.example.digitracksdk.domain.model.view_payout.ViewPayoutRequest
import com.example.digitracksdk.presentation.home.view_payout.adapter.ViewPayoutAdapter
import com.example.digitracksdk.presentation.home.view_payout.view_model.ViewPayoutViewModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class ViewPayoutActivity : BaseActivity() {
    lateinit var binding: ActivityViewPayoutBinding
    var selectedFromDate: Calendar? = null
    lateinit var preferenceUtils: PreferenceUtils


    private val viewPayoutViewModel: ViewPayoutViewModel by viewModel()

    lateinit var viewPayoutAdapter: ViewPayoutAdapter
    var gnetId: String = ""
    var selectedFrom = ""
    var selectedTo = ""
    private val payoutList: ArrayList<LSTReimbDetail> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityViewPayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        getPreferenceData()
        setUpObserver()
        setUpToolbar()
        setUpAdapter()
        callViewPayoutApi()
        setUpListener()
    }

    private fun setUpObserver() {
        binding.apply {
            with(viewPayoutViewModel) {
                viewPayoutResponseData.observe(this@ViewPayoutActivity) {
                    toggleLoader(false)
                    if (it.Status == Constant.success) {
                        if (it.LSTReimbDetails?.isNotEmpty() == true) {
                            showNoDataLayout(false)
                            payoutList.clear()
                            payoutList.addAll(it.LSTReimbDetails)
                            if (::viewPayoutAdapter.isInitialized) {
                                viewPayoutAdapter.notifyDataSetChanged()
                            }
                        } else {
                            showNoDataLayout(true)
                        }
                    } else {
                        showNoDataLayout(true)
                        showToast(it.Message)
                    }
                }

                messageData.observe(this@ViewPayoutActivity) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }
        }
    }

    private fun getPreferenceData() {
        binding.apply {
            gnetId = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
        }
    }

    private fun setUpListener() {
        binding.apply {
            btnApply.setOnClickListener {
                callViewPayoutApi()
            }
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callViewPayoutApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                callViewPayoutApi()
            }

            etFromDate.setOnClickListener {
                val cal = Calendar.getInstance()
                val y = cal.get(Calendar.YEAR)
                val m = cal.get(Calendar.MONTH)
                val d = cal.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    this@ViewPayoutActivity,
                    { view, year, monthOfYear, dayOfMonth ->

                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(Calendar.YEAR, year)
                        selectedCalendar.set(Calendar.MONTH, monthOfYear)
                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        selectedFromDate = selectedCalendar
                        val selectedDate =
                            AppUtils.INSTANCE?.convertDateToString(
                                selectedCalendar.time,
                                "dd-MMM-yyyy"
                            )
                        selectedFrom = selectedDate.toString()
                        etFromDate.setText(selectedDate)
                        etToDate.setText("")
                    },
                    y,
                    m,
                    d
                )
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                datePickerDialog.show()
            }

            etToDate.setOnClickListener {
                val cal = Calendar.getInstance()
                val y = cal.get(Calendar.YEAR)
                val m = cal.get(Calendar.MONTH)
                val d = cal.get(Calendar.DAY_OF_MONTH)


                val datePickerDialog = DatePickerDialog(
                    this@ViewPayoutActivity,
                    { view, year, monthOfYear, dayOfMonth ->
                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(Calendar.YEAR, year)
                        selectedCalendar.set(Calendar.MONTH, monthOfYear)
                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val selectedDate =
                            AppUtils.INSTANCE?.convertDateToString(
                                selectedCalendar.time,
                                "dd-MMM-yyyy"
                            )
                        selectedTo = selectedDate.toString()
                        etToDate.setText(selectedDate)
                    },
                    y,
                    m,
                    d
                )
                if (selectedFromDate == null) {
                    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

                } else {
                    datePickerDialog.datePicker.minDate = selectedFromDate?.time?.time!!
                    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                }
                datePickerDialog.show()
            }
        }
    }

    private fun getViewPayoutRequestModel(): ViewPayoutRequest {
        val request = ViewPayoutRequest()
        request.fromDate = selectedFrom
        request.toDate = selectedTo
        request.gNETAssociateID = gnetId
        return request
    }

    private fun callViewPayoutApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                layoutNoInternet.root.visibility = View.GONE
                viewPayoutViewModel.callViewPayoutApi(request = getViewPayoutRequestModel())

            } else {
                layoutNoInternet.root.visibility = View.VISIBLE
                recyclerPayout.visibility = View.GONE
//                showToast(getString(R.string.no_internet_connection))
            }
            layoutNoData.root.visibility = View.GONE
        }
    }

    private fun setUpAdapter() {
        binding.apply {
            viewPayoutAdapter = ViewPayoutAdapter(this@ViewPayoutActivity, payoutList)
            recyclerPayout.adapter = viewPayoutAdapter
        }
    }

    fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.root,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    private fun showNoDataLayout(show: Boolean) {
        binding.apply {
            noDataLayout(
                noDataLayout = layoutNoData.root,
                recyclerView = recyclerPayout,
                show = show
            )
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.view_payout)
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = View.VISIBLE
        }
    }
}