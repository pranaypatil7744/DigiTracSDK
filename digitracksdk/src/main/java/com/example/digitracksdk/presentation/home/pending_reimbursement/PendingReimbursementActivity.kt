package com.example.digitracksdk.presentation.home.pending_reimbursement

import android.os.Bundle
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import android.text.TextUtils
import android.view.View.*
import androidx.activity.enableEdgeToEdge
import com.innov.digitrac.databinding.ActivityPendingReimbursementBinding
import com.example.digitracksdk.domain.model.reimbursement_model.PendingReimbursementRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.UpdatePendingReimbursementRequestModel
import com.example.digitracksdk.presentation.home.pending_reimbursement.adapter.PendingReimbursementAdapter
import com.example.digitracksdk.presentation.home.pending_reimbursement.model.PendingReimbursementModel
import com.example.digitracksdk.presentation.home.reimbursements.ReimbursementViewModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.DialogUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class PendingReimbursementActivity : BaseActivity(),
    PendingReimbursementAdapter.PendingReimbursementClickManager, DialogUtils.DialogManager {
    lateinit var binding: ActivityPendingReimbursementBinding
    private lateinit var pendingReimbursementAdapter: PendingReimbursementAdapter
    private val reimbursementViewModel: ReimbursementViewModel by viewModel()
    lateinit var preferenceUtils: PreferenceUtils
    private var pendingList: ArrayList<PendingReimbursementModel> = ArrayList()
    private var checkSelection: ArrayList<PendingReimbursementModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPendingReimbursementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setUpObserver()
        callPendingReimbursementApi()
        setUpPendingReimAdapter()
        setUpListener()
    }

    private fun setUpObserver() {
        binding.apply {
            with(reimbursementViewModel) {
                pendingReimbursementListResponseData.observe(this@PendingReimbursementActivity) {
                    toggleLoader(false)
                    if (it.LSTPendingFinalVoucher.isNotEmpty()) {
                        showNoDataLayout(false)
                        btnSubmit.visibility = VISIBLE
                        pendingList.clear()
                        for (i in it.LSTPendingFinalVoucher) {
                            pendingList.add(
                                PendingReimbursementModel(
                                    title = i.VoucherNo,
                                    date = i.AR_CreatedON,
                                    amount = getString(R.string.rupees_symbol) + i.TotalAmount,
                                    AR_AssociateReimbursementId = i.AR_AssociateReimbursementId,
                                    isSelected = false,
                                    month = i.VoucherMonth,
                                    year = i.VoucherYear
                                )
                            )
                        }
                        pendingReimbursementAdapter.notifyDataSetChanged()
                    } else {
                        showNoDataLayout(true)
                        btnSubmit.visibility = GONE
                    }
                }

                updatePendingReimbursementResponseData.observe(this@PendingReimbursementActivity) {
                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        showToast(it.Message.toString())
                        for (i in pendingList) {
                            i.isSelected = false
                        }
                        callPendingReimbursementApi()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(this@PendingReimbursementActivity) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }

        }
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callPendingReimbursementApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing=false
                callPendingReimbursementApi()
            }
            btnSubmit.setOnClickListener {
                checkSelection = pendingList.filter {
                    it.isSelected
                } as ArrayList<PendingReimbursementModel>
                if (checkSelection.size == 0) {
                    showToast(getString(R.string.please_select_at_least_one_voucher))
                } else {
                    var isSameMonth: Boolean = true
                    for (i in checkSelection) {
                        val month = i.date.toString().substring(3, 6)
                        val year = i.date.toString().substring(7, 11)
                        if (i.month == checkSelection[0].month &&
                            i.year == checkSelection[0].year
                        ) {
                            //
                        } else {
                            isSameMonth = false
                        }
                    }
                    if (!isSameMonth) {
                        showToast(getString(R.string.please_select_same_month_voucher))
                    } else {
                        val totalSameVoucher = pendingList.filter {
                            it.month == checkSelection[0].month
                                    && it.year == checkSelection[0].year
                        }
                        if (checkSelection.size != totalSameVoucher.size) {
                            DialogUtils.showPermissionDialog(
                                this@PendingReimbursementActivity,
                                title = getString(R.string.alert),
                                msg = getString(R.string.few_vouchers_of_this_month_is_still_pending_for_clubbing_do_you_wish_to_proceed),
                                positiveBtn = getString(R.string.continue_),
                                negativeBtn = getString(R.string.close),
                                isFinish = false,
                                isOtherAction = true,
                                listener = this@PendingReimbursementActivity
                            )
                        } else {
                            val pendingIDList: ArrayList<String> = kotlin.collections.ArrayList()
                            pendingIDList.clear()
                            for (i in checkSelection) {
                                pendingIDList.add(i.AR_AssociateReimbursementId.toString())
                            }
//                            showToast("success")
                            callUpdatePendingVoucherApi(TextUtils.join(",", pendingIDList))
                        }
                    }
                }
            }
        }
    }

    override fun onContinueClick() {
        val pendingIDList: ArrayList<String> = kotlin.collections.ArrayList()
        pendingIDList.clear()
        for (i in checkSelection) {
            pendingIDList.add(i.AR_AssociateReimbursementId.toString())
        }
//        showToast("success")
        callUpdatePendingVoucherApi(TextUtils.join(",", pendingIDList))
    }

    private fun callUpdatePendingVoucherApi(AssociateReimbursementID: String) {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                reimbursementViewModel.callUpdatePendingReimbursementApi(
                    request =
                    UpdatePendingReimbursementRequestModel(
                        AssociateID = preferenceUtils.getValue(Constant.PreferenceKeys.AssociateID),
                        AssociateReimbursementID = AssociateReimbursementID
                    )
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    private fun setUpPendingReimAdapter() {
        binding.apply {
            pendingReimbursementAdapter = PendingReimbursementAdapter(
                this@PendingReimbursementActivity,
                pendingList,
                this@PendingReimbursementActivity,
            1
            )
            recyclerPendingReimbursement.adapter = pendingReimbursementAdapter
        }
    }

    private fun callPendingReimbursementApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                layoutNoInternet.root.visibility = GONE
                btnSubmit.visibility = VISIBLE
                reimbursementViewModel.callPendingReimbursementListApi(
                    request = PendingReimbursementRequestModel(
                        AssociateID = preferenceUtils.getValue(Constant.PreferenceKeys.AssociateID),
                        MappingID = ""
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerPendingReimbursement.visibility= GONE
                btnSubmit.visibility = GONE
            }
            layoutNoData.root.visibility= GONE
        }
    }

    private fun toggleLoader(showLoader: Boolean) {
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
                noDataLayout = layoutNoData.root, recyclerView = recyclerPendingReimbursement, show = show
            )
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.pending_reimbursement)
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
        }
    }

    override fun clickOnItem(position: Int) {
        pendingList[position].isSelected = !pendingList[position].isSelected
        pendingReimbursementAdapter.notifyItemChanged(position)
    }
}