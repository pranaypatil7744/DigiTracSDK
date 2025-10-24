package com.example.digitracksdk.presentation.leaves.leave_status

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityLeaveHistoryBinding
import com.example.digitracksdk.domain.model.leaves.LeaveRequestViewRequestModel
import com.example.digitracksdk.domain.model.leaves.ListAppliedLeaveModel
import com.example.digitracksdk.presentation.leaves.LeavesViewModel
import com.example.digitracksdk.presentation.leaves.apply_leave.adapter.ApplyLeavesAdapter
import com.example.digitracksdk.presentation.leaves.apply_leave.model.ApplyLeavesModel
import com.example.digitracksdk.presentation.leaves.apply_leave.model.LeavesStatus
import com.example.digitracksdk.presentation.leaves.apply_leave.model.LeavesType
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class LeaveHistoryActivity : BaseActivity() {
    lateinit var binding:ActivityLeaveHistoryBinding
    var screenName:String? = ""
    var innovId: String? = ""
    var gnetAssociateId: String? = ""
    lateinit var preferenceUtils: PreferenceUtils
    private val leavesViewModel: LeavesViewModel by viewModel()
    private var applyLeavesList: ArrayList<ApplyLeavesModel> = ArrayList()
    private lateinit var applyLeavesAdapter: ApplyLeavesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLeaveHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        getPreferenceData()
        getIntentData()
        setUpToolbar()
        setUpObserver()
        callViewLeaveRequestListApi()
        setUpAdapter()
        setUpListener()
    }

    private fun setUpObserver() {
        binding.apply {
            with(leavesViewModel) {
                viewLeavesRequestListResponse.observe(this@LeaveHistoryActivity) {
                    toggleLoader(false)
                    if (it.status == Constant.success) {
                        setUpListData(it.lstAppliedLeave ?: arrayListOf())
                    } else {
                        showToast(it.Message.toString())
                    }
                }
                messageData.observe(this@LeaveHistoryActivity
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
                callViewLeaveRequestListApi()
            }
        }
    }

    private fun setUpAdapter() {
        binding.apply {
            applyLeavesAdapter = ApplyLeavesAdapter(this@LeaveHistoryActivity,applyLeavesList,
                arrayListOf())
            recyclerApplyHistory.adapter = applyLeavesAdapter
        }
    }

    private fun getPreferenceData() {
        gnetAssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
    }
    private fun getIntentData() {
        intent?.extras?.run{
            screenName = getString(Constant.SCREEN_NAME)
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
    private fun getLeaveListRequestModel(): LeaveRequestViewRequestModel {
        val request = LeaveRequestViewRequestModel()
        request.GNETAssociateID = gnetAssociateId
        request.InnovID = innovId
        request.FromDate = ""
        request.ToDate = ""
        return request
    }

    private fun callViewLeaveRequestListApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            binding.layoutNoInternet.root.visibility = GONE
            leavesViewModel.callViewLeaveRequestListApi(getLeaveListRequestModel())
        } else {
            binding.layoutNoInternet.root.visibility = VISIBLE
        }
    }

    private fun setUpListData(data: ArrayList<ListAppliedLeaveModel>) {
        binding.apply {
            applyLeavesList.clear()
            if (!data.isNullOrEmpty()) {
                layoutNoData.root.visibility = GONE
                val pendingList = data.filter{
                    it.Status == Constant.Pending
                }
                val approvedList = data.filter{
                    it.Status == Constant.Approved
                }
                val rejectedList = data.filter{
                    it.Status == Constant.Rejected
                }
                when(screenName){
                    getString(R.string.approved_leaves) ->{
                        if (approvedList.isNotEmpty()){
                            layoutNoData.root.visibility = GONE
                            for (i in approvedList) {
                                applyLeavesList.add(
                                    ApplyLeavesModel(
                                        leaveTitle = i.RegularizationType,
                                        leaveMsg = i.Remarks,
                                        startDate = AppUtils.INSTANCE?.convertDateFormat("MM/dd/yyyy hh:mm:ss",i.RegularizationDate.toString(),"dd MMM yyyy"),
                                        endDate = "",
                                        totalDays = AppUtils.INSTANCE?.convertDateFormat("MM/dd/yyyy hh:mm:ss",i.ApprovedDate.toString(),"dd MMM yyyy"),
                                        leaveStatus = LeavesStatus.APPROVED,
                                        leavesDisplayType = LeavesType.LEAVES_HISTORY
                                    )
                                )
                                applyLeavesAdapter.notifyDataSetChanged()
                            }
                        }else{
                            layoutNoData.root.visibility = VISIBLE
                        }
                    }
                    getString(R.string.pending_leaves) ->{
                        if (pendingList.isNotEmpty()){
                            layoutNoData.root.visibility = GONE
                            for (i in pendingList) {
                                applyLeavesList.add(
                                    ApplyLeavesModel(
                                        leaveTitle = i.RegularizationType,
                                        leaveMsg = i.Remarks,
                                        startDate = AppUtils.INSTANCE?.convertDateFormat("MM/dd/yyyy hh:mm:ss",i.RegularizationDate.toString(),"dd MMM yyyy"),
                                        endDate = "",
                                        totalDays = AppUtils.INSTANCE?.convertDateFormat("MM/dd/yyyy hh:mm:ss",i.ApprovedDate.toString(),"dd MMM yyyy"),
                                        leaveStatus = LeavesStatus.PENDING,
                                        leavesDisplayType = LeavesType.LEAVES_HISTORY
                                    )
                                )
                                applyLeavesAdapter.notifyDataSetChanged()
                            }
                        }else{
                            layoutNoData.root.visibility = VISIBLE
                        }
                    }
                    getString(R.string.rejected_leaves) ->{
                        if (rejectedList.isNotEmpty()){
                            layoutNoData.root.visibility = GONE
                            for (i in rejectedList) {
                                applyLeavesList.add(
                                    ApplyLeavesModel(
                                        leaveTitle = i.RegularizationType,
                                        leaveMsg = i.Remarks,
                                        startDate = AppUtils.INSTANCE?.convertDateFormat("MM/dd/yyyy hh:mm:ss",i.RegularizationDate.toString(),"dd MMM yyyy"),
                                        endDate = "",
                                        totalDays = AppUtils.INSTANCE?.convertDateFormat("MM/dd/yyyy hh:mm:ss",i.ApprovedDate.toString(),"dd MMM yyyy"),
                                        leaveStatus = LeavesStatus.REJECTED,
                                        leavesDisplayType = LeavesType.LEAVES_HISTORY
                                    )
                                )
                                applyLeavesAdapter.notifyDataSetChanged()
                            }
                        }else{
                            layoutNoData.root.visibility = VISIBLE
                        }
                    }
                    else ->{
                        for (i in data) {
                            applyLeavesList.add(
                                ApplyLeavesModel(
                                    leaveTitle = i.RegularizationType,
                                    leaveMsg = i.Remarks,
                                    startDate = AppUtils.INSTANCE?.convertDateFormat("MM/dd/yyyy hh:mm:ss",i.RegularizationDate.toString(),"dd MMM yyyy"),
                                    endDate = "",
                                    totalDays = AppUtils.INSTANCE?.convertDateFormat("MM/dd/yyyy hh:mm:ss",i.ApprovedDate.toString(),"dd MMM yyyy"),
                                    leaveStatus = AppUtils.INSTANCE?.getLeaveStatus(i.Status ?: ""),
                                    leavesDisplayType = LeavesType.LEAVES_HISTORY
                                )
                            )
                            applyLeavesAdapter.notifyDataSetChanged()
                        }
                    }
                }


            } else {
                layoutNoData.root.visibility = VISIBLE
            }
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = screenName?:""
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
        }
    }
}