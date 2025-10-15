package com.example.digitracksdk.presentation.home.new_reimbursement.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseFragment
import com.innov.digitrac.databinding.FragmentNewReimbursementPendingBinding
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementListRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementVoucherListModel
import com.example.digitracksdk.presentation.home.reimbursements.ReimbursementViewModel
import com.example.digitracksdk.presentation.home.reimbursements.adapter.ReimbursementAdapter
import com.example.digitracksdk.presentation.home.reimbursements.fragments.ReimbursementDetailsBottomSheetFragment
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementModel
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementType
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class NewReimbursementPendingFragment : BaseFragment(),
    ReimbursementAdapter.ReimbursementClickManager {
    lateinit var binding: FragmentNewReimbursementPendingBinding
    lateinit var reimbursementDetailsBottomSheet: ReimbursementDetailsBottomSheetFragment
    lateinit var preferenceUtils: PreferenceUtils
    private val reimbursementViewModel: ReimbursementViewModel by viewModel()
    private var gNetAssociateId: String = ""
    private var associateId: String = ""

    var innovId: String = ""

    var list: ArrayList<ReimbursementModel> = ArrayList()

    lateinit var adapter: ReimbursementAdapter

    companion object {
        fun newInstance(): NewReimbursementPendingFragment {
            return NewReimbursementPendingFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNewReimbursementPendingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceUtils = PreferenceUtils(requireContext())
        getPreferenceData()
//        setUpAdapter()
        setUpObserver()
        setUpListener()
        callReimbursementListApi()
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callReimbursementListApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                callReimbursementListApi()
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        callReimbursementListApi()
    }

    fun callReimbursementListApi() {
        binding.apply {
            if (requireActivity().isNetworkAvailable()) {
                toggleLoader(true)
                layoutNoInternet.root.visibility = GONE
                reimbursementViewModel.callGetNewReimbursementPendingListApi(
                    getReimbursementListRequestModel()
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerReimbursement1.visibility = GONE
            }
            layoutNoData.root.visibility = GONE
        }
    }

    private fun getReimbursementListRequestModel(): ReimbursementListRequestModel {
        val request = ReimbursementListRequestModel()
        request.GNETAssociateId = gNetAssociateId
        request.InnovId = innovId
        return request
    }

    private fun setUpObserver() {
        binding.apply {
            with(reimbursementViewModel) {
                newReimbursementPendingListResponseData.observe(viewLifecycleOwner) {
                    toggleLoader(false)
                    if (it.status.toString().lowercase() == Constant.SUCCESS.lowercase()) {
                        if (it.ReimbursementVoucherListDetails?.isNotEmpty() == true) {
                            val reimbursementAwaitingList: ArrayList<ReimbursementVoucherListModel> =
                                ArrayList()
                            reimbursementAwaitingList.clear()
                            for (i in it.ReimbursementVoucherListDetails ?: arrayListOf()) {
                                if (i.ApprovalStatus == Constant.Rejected || i.ApprovalStatusL2 == Constant.Rejected || i.ApprovalStatusL3 == Constant.Rejected) {

                                } else if (i.ApprovalStatus == Constant.Approved || i.ApprovalStatusL2 == Constant.Approved || i.ApprovalStatusL3 == Constant.Approved) {

                                } else {
                                    reimbursementAwaitingList.add(i)
                                }
                            }
                            list.clear()
                            if (reimbursementAwaitingList.isNotEmpty()) {
                                val reimbursementType = com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementType.AWAITING
                                layoutNoData.root.visibility = GONE
                                recyclerReimbursement1.visibility = VISIBLE
                                for (i in reimbursementAwaitingList) {
                                    var title = ""
                                    title = when {
                                        i.ApproverName.toString().isNotEmpty() -> {
                                            i.ApproverName.toString()
                                        }
                                        i.ApproverNameL2.toString().isNotEmpty() -> {
                                            i.ApproverNameL2.toString()
                                        }
                                        else -> {
                                            i.ApproverNameL3.toString()
                                        }
                                    }
                                    list.add(
                                        ReimbursementModel(
                                            title = title,
                                            description = i.CreatedDate,
                                            type = reimbursementType,
                                            category = com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementCategoryType.PUBLIC_TRANSPORT,
                                            amount = "${getString(R.string.rupees_symbol)}${i.TotalAmount}",
                                            AssociateReimbursementId = i.AssociateReimbursementId,
                                            createdDate = i.CreatedDate,
                                            voucherNo = i.VoucherNo,
                                            paidDate = getString(R.string.paid_date) + " " + i.PaidDate,
                                            paidStatus = getString(R.string.paid_status) + " " + i.PaidStatus
                                        )
                                    )
                                }
                            } else {
                                showNoDataLayout(true)
                            }
                        } else {
                            showNoDataLayout(true)
                        }
                        setUpAdapter()

                    } else {
                        showNoDataLayout(true)
                        showToast(it.Message.toString())
                    }

                }
                messageData.observe(viewLifecycleOwner) {
                    toggleLoader(false)
                    showNoDataLayout(true)
                    showToast(it.toString())
                }
            }
        }
    }

    fun toggleLoader(showLoader: Boolean) {
        binding.apply {
            toggleFadeView(
                root, contentLoading.root, contentLoading.imageLoading, showLoader
            )
        }
    }

    private fun showNoDataLayout(show: Boolean) {
        binding.apply {
            noDataLayout(
                noDataLayout = layoutNoData.root, recyclerView = recyclerReimbursement1, show = show
            )
        }
    }

    private fun getPreferenceData() {
        binding.apply {
            preferenceUtils.apply {
                gNetAssociateId = getValue(Constant.PreferenceKeys.GnetAssociateID)
                associateId = getValue(Constant.PreferenceKeys.AssociateID)
                innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
            }
        }
    }

    private fun setUpAdapter() {
        binding.apply {
            adapter =
                ReimbursementAdapter(requireActivity(), list, this@NewReimbursementPendingFragment)
            recyclerReimbursement1.adapter = adapter
        }
    }

    override fun onItemClick(position: Int, associateReimbursementId: String, createdDate: String) {
        reimbursementDetailsBottomSheet = ReimbursementDetailsBottomSheetFragment()
        reimbursementDetailsBottomSheet.reimbursementType = ReimbursementType.AWAITING
        reimbursementDetailsBottomSheet.date = createdDate
        reimbursementDetailsBottomSheet.associateReimbursementId = associateReimbursementId
        reimbursementDetailsBottomSheet.show(
            parentFragmentManager,
            "reimbursement bottom sheet"
        )
    }
}