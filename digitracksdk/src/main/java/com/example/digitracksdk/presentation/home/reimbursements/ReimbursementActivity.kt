package com.example.digitracksdk.presentation.home.reimbursements

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityReimbursementBinding
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementDetailsRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementDetailsResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementListRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementVoucherListModel
import com.example.digitracksdk.presentation.home.reimbursements.adapter.ReimbursementPagerAdapter
import com.example.digitracksdk.presentation.home.reimbursements.fragments.ApprovedFragment
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementCategoryType
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementModel
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementType
import com.example.digitracksdk.presentation.home.reimbursements.fragments.AwaitingFragment
import com.example.digitracksdk.presentation.home.reimbursements.fragments.RejectedFragment
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimburseDetailsModel
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementDetailsListModel
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementDetailsType
import com.example.digitracksdk.presentation.home.reimbursements.model.*
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayoutMediator
import com.example.digitracksdk.utils.AppUtils
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.collections.ArrayList

class ReimbursementActivity : BaseActivity() {
    private val reimbursementViewModel: ReimbursementViewModel by viewModel()
    lateinit var preferenceUtils: PreferenceUtils
    lateinit var binding: ActivityReimbursementBinding
    var tabsList: ArrayList<String> = ArrayList()

    var fragmentList: ArrayList<Fragment> = ArrayList()
    var awaitingFragment = AwaitingFragment.newInstance()
    var approvedFragment = ApprovedFragment.newInstance()
    var rejectedFragment = RejectedFragment.newInstance()

    var reimbursementListData: ArrayList<ReimbursementVoucherListModel>? = ArrayList()
    var reimbursementAwaitingList: ArrayList<ReimbursementVoucherListModel>? = ArrayList()
    var reimbursementRejectedList: ArrayList<ReimbursementVoucherListModel>? = ArrayList()
    var reimbursementApprovedList: ArrayList<ReimbursementVoucherListModel>? = ArrayList()

    var gnetAssociateId: String = ""
    var innovId: String = ""
    var awaitingCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReimbursementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        setObserver()
        getPreferenceData()
        setUpToolbar()
        setListener()
        setUpView()
    }

    override fun onResume() {
        super.onResume()
        callReimbursementListApi()
    }

    private fun setObserver() {
        binding.apply {
            with(reimbursementViewModel) {
                reimbursementListResponseData.observe(this@ReimbursementActivity) {
                    if (it.status.toString().lowercase() == Constant.SUCCESS.lowercase()) {
                        if (it.ReimbursementVoucherListDetails!!.isNotEmpty()) {

                            showNoDataLayout(false)
                            reimbursementListData?.clear()
                            reimbursementListData?.addAll(
                                it.ReimbursementVoucherListDetails ?: arrayListOf()
                            )
                            setUpListsData()
                        } else
                            showNoDataLayout(true)

                    } else {
                        showNoDataLayout(true)
                        showToast(it.Message.toString())
                    }

                }
                messageData.observe(
                    this@ReimbursementActivity
                ) { t ->
                    showNoDataLayout(true)
                    showToast(t.toString())
                }
                showProgressbar.observe(this@ReimbursementActivity) {
                    toggleLoader(it)
                }
            }
        }
    }

    fun callReimbursementListApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                reimbursementViewModel.callReimbursementListApi(getReimbursementListRequestModel())
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                fragmentPager.visibility = GONE
            }
            layoutNoData.root.visibility = GONE
        }
    }

    private fun setUpListsData() {
        reimbursementAwaitingList?.clear()
        reimbursementRejectedList?.clear()
        reimbursementApprovedList?.clear()

//        val awaitingList = reimbursementListData?.filter {
//            (it.ApprovalStatus == "").or(it.ApprovalStatusL2 == "").or(it.ApprovalStatusL3 == "")
//        }
//        val rejectedList = reimbursementListData?.filter {
//            (it.ApprovalStatus == Constant.Rejected).or(it.ApprovalStatusL2 == Constant.Rejected).or(it.ApprovalStatusL3 == Constant.Rejected)
//        }
//        val approvedList = reimbursementListData?.filter {
//            (it.ApprovalStatus == Constant.Approved).and(it.ApprovalStatusL2 == Constant.Approved).and(it.ApprovalStatusL3 == Constant.Approved)
//        }

        for (i in reimbursementListData ?: arrayListOf()) {
            if (i.ApprovalStatus == Constant.Rejected || i.ApprovalStatusL2 == Constant.Rejected || i.ApprovalStatusL3 == Constant.Rejected) {
                reimbursementRejectedList?.add(i)
            } else if (i.ApprovalStatus == Constant.Approved || i.ApprovalStatusL2 == Constant.Approved || i.ApprovalStatusL3 == Constant.Approved) {
                reimbursementApprovedList?.add(i)
            } else {
                reimbursementAwaitingList?.add(i)
            }
        }

        awaitingFragment.apply {

        }

        if (!reimbursementAwaitingList.isNullOrEmpty()) {
//            reimbursementAwaitingList?.addAll(awaitingList)
            awaitingCount = reimbursementAwaitingList?.size ?: 0
            TabLayoutMediator(binding.tabLayout, binding.fragmentPager) { tab, position ->
                tab.text = tabsList[position]
                if (position == 0) {
                    tab.orCreateBadge.backgroundColor =
                        ContextCompat.getColor(this, R.color.blue_ribbon)
                    tab.orCreateBadge.badgeTextColor = ContextCompat.getColor(this, R.color.white)
                    tab.orCreateBadge.number = awaitingCount
                    tab.orCreateBadge.badgeGravity = BadgeDrawable.TOP_END
                }
            }.attach()
            awaitingFragment.apply {
                list.clear()
                val reimbursementType = ReimbursementType.AWAITING
                binding.layoutNoData.root.visibility = GONE
                for (i in reimbursementAwaitingList ?: arrayListOf()) {
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
                            category = ReimbursementCategoryType.PUBLIC_TRANSPORT,
                            amount = "${getString(R.string.rupees_symbol)}${i.TotalAmount}",
                            AssociateReimbursementId = i.AssociateReimbursementId,
                            createdDate = i.CreatedDate,
                            voucherNo = i.VoucherNo,
                            paidDate = getString(R.string.paid_date) + " " + i.PaidDate,
                            paidStatus = getString(R.string.paid_status) + " " + i.PaidStatus
                        )
                    )
                }
                adapter.notifyDataSetChanged()
            }
        } else {
            awaitingFragment.binding.layoutNoData.root.visibility = GONE
        }

        rejectedFragment.apply {
            list.clear()
            if (!reimbursementRejectedList.isNullOrEmpty()) {
//            reimbursementRejectedList?.addAll(rejectedList)
                val reimbursementType = ReimbursementType.REJECTED
                binding.layoutNoData.root.visibility = GONE
                for (i in reimbursementRejectedList ?: arrayListOf()) {
                    list.add(
                        ReimbursementModel(
                            title = i.ApproverName,
                            description = i.CreatedDate,
                            type = reimbursementType,
                            category = ReimbursementCategoryType.PUBLIC_TRANSPORT,
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
                binding.layoutNoData.root.visibility = VISIBLE
            }
            adapter.notifyDataSetChanged()
        }


        if (!reimbursementApprovedList.isNullOrEmpty()) {
//            reimbursementApprovedList?.addAll(approvedList)
            approvedFragment.apply {
                binding.layoutNoData.root.visibility = GONE
                list.clear()
                for (i in reimbursementApprovedList ?: arrayListOf()) {
                    val reimbursementType = ReimbursementType.APPROVED
                    list.add(
                        ReimbursementModel(
                            title = i.ApproverName,
                            description = i.CreatedDate,
                            type = reimbursementType,
                            category = ReimbursementCategoryType.PUBLIC_TRANSPORT,
                            amount = "${getString(R.string.rupees_symbol)}${i.TotalAmount}",
                            approverName1 = getString(R.string.approver_name) + " " + i.ApproverName,
                            approvedDate1 = getString(R.string.approved_date) + " " + i.ApprovedDate,
                            approvalStatus1 = getString(R.string.approval_status) + " " + i.ApprovalStatus,
                            approvalRemark1 = getString(R.string.approval_remark) + " " + i.ApprovalRemark,
                            approverName2 = getString(R.string.approver_namel2) + " " + i.ApproverNameL2,
                            approvedDate2 = getString(R.string.approved_datel2) + " " + i.ApprovedDateL2,
                            approvalStatus2 = getString(R.string.approval_statusl2) + " " + i.ApprovalStatusL2,
                            approvalRemark2 = getString(R.string.approval_remarkl2) + " " + i.ApprovalRemarkL2,
                            approverName3 = getString(R.string.approver_namel3) + " " + i.ApproverNameL3,
                            approvedDate3 = getString(R.string.approved_datel3) + " " + i.ApprovedDateL3,
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
                adapter.notifyDataSetChanged()
            }
        } else {
            approvedFragment.binding.layoutNoData.root.visibility = VISIBLE
        }

    }

    private fun getReimbursementListRequestModel(): ReimbursementListRequestModel {
        val request = ReimbursementListRequestModel()
        request.GNETAssociateId = gnetAssociateId
        request.InnovId = innovId
        return request
    }

    private fun getPreferenceData() {
        gnetAssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
    }

    private fun setListener() {
        binding.apply {
            fragmentPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
                }
            })

            layoutNoInternet.btnTryAgain.setOnClickListener {
                callReimbursementListApi()
            }
            fabAddReimbursement.setOnClickListener {
                startActivity(
                    Intent(
                        this@ReimbursementActivity,
                        AddReimbursementActivity::class.java
                    )
                )
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                callReimbursementListApi()
            }
        }
    }

    private fun setUpView() {
        fragmentList.add(awaitingFragment)
        fragmentList.add(approvedFragment)
        fragmentList.add(rejectedFragment)
        binding.fragmentPager.adapter = ReimbursementPagerAdapter(this, fragmentList)
        binding.fragmentPager.isUserInputEnabled = true
        binding.fragmentPager.offscreenPageLimit = 3
        tabsList.add(getString(R.string.awaiting))
        tabsList.add(getString(R.string.approved))
        tabsList.add(getString(R.string.rejected))

        TabLayoutMediator(binding.tabLayout, binding.fragmentPager) { tab, position ->
            tab.text = tabsList[position]
            if (position == 0) {
                tab.orCreateBadge.backgroundColor =
                    ContextCompat.getColor(this, R.color.blue_ribbon)
                tab.orCreateBadge.badgeTextColor = ContextCompat.getColor(this, R.color.white)
                tab.orCreateBadge.number = awaitingCount
                tab.orCreateBadge.badgeGravity = BadgeDrawable.TOP_END
            }
        }.attach()

    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.reimbursements)
            btnBack.setOnClickListener {
                finish()
            }
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
                viewPager2 = fragmentPager,
                show = show
            )
        }
    }

    fun callReimbursementDetailsApi(associateReimbursementId: String): ArrayList<ReimburseDetailsModel>? {
        if (isNetworkAvailable()) {
            reimbursementViewModel.callReimbursementDetailsApi(
                request = ReimbursementDetailsRequestModel(
                    AssociateReimbursementId = associateReimbursementId
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
        with(reimbursementViewModel) {
            val detailsList: ArrayList<ReimburseDetailsModel> = ArrayList()

            showProgressbar.observe(this@ReimbursementActivity) {
                toggleLoader(it)
            }
            reimbursementDetailsResponseData.observe(this@ReimbursementActivity,
                object : Observer<ReimbursementDetailsResponseModel> {
                    override fun onChanged(it: ReimbursementDetailsResponseModel) {
                        reimbursementDetailsResponseData.removeObserver(this)
                        val list: ArrayList<ReimbursementDetailsListModel> = ArrayList()
                        list.clear()
                        if (!it.ListOfAssociateReimbursement.isNullOrEmpty()) {
                            for (i in it.ListOfAssociateReimbursement ?: arrayListOf()) {
                                list.add(
                                    ReimbursementDetailsListModel(
                                        title = getString(R.string.claim_date),
                                        subTitle = i.ClaimDate,
                                        title2 = getString(R.string.expensive_type),
                                        subTitle2 = i.ReimbursementCategory,
                                        reimbursementDetailsType = ReimbursementDetailsType.MULTI_LINE
                                    )
                                )
                                list.add(
                                    ReimbursementDetailsListModel(
                                        title = getString(R.string.claim_year),
                                        subTitle = i.ClaimYear,
                                        title2 = getString(R.string.claim_month),
                                        subTitle2 = i.ClaimMonth,
                                        reimbursementDetailsType = ReimbursementDetailsType.MULTI_LINE
                                    )
                                )
                                list.add(
                                    ReimbursementDetailsListModel(
                                        title = getString(R.string.bill_date),
                                        subTitle = i.BillDate,
                                        title2 = getString(R.string.bill_number),
                                        subTitle2 = i.BillNo,
                                        reimbursementDetailsType = ReimbursementDetailsType.MULTI_LINE
                                    )
                                )
                                list.add(
                                    ReimbursementDetailsListModel(
                                        title = getString(R.string.bill_from),
                                        subTitle = i.FromDate,
                                        title2 = getString(R.string.bill_to),
                                        subTitle2 = i.ToDate,
                                        reimbursementDetailsType = ReimbursementDetailsType.MULTI_LINE
                                    )
                                )
                                list.add(
                                    ReimbursementDetailsListModel(
                                        title = getString(R.string.journey_from),
                                        subTitle = i.FromLocation,
                                        title2 = getString(R.string.journey_to),
                                        subTitle2 = i.ToLocation,
                                        reimbursementDetailsType = ReimbursementDetailsType.MULTI_LINE
                                    )
                                )
                                list.add(
                                    ReimbursementDetailsListModel(
                                        title = getString(R.string.base_amount),
                                        subTitle = i.Amount,
                                        title2 = getString(R.string.travel_mode),
                                        subTitle2 = i.ModeOfTravel,
                                        reimbursementDetailsType = ReimbursementDetailsType.MULTI_LINE
                                    )
                                )
                                list.add(
                                    ReimbursementDetailsListModel(
                                        title = getString(R.string.tax_amount),
                                        subTitle = i.TaxAmount,
                                        title2 = getString(R.string.gross_amount),
                                        subTitle2 = i.GrossAmount,
                                        reimbursementDetailsType = ReimbursementDetailsType.MULTI_LINE
                                    )
                                )
                                list.add(
                                    ReimbursementDetailsListModel(
                                        title = getString(R.string.start_km),
                                        subTitle = i.StartKM,
                                        title2 = getString(R.string.end_km),
                                        subTitle2 = i.EndKM,
                                        reimbursementDetailsType = ReimbursementDetailsType.MULTI_LINE
                                    )
                                )
                                list.add(
                                    ReimbursementDetailsListModel(
                                        reimbursementDetailsType = ReimbursementDetailsType.TWO_LINE,
                                        title = getString(R.string.remark),
                                        subTitle = i.Remark
                                    )
                                )
                                list.add(
                                    ReimbursementDetailsListModel(
                                        reimbursementDetailsType = ReimbursementDetailsType.TWO_LINE,
                                        title = getString(R.string.bill),
                                        subTitle = i.FilePath1 ?: "${getString(R.string.bill)}.pdf",
                                        associateId = i.AssociateReimbursementDetailId
                                    )
                                )
                                list.add(ReimbursementDetailsListModel(reimbursementDetailsType = ReimbursementDetailsType.DIVIDER))
                            }
                            detailsList.clear()
                            detailsList.add(ReimburseDetailsModel(reimbursementDetailsList = list))
                        }
                    }

                })
            messageData.observe(this@ReimbursementActivity
            ) { it ->
                toggleLoader(false)
                showToast(it.toString())
            }
            return detailsList
        }
    }
}