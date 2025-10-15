package com.example.digitracksdk.presentation.leaves.leave_status

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityLeaveStatusBinding
import com.example.digitracksdk.domain.model.leaves.BalanceLeaveStatusRequestModel
import com.example.digitracksdk.presentation.leaves.LeavesViewModel
import com.example.digitracksdk.presentation.leaves.apply_leave.adapter.ApplyLeavesAdapter
import com.example.digitracksdk.presentation.leaves.apply_leave.model.ApplyLeavesModel
import com.example.digitracksdk.presentation.leaves.apply_leave.model.LeavesType
import com.example.digitracksdk.presentation.leaves.apply_leave.model.MyLeaveDashboardModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class LeaveStatusActivity : BaseActivity(), ApplyLeavesAdapter.LeaveClickManager {
    lateinit var binding:ActivityLeaveStatusBinding
    private val leavesViewModel: LeavesViewModel by viewModel()
    lateinit var preferenceUtils: PreferenceUtils
    private lateinit var applyLeavesAdapter: ApplyLeavesAdapter
    private var leaveStatusList:ArrayList<ApplyLeavesModel> = ArrayList()
    private val myBalanceLeavesList: ArrayList<MyLeaveDashboardModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLeaveStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setUpObserver()
        callBalancedLeaveStatusApi()
        callLeaveStatusSummaryApi()
        setUpAdapter()
        setUpListener()
    }

    private fun setUpObserver() {
        binding.apply {
            with(leavesViewModel) {
                balanceLeaveStatusResponse.observe(this@LeaveStatusActivity) {
                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        myBalanceLeavesList.clear()
                        myBalanceLeavesList.add(
                            MyLeaveDashboardModel(
                                leaveMsg = it.PLText,
                                remainingLeaves = it.PL ?: "0",
                                leavesIcon = R.drawable.ic_annual_leaves
                            )
                        )
                        myBalanceLeavesList.add(
                            MyLeaveDashboardModel(
                                leaveMsg = it.CLText,
                                remainingLeaves = it.CL ?: "0",
                                leavesIcon = R.drawable.ic_casual_leave
                            )
                        )
                        myBalanceLeavesList.add(
                            MyLeaveDashboardModel(
                                leaveMsg = it.SLText,
                                remainingLeaves = it.SL ?: "0",
                                leavesIcon = R.drawable.ic_sick_leave
                            )
                        )
                        leaveStatusList.add(
                            ApplyLeavesModel(
                                leaveMsg = getString(R.string.leave_balance_),
                                leavesDisplayType = LeavesType.TITLE
                            )
                        )
                        leaveStatusList.add(
                            ApplyLeavesModel(
                                myLeaveDashboardList = myBalanceLeavesList,
                                leavesDisplayType = LeavesType.MY_LEAVES
                            )
                        )
                        applyLeavesAdapter.notifyDataSetChanged()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                leaveStatusSummaryResponse.observe(this@LeaveStatusActivity) {
                    toggleLoader(false)

                    val leavesStatusList: ArrayList<MyLeaveDashboardModel> = ArrayList()

                    leavesStatusList.clear()
                    leavesStatusList.add(
                        MyLeaveDashboardModel(
                            leaveMsg = getString(R.string.leave_requests),
                            remainingLeaves = it.TotalLeaveRequest ?: "0",
                            leavesIcon = R.drawable.ic_leave_requested
                        )
                    )
                    leavesStatusList.add(
                        MyLeaveDashboardModel(
                            leaveMsg = getString(R.string.approved_leaves),
                            remainingLeaves = it.LeavesApproved ?: "0",
                            leavesIcon = R.drawable.ic_leave_approved
                        )
                    )
                    leavesStatusList.add(
                        MyLeaveDashboardModel(
                            leaveMsg = getString(R.string.pending_leaves),
                            remainingLeaves = it.PendingApproval ?: "0",
                            leavesIcon = R.drawable.ic_leave_pending
                        )
                    )
                    leavesStatusList.add(
                        MyLeaveDashboardModel(
                            leaveMsg = getString(R.string.rejected_leaves),
                            remainingLeaves = it.LeavesRejected ?: "0",
                            leavesIcon = R.drawable.ic_leave_rejected
                        )
                    )
                    leaveStatusList.add(
                        ApplyLeavesModel(
                            leaveMsg = getString(R.string.leave_status),
                            leavesDisplayType = LeavesType.TITLE
                        )
                    )
                    leaveStatusList.add(
                        ApplyLeavesModel(
                            myLeaveDashboardList = leavesStatusList,
                            leavesDisplayType = LeavesType.LEAVE_STATUS
                        )
                    )
                    applyLeavesAdapter.notifyDataSetChanged()
                }

                messageData.observe(this@LeaveStatusActivity
                ) { it ->
                    toggleLoader(false)
                    it?.let { it1 -> showToast(it1) }
                }
            }
        }
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener{
                leaveStatusList.clear()
                callBalancedLeaveStatusApi()
                callLeaveStatusSummaryApi()
            }
        }
    }

    private fun setUpAdapter() {
        binding.apply {
            applyLeavesAdapter = ApplyLeavesAdapter(this@LeaveStatusActivity,leaveStatusList,myBalanceLeavesList,this@LeaveStatusActivity)
            recyclerLeavesStatus.adapter = applyLeavesAdapter
        }
    }

    private fun callBalancedLeaveStatusApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                binding.layoutNoInternet.root.visibility = GONE
                leavesViewModel.callBalanceLeaveStatusApi(request = BalanceLeaveStatusRequestModel(
                    AssociateID = preferenceUtils.getValue(Constant.PreferenceKeys.AssociateID),
                    InnovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
                )
                )
            } else {
                binding.layoutNoInternet.root.visibility = VISIBLE
            }
        }
    }

    private fun callLeaveStatusSummaryApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                binding.layoutNoInternet.root.visibility = GONE
                leavesViewModel.callLeaveStatusSummaryApi(request = BalanceLeaveStatusRequestModel(
                    AssociateID = preferenceUtils.getValue(Constant.PreferenceKeys.AssociateID),
                    InnovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
                )
                )
            } else {
                binding.layoutNoInternet.root.visibility = VISIBLE
            }
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
    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.leave_status)
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
        }
    }

    override fun onItemClick(position: Int, type: String) {
        val i = Intent(this, LeaveHistoryActivity::class.java)
        val b = Bundle()
        b.putString(Constant.SCREEN_NAME,type)
        i.putExtras(b)
        startActivity(i)
    }
}