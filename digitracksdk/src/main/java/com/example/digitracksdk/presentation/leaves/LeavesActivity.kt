package com.example.digitracksdk.presentation.leaves

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityLeavesBinding
import com.example.digitracksdk.databinding.BottomSheetHolidayListBinding
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.leaves.ListHolidaysModel
import com.example.digitracksdk.presentation.leaves.adapter.HolidayListAdapter
import com.example.digitracksdk.presentation.leaves.apply_leave.ApplyLeaveActivity
import com.example.digitracksdk.presentation.leaves.model.HolidayListModel
import com.example.digitracksdk.presentation.my_letters.adapter.MyLettersAdapter
import com.example.digitracksdk.presentation.my_letters.model.MyLettersModel
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.digitracksdk.presentation.leaves.leave_status.LeaveStatusActivity
import com.example.digitracksdk.utils.AppUtils
import org.koin.android.viewmodel.ext.android.viewModel

class LeavesActivity : BaseActivity(), MyLettersAdapter.MyLettersClickManager {
    lateinit var binding: ActivityLeavesBinding
    private val leavesViewModel: LeavesViewModel by viewModel()
    lateinit var preferenceUtils: PreferenceUtils
    lateinit var bottomSheetHolidayListBinding: BottomSheetHolidayListBinding
    lateinit var holidayListAdapter: HolidayListAdapter
    private var holidayList: ArrayList<HolidayListModel> = ArrayList()
    private var leavesList: ArrayList<MyLettersModel> = ArrayList()
    lateinit var leavesAdapter: MyLettersAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLeavesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setUpObserver()
        setUpLeavesData()
    }

    private fun setUpObserver() {
        binding.apply {
            with(leavesViewModel) {
                holidaysListResponse.observe(this@LeavesActivity) {
                    if (it.Status == Constant.success) {
                        val data: ArrayList<ListHolidaysModel>? = it.LstHolidays
                        bottomSheetHolidayListBinding.layoutNoData.root.visibility = GONE
                        holidayList.clear()
                        if (data != null) {
                            for (d in data) {
                                holidayList.add(
                                    HolidayListModel(
                                        date = d.HolidayDate,
                                        holidayName = d.HolidayName
                                    )
                                )
                            }
                            holidayListAdapter.notifyDataSetChanged()
                        } else {
                            bottomSheetHolidayListBinding.apply {
                                layoutNoData.root.visibility = VISIBLE
                                layoutNoData.tvNoData.text = it.Message
                            }
                        }
                    } else {
                        showToast(getString(R.string.something_went_wrong))
                    }
                }

                messageData.observe(this@LeavesActivity) {
                    showToast(it)
                }

                showProgressBar.observe(this@LeavesActivity) {
                    toggleLoader(it)
                }

            }
        }
    }

    private fun setUpLeavesData() {
        leavesList.clear()
        leavesList.add(
            MyLettersModel(
                itemName = getString(R.string.apply_leave), itemIcon = R.drawable.ic_leaves
            )
        )
        leavesList.add(
            MyLettersModel(
                itemName = getString(R.string.holiday_list), itemIcon = R.drawable.ic_holiday
            )
        )
        leavesList.add(
            MyLettersModel(
                itemName = getString(R.string.leave_status), itemIcon = R.drawable.ic_leave_status
            )
        )
        setUpLeaveAdapter()
    }

    private fun setUpLeaveAdapter() {
        leavesAdapter = MyLettersAdapter(this, leavesList, this)
        binding.recyclerLeaves.adapter = leavesAdapter
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.leaves)
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
        }
    }

    override fun onClickItems(position: Int) {
        when (leavesList[position].itemName) {
            getString(R.string.apply_leave) -> {
                startActivity(Intent(this, ApplyLeaveActivity::class.java))
            }
            getString(R.string.holiday_list) -> {
                openHolidayListBottomSheet()
            }
            getString(R.string.leave_status) -> {
                startActivity(Intent(this, LeaveStatusActivity::class.java))
            }
        }
    }

    private fun callHolidaysListApi() {
        if (isNetworkAvailable()) {
            leavesViewModel.callHolidaysListApi(
                CommonRequestModel(
                    InnovId = preferenceUtils.getValue(
                        Constant.PreferenceKeys.INNOV_ID
                    )
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }

    }

    private fun openHolidayListBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_holiday_list, null)
        bottomSheetHolidayListBinding = BottomSheetHolidayListBinding.bind(view)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.setCancelable(true)
        bottomSheetHolidayListBinding.apply {
            holidayListAdapter = HolidayListAdapter(this@LeavesActivity, holidayList)
            recyclerHolidayList.adapter = holidayListAdapter
            btnClose.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
        }
        callHolidaysListApi()
        bottomSheetDialog.show()
    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            bottomSheetHolidayListBinding.contentLoading.root,
            bottomSheetHolidayListBinding.contentLoading.imageLoading,
            showLoader
        )
    }
}