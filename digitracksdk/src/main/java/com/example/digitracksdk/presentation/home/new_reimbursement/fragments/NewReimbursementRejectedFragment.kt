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
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementModel
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class NewReimbursementRejectedFragment : BaseFragment(),
    ReimbursementAdapter.ReimbursementClickManager {

    lateinit var binding: FragmentNewReimbursementPendingBinding
    lateinit var preferenceUtils: PreferenceUtils
    private val reimbursementViewModel: ReimbursementViewModel by viewModel()
    private var gNetAssociateId: String = ""
    private var associateId: String = ""

    var innovId: String = ""

    var list: ArrayList<ReimbursementModel> = ArrayList()

    lateinit var adapter: ReimbursementAdapter

    companion object {
        fun newInstance(): NewReimbursementRejectedFragment {
            return NewReimbursementRejectedFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
                reimbursementViewModel.callGetNewReimbursementRejectedListApi(
                    getReimbursementListRequestModel()
                )
            } else {
                recyclerReimbursement1.visibility = GONE
                layoutNoInternet.root.visibility = VISIBLE
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
                newReimbursementRejectedListResponseData.observe(viewLifecycleOwner) {
                    toggleLoader(false)
                    val reimbursementRejectedList: ArrayList<ReimbursementVoucherListModel> =
                        ArrayList()
                    reimbursementRejectedList.clear()

                    if (it.ReimbursementVoucherListDetails?.isNotEmpty() == true) {
                        for (i in it.ReimbursementVoucherListDetails ?: arrayListOf()) {
                            if (i.ApprovalStatus == Constant.Rejected || i.ApprovalStatusL2 == Constant.Rejected || i.ApprovalStatusL3 == Constant.Rejected) {
                                reimbursementRejectedList.add(i)
                            } else if (i.ApprovalStatus == Constant.Approved || i.ApprovalStatusL2 == Constant.Approved || i.ApprovalStatusL3 == Constant.Approved) {

                            } else {

                            }
                        }

                        list.clear()
                        if (reimbursementRejectedList.isNotEmpty()) {
//            reimbursementRejectedList?.addAll(rejectedList)
                            val reimbursementType = com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementType.REJECTED
                            showNoDataLayout(false)
                            for (i in reimbursementRejectedList ?: arrayListOf()) {
                                list.add(
                                    ReimbursementModel(
                                        title = i.ApproverName,
                                        description = i.CreatedDate,
                                        type = reimbursementType,
                                        category = com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementCategoryType.PUBLIC_TRANSPORT,
                                        amount = "${getString(R.string.rupees_symbol)}${i.TotalAmount}",
                                        approverName1 = getString(R.string.approver_name) + " " + i.ApproverName,
                                        approvedDate1 = getString(R.string.rejected_date) + " " + i.ApprovedDate,
                                        approvalStatus1 = getString(R.string.approval_status) + " " + i.ApprovalStatus,
                                        approvalRemark1 = getString(R.string.approval_remark) + " " + i.ApprovalRemark,
                                        approverName2 = getString(R.string.approver_namel2) + " " + i.ApproverNameL2,
                                        approvedDate2 = getString(R.string.rejected_datel2) + " " + i.ApprovedDateL2,
                                        approvalStatus2 = getString(R.string.approval_statusl2) + " " + i.ApprovalStatusL2,
                                        approvalRemark2 = getString(R.string.approval_remarkl2) + " " + i.ApprovalRemarkL2,
                                        approverName3 = getString(R.string.approver_namel3) + " " + i.ApproverNameL3,
                                        approvedDate3 = getString(R.string.rejected_datel3) + " " + i.ApprovedDateL3,
                                        approvalStatus3 = getString(R.string.approval_statusl3) + " " + i.ApprovalStatusL3,
                                        approvalRemark3 = getString(R.string.approval_remarkl3) + " " + i.ApprovalRemarkL3,
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
                        setUpAdapter()
                    } else {
                        showNoDataLayout(true)
                    }

                }
                messageData.observe(viewLifecycleOwner) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }
        }
    }

    fun toggleLoader(showLoader: Boolean) {
        binding.apply {
            toggleFadeView(
                root,
                contentLoading.root,
                contentLoading.imageLoading,
                showLoader
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
                ReimbursementAdapter(requireActivity(), list, this@NewReimbursementRejectedFragment)
            recyclerReimbursement1.adapter = adapter
        }
    }

    override fun onItemClick(position: Int, associateReimbursementId: String, createdDate: String) {

    }
}