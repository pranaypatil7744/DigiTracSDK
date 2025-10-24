package com.example.digitracksdk.presentation.attendance.time_sheet

import android.os.Bundle
import android.view.View.*
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityAttendanceTimeSheetBinding
import com.example.digitracksdk.domain.model.attendance_model.AttendanceTimeSheetRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendenceWeekListModel
import com.example.digitracksdk.presentation.attendance.AttendanceViewModel
import com.example.digitracksdk.presentation.attendance.adapter.AttendanceTimeSheetAdapter
import com.example.digitracksdk.presentation.attendance.model.AttendanceTimeSheetModel
import com.example.digitracksdk.presentation.attendance.model.TimeSheetItemType
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AttendanceTimeSheetActivity : BaseActivity() {
    lateinit var binding: ActivityAttendanceTimeSheetBinding
    lateinit var attendanceTimeSheetAdapter: AttendanceTimeSheetAdapter
    private var attendanceTimeSheetList: ArrayList<AttendenceWeekListModel> = ArrayList()
    private var selectedMonthHistoryList: ArrayList<AttendanceTimeSheetModel> = ArrayList()
    private var selectedWeekPosition: Int = 0
    private val attendanceViewModel: AttendanceViewModel by viewModel()
    var endDate: String? = ""
    var startOfDate: String? = ""
    lateinit var preferenceUtils: PreferenceUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAttendanceTimeSheetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        endDate = SimpleDateFormat("dd-MMM-yyyy").format(Date())
        startOfDate = AppUtils.INSTANCE?.getCalculatedDate(endDate, "dd-MMM-yyyy", -7 * 5)
        setUpToolbar()
        setUpObserver()
        setUpTimeSheetAdapter()
        callAttendanceTimeSheetApi(startOfDate.toString(), endDate.toString())
        setUpListener()
    }

    private fun setUpObserver() {
        binding.apply {
            with(attendanceViewModel) {
                attendanceTimeSheetResponseData.observe(this@AttendanceTimeSheetActivity
                ) { it ->
                    if (it.Status == Constant.success) {
                        attendanceTimeSheetList.clear()
                        attendanceTimeSheetList.addAll(
                            it.AttendenceWeekList ?: arrayListOf()
                        )
                        attendanceTimeSheetData(it.AttendenceWeekList ?: arrayListOf())
                    } else {
                        toggleLoader(false)
                        showToast(it.Message.toString())
                    }
                }
                messageData.observe(this@AttendanceTimeSheetActivity
                ) { t ->
                    toggleLoader(false)
                    showToast(t.toString())
                }
            }
        }
    }

    private fun callAttendanceTimeSheetApi(startDate: String, endDate: String) {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                layoutNoInternet.root.visibility = GONE
                attendanceViewModel.callAttendanceTimeSheetApi(
                    request = AttendanceTimeSheetRequestModel(
                        InnovID = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID),
                        Startdate = startDate,
                        Enddate = endDate
                    )
                )
            } else {
                selectedMonthHistoryList.clear()
                attendanceTimeSheetAdapter.notifyDataSetChanged()
                layoutNoInternet.root.visibility = VISIBLE
                timeSheetTop.root.visibility = GONE
            }
        }
    }

    private fun attendanceTimeSheetData(data: ArrayList<AttendenceWeekListModel>) {
        val listSize = data.size - 1
        selectedWeekPosition = listSize
        binding.apply {
            if (selectedWeekPosition >= 0) {
                layoutNoData.root.visibility = GONE
                timeSheetTop.root.visibility = VISIBLE
                selectedMonthHistoryList.clear()
                selectedMonthHistoryList.add(
                    AttendanceTimeSheetModel(
                        "", data[listSize].AttendenceList, TimeSheetItemType.TIME_SHEET_DAILY_ITEM
                    )
                )
                timeSheetTop.apply {
                    btnNext.visibility = INVISIBLE
                    if (selectedWeekPosition - 1 == -1) {
                        btnPrev.visibility = INVISIBLE
                    }
                }
                val toData =
                    attendanceTimeSheetList[selectedWeekPosition].AttendenceList?.get(0)?.WeekStartDate
                val fromDate =
                    attendanceTimeSheetList[selectedWeekPosition].AttendenceList?.get(0)?.WeekEndDate
                val title =
                    toData.toString().replace("-", " ")+" " + getString(R.string.to)+" " +
                            fromDate.toString().replace("-", " ")
                timeSheetTop.tvCurrentMonthYear.text = title
                toggleLoader(false)
                attendanceTimeSheetAdapter.notifyDataSetChanged()
            } else {
                toggleLoader(false)
                layoutNoData.root.visibility = VISIBLE
                timeSheetTop.root.visibility = INVISIBLE
            }
        }
    }


    private fun setUpTimeSheetAdapter() {
        binding.apply {
            attendanceTimeSheetAdapter = AttendanceTimeSheetAdapter(
                this@AttendanceTimeSheetActivity,
                selectedMonthHistoryList
            )
            recyclerAttendanceHistory.adapter = attendanceTimeSheetAdapter
        }

    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            btnBack.setOnClickListener {
                finish()
            }
            tvTitle.text = getString(R.string.time_sheet)
        }
    }

    private fun setUpListener() {
        binding.apply {

            layoutNoInternet.btnTryAgain.setOnClickListener {
                callAttendanceTimeSheetApi(startOfDate.toString(), endDate.toString())
            }

            timeSheetTop.apply {
                btnPrev.setOnClickListener {
                    selectedWeekPosition -= 1
                    selectedMonthHistoryList.clear()
                    selectedMonthHistoryList.add(
                        AttendanceTimeSheetModel(
                            "",
                            attendanceTimeSheetList[selectedWeekPosition].AttendenceList,
                            TimeSheetItemType.TIME_SHEET_DAILY_ITEM
                        )
                    )
                    val toData =
                        attendanceTimeSheetList[selectedWeekPosition].AttendenceList?.get(0)?.WeekStartDate
                    val fromDate =
                        attendanceTimeSheetList[selectedWeekPosition].AttendenceList?.get(0)?.WeekEndDate
                    val title =
                        toData.toString().replace("-", " ")+" " + getString(R.string.to) +" "+
                                fromDate.toString().replace("-", " ")
                    tvCurrentMonthYear.text = title
                    if (selectedWeekPosition - 1 == -1) {
                        btnPrev.visibility = INVISIBLE
                    } else {
                        btnPrev.visibility = VISIBLE
                    }
                    btnNext.visibility = VISIBLE
                    attendanceTimeSheetAdapter.notifyDataSetChanged()
                }

                btnNext.setOnClickListener {
                    selectedWeekPosition += 1
                    selectedMonthHistoryList.clear()
                    selectedMonthHistoryList.add(
                        AttendanceTimeSheetModel(
                            "",
                            attendanceTimeSheetList[selectedWeekPosition].AttendenceList,
                            TimeSheetItemType.TIME_SHEET_DAILY_ITEM
                        )
                    )
                    val toData =
                        attendanceTimeSheetList[selectedWeekPosition].AttendenceList?.get(0)?.WeekStartDate
                    val fromDate =
                        attendanceTimeSheetList[selectedWeekPosition].AttendenceList?.get(0)?.WeekEndDate
                    val title =
                        toData.toString().replace("-", " ")+" " + getString(R.string.to) +" "+
                                fromDate.toString().replace("-", " ")
                    tvCurrentMonthYear.text = title
                    if (selectedWeekPosition + 1 == attendanceTimeSheetList.size) {
                        btnNext.visibility = INVISIBLE
                    } else {
                        btnNext.visibility = VISIBLE
                    }
                    btnPrev.visibility = VISIBLE
                    attendanceTimeSheetAdapter.notifyDataSetChanged()
                }
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
}