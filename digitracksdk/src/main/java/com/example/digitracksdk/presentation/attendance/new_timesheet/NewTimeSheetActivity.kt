package com.example.digitracksdk.presentation.attendance.new_timesheet

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityNewTimeSheetBinding
import com.example.digitracksdk.databinding.ItemCalendarDayHeaderBinding
import com.example.digitracksdk.databinding.ItemCalendarItemBinding
import com.example.digitracksdk.domain.model.attendance_model.AttendanceCycleRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceValidationRequestModel
import com.example.digitracksdk.domain.model.attendance_model.LeaveHexCodeRequestModel
import com.example.digitracksdk.domain.model.attendance_model.LeaveHexData
import com.example.digitracksdk.domain.model.attendance_model.ListViewAttendanceModel
import com.example.digitracksdk.domain.model.attendance_model.ViewAttendanceRequestModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.InsertAttendanceRegularizationRequestModel
import com.example.digitracksdk.presentation.attendance.AttendanceViewModel
import com.example.digitracksdk.presentation.attendance.CandidateViewModel
import com.example.digitracksdk.presentation.attendance.view_attendance_new.adapter.ViewAttendanceDialogAdapter
import com.example.digitracksdk.presentation.attendance.view_attendance_new.model.DialogModel
import com.example.digitracksdk.presentation.leaves.apply_leave.ApplyLeaveActivity
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.Exception
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.ArrayList
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class NewTimeSheetActivity : BaseActivity() {
    lateinit var binding: ActivityNewTimeSheetBinding
    private val attendanceViewModel: AttendanceViewModel by viewModel()
    private val candidateViewModel: CandidateViewModel by viewModel()
    var colorAttendanceDateList = ArrayList<ListViewAttendanceModel>()
    val calendarDay = ArrayList<CalendarDay?>()
    val hexacolor = ArrayList<String>()
    lateinit var preferenceUtils: PreferenceUtils
    var startDate: CalendarDay? = null
    var endDate: CalendarDay? = null
    var leaveHexData = ArrayList<LeaveHexData>()
    var selectedDates = arrayListOf<CalendarDay>()
    var successResultMap = mutableMapOf<Int, String>()
    var maxCount = 0
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNewTimeSheetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setObserver()
        callAttendanceCycleApi()
        callLeaveCodeApi()
        setUpListener()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setObserver() {
        with(attendanceViewModel)
        {
            attendanceCycleResponseData.observe(this@NewTimeSheetActivity) {
                toggleLoader(false)
                val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH)
                val formatter1 = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
                try {
                    if (!it.StartDate.isNullOrEmpty()) {
                        startDate = CalendarDay(
                            LocalDate.parse(it.StartDate, formatter)!!,
                            DayPosition.MonthDate
                        )

                    }
                    if (!it.EndDate.isNullOrEmpty()) {
                        endDate = CalendarDay(
                            LocalDate.parse(it.EndDate, formatter)!!,
                            DayPosition.MonthDate
                        )
                    }
                } catch (e: Exception) {
                    try {
                        if (!it.StartDate.isNullOrEmpty()) {
                            startDate = CalendarDay(
                                LocalDate.parse(it.StartDate, formatter1)!!,
                                DayPosition.MonthDate
                            )

                        }
                        if (!it.EndDate.isNullOrEmpty()) {
                            endDate = CalendarDay(
                                LocalDate.parse(it.EndDate, formatter1)!!,
                                DayPosition.MonthDate
                            )

                        }
                    } catch (e: Exception) {

                    }
                }
                callApi(it.StartDate.toString(), it.EndDate.toString())
            }
            leaveHexCodeResponseData.observe(this@NewTimeSheetActivity) {
                toggleLoader(false)
                if (it.status.equals(Constant.success, true)) {
                    if (!it?.lstAttendance.isNullOrEmpty()) {
                        val data = it?.lstAttendance
                        leaveHexData = data ?: ArrayList()
                        setUpAdapter(data ?: arrayListOf())
                    }

                } else {
                    showToast(it.Message.toString())
                }

            }
            viewAttendanceResponseData.observe(this@NewTimeSheetActivity) {
                toggleLoader(false)
                if (it.Status?.lowercase() == Constant.success) {
                    if (!it.LSTAttendaceTimeAsheetDetails.isNullOrEmpty()) {
                        colorAttendanceDateList.clear()
                        colorAttendanceDateList = it?.LSTAttendaceTimeAsheetDetails ?: arrayListOf()
                        setUpCalendar()
                        calendarDay.clear()
                        hexacolor.clear()
                        for (i in colorAttendanceDateList) {
                            val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.US)
                            val formatter1 = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.US)
                            lateinit var startDate: CalendarDay
                            if (!i.AttendanceDate.isNullOrEmpty()) {
                                try {
                                    startDate = CalendarDay(
                                        LocalDate.parse(i.AttendanceDate, formatter)!!,
                                        DayPosition.MonthDate
                                    )

                                } catch (e: Exception) {
                                    try {
                                        startDate = CalendarDay(
                                            LocalDate.parse(i.AttendanceDate, formatter1)!!,
                                            DayPosition.MonthDate
                                        )
                                    } catch (e: Exception) {

                                    }

                                }
                            }
                            calendarDay.add(startDate)
                            hexacolor.add((if (i.HexCode == "HexCode") "#f61f38" else i.HexCode).toString())
                        }

                    } else {
                        showToast(getString(R.string.no_data_found))
                    }
                } else {
                    showToast(it.Message.toString())
                }

            }
            attendanceValidationResponseData.observe(this@NewTimeSheetActivity) {
                val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH)
                val selectedDatePosition = selectedDates.indexOf(
                    CalendarDay(
                        LocalDate.parse(it.date, formatter),
                        DayPosition.MonthDate
                    )
                )

                if (it.status?.lowercase() == Constant.success) {
                    if (it.Message?.lowercase() == Constant.success) {
                        callAttendanceRegularizationApi(selectedDatePosition)
                    } else {
                        successResultMap[selectedDatePosition] =
                            if (it != null && !it.Message.isNullOrEmpty())
                                it.Message.toString() else ""
                        count += 1
                        checkIsHitAll()
                    }
                } else {
                    showToast(it.Message.toString())
                    successResultMap[selectedDatePosition] = it.Message.toString()
                    count += 1
                    checkIsHitAll()
                }
            }
            messageData.observe(this@NewTimeSheetActivity) {
                toggleLoader(false)
                showToast(msg = it.toString())
            }

        }
        with(candidateViewModel) {
            candidateAttendanceResponseData.observe(this@NewTimeSheetActivity) {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)

                val selectedDatePosition = selectedDates.indexOf(
                    CalendarDay(
                        LocalDate.parse(it.date, formatter),
                        DayPosition.MonthDate
                    )
                )
                if (!it.Status.isNullOrEmpty()) when (it.Status!!.toInt()) {
                    1 -> {
                        successResultMap[selectedDatePosition] = "Successful"
                        count += 1
                        checkIsHitAll()
                    }

                    2 -> {
                        successResultMap[selectedDatePosition] = "Already Applied"
                        count += 1
                        checkIsHitAll()
                    }

                    else -> {
                        successResultMap[selectedDatePosition] = "Applied Failed"
                        count += 1
                        checkIsHitAll()
                    }
                } else {
                    successResultMap[selectedDatePosition] = "Failure"
                    count += 1
                    checkIsHitAll()
                }
            }
            progressBar.observe(this@NewTimeSheetActivity) {
                toggleLoader(false)

            }
            messageData.observe(this@NewTimeSheetActivity) {
                showToast(it)
            }
        }


    }

    private fun getInsertAttendanceRegularizationRequestModel(position: Int): InsertAttendanceRegularizationRequestModel {
        val request = InsertAttendanceRegularizationRequestModel()
        val regularizationDate = AppUtils.INSTANCE?.convertDateFormat(
            dateFormatToRead = "dd MMM yyyy",
            dateToRead = selectedDates[position].date.toString().trim(),
            dateFormatToConvert = "yyyy-MMM-dd"
        )
        //2023-Jan-13

        request.apply {
            Empcode = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
            RequestTypeId = 2
            RegularizationDate = regularizationDate ?: ""
            ToDate = regularizationDate ?: ""
            InTime = ""
            OutTime = ""
            Location = "null"
            Remarks = "null"
        }
        return request

    }

    private fun callAttendanceRegularizationApi(position: Int) {

        candidateViewModel.callCandidateAttendanceApi(
            getInsertAttendanceRegularizationRequestModel(position)
        )
    }

    private fun setUpCalendar() {

        binding.calendarView.apply {

            class DayViewContainer(view: View) : ViewContainer(view) {
                lateinit var day: CalendarDay
                val itemCalendarItemBinding = ItemCalendarItemBinding.bind(view)
                val textView = itemCalendarItemBinding.calendarDayText
                val cardView = itemCalendarItemBinding.calendarDayCard

                init {
                    cardView.setOnClickListener {
                        if (selectedDates.contains(day)) {
                            selectedDates.remove(day)
                        } else {
                            selectedDates.add(day)
                        }
                        binding.calendarView.notifyCalendarChanged()
                        binding.quickRegularize.visibility =
                            if (selectedDates.size > 0) View.VISIBLE else View.GONE
                    }
                }
            }

            class MonthViewContainer(view: View) : ViewContainer(view) {
                val itemCalendarDayHeaderBinding = ItemCalendarDayHeaderBinding.bind(view)
                val textViewMonth = itemCalendarDayHeaderBinding.tvMonth
            }

            dayBinder = object : MonthDayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)

                @SuppressLint("ResourceAsColor")
                override fun bind(container: DayViewContainer, data: CalendarDay) {
                    container.day = data
                    container.textView.text = data.date.dayOfMonth.toString()
                    if (data.position == DayPosition.MonthDate) {
                        if (data.date < startDate?.date || data.date > endDate?.date) {
                            (container.textView.parent as View).visibility = View.GONE
                        } else {
                            container.textView.visibility = View.VISIBLE
                            if (selectedDates.contains(data)) {
                                container.textView.setTextColor(
                                    ContextCompat.getColor(
                                        this@NewTimeSheetActivity,
                                        R.color.white
                                    )
                                )
                                container.cardView.setCardBackgroundColor(
                                    ColorStateList.valueOf(
                                        R.color.gray
                                    )
                                )
                                container.textView.setBackgroundResource(R.color.transparent)
                            } else {
                                val c = calendarDay.indexOf(data)

                                container.cardView.setCardBackgroundColor(
                                    ColorStateList.valueOf(
                                        Color.parseColor(hexacolor[c])
                                    )
                                )
                                val hexData =
                                    leaveHexData.firstOrNull { it.HexCode == hexacolor[c] }
                                if (hexData?.LeaveType == "Not Marked" || hexData?.LeaveType == "Present") {
                                    container.textView.setTextColor(
                                        ContextCompat.getColor(
                                            this@NewTimeSheetActivity,
                                            R.color.white
                                        )
                                    )
                                } else {
                                    container.textView.setTextColor(
                                        ContextCompat.getColor(
                                            this@NewTimeSheetActivity,
                                            R.color.black
                                        )
                                    )
                                }
                            }
                        }

                    } else {
                        (container.textView.parent as View).visibility = View.GONE
                    }
                }

            }
            monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    val formatter: DateTimeFormatter =
                        DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)
                    container.textViewMonth.text = formatter.format(data.yearMonth)

                }

                override fun create(view: View) = MonthViewContainer(view)

            }

            val currentMonth = YearMonth.now() //2023-02
            val startMonth = startDate?.date?.yearMonth ?: currentMonth
            val endMonth = endDate?.date?.yearMonth ?: currentMonth
            val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

            val daysOfWeek = daysOfWeek(DayOfWeek.SUNDAY) //7

            setup(startMonth, endMonth, daysOfWeek.first())
            scrollToMonth(currentMonth)
        }
    }

    private fun setUpAdapter(list: ArrayList<LeaveHexData>) {
        binding.chipGroup.removeAllViews()
        repeat(list.size)
        {
            val chip = Chip(this@NewTimeSheetActivity)
            chip.apply {
                chipIconSize = 72f
                text = list[it].LeaveType.toString()
                val d = ContextCompat.getDrawable(
                    this@NewTimeSheetActivity,
                    R.drawable.ic_launcher_background
                )

                layoutDirection = View.LAYOUT_DIRECTION_LTR
                d?.setTint(
                    Color.parseColor(
                        if (list[it].HexCode!!.contains("#"))
                            list[it].HexCode!! else "#${list[it].HexCode!!}"
                    )
                )
                chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this@NewTimeSheetActivity,
                        R.color.white
                    )
                )

                chipIcon = d

                setTextColor(
                    ContextCompat.getColor(
                        this@NewTimeSheetActivity,
                        R.color.eerieBlack
                    )
                )

                isCheckable = false
                isClickable = false
                iconStartPadding = 10f

            }
            binding.chipGroup.addView(chip)
        }

    }

    private fun callApi(startDate: String, endDate: String) {
        binding.apply {

            if (this@NewTimeSheetActivity.isNetworkAvailable()) {
                layoutNoInternet.root.visibility = View.GONE
                toggleLoader(true)
                attendanceViewModel.callViewAttendanceApi(
                    ViewAttendanceRequestModel(
                        AssociateID = preferenceUtils.getValue(Constant.PreferenceKeys.AssociateID),
                        FromDate = startDate?.replace(" ", "-") ?: "01-Jan-2023",
                        Todate = endDate?.replace(" ", "-") ?: "28-Feb-2023"
                    )
                )
            } else {
                layoutNoInternet.root.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpListener() {
        binding.apply {

            layoutNoInternet.btnTryAgain.setOnClickListener {
                callAttendanceCycleApi()
            }
            btnApplyLeave.setOnClickListener {
                val intent = Intent(this@NewTimeSheetActivity, ApplyLeaveActivity::class.java)
                val b = Bundle()
                b.putString(Constant.SCREEN_NAME, getString(R.string.attendance_calendar))
                intent.putExtras(b)
                refreshResult.launch(intent)

            }

            quickRegularize.setOnClickListener {
                maxCount = selectedDates.size
                count = 0
                if (maxCount != 0) {
                    toggleLoader(true)

                    updateAttendance(selectedDates)
                } else {
                    showToast("Please Select First")
                }
            }
        }
    }

    private val refreshResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                callAttendanceCycleApi()

            }
        }

    private fun updateAttendance(selectedDates: ArrayList<CalendarDay>) {
        successResultMap.clear()
        for (i in 0 until selectedDates.size) {
            attendanceViewModel.callAttendanceValidationApi(
                request = attendanceValidationRequestModel(i, selectedDates[i])
            )
        }
    }

    private fun attendanceValidationRequestModel(
        index: Int,
        list: CalendarDay
    ): AttendanceValidationRequestModel {
        val formatter1: DateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH)
        val regularizationDateValidate: String = list.date.format(formatter1)
        val attendanceValidationRequestModel = AttendanceValidationRequestModel()
        attendanceValidationRequestModel.let {
            it.GNETAssociateID = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
            it.FromDate = regularizationDateValidate
            it.ToDate = regularizationDateValidate
            it.InnovID = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
            it.Type = "AR"

        }
        return attendanceValidationRequestModel
    }


    private fun checkIsHitAll() {
        if (count == maxCount) {
            toggleLoader(false)
            createDialog()
        }
    }

    private fun createDialog() {
        val dialog = Dialog(this@NewTimeSheetActivity)
        dialog.apply {

            setContentView(R.layout.dialog_view_attendence)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerView)
            val dismiss = dialog.findViewById<TextView>(R.id.btnSubmit)

            val adap = ViewAttendanceDialogAdapter(
                getDialogData()
            )

            recyclerView.adapter = adap
            dismiss.setOnClickListener {
                selectedDates.clear()
//                setUpCalendar()
                callAttendanceCycleApi()
                dialog.dismiss()
            }
            show()
        }

    }

    private fun getDialogData(): ArrayList<DialogModel> {

        val list = ArrayList<DialogModel>()

        try {
            list.clear()
            for (i in 0 until count) {
                val regularizationDate = AppUtils.INSTANCE?.convertDateFormat(
                    dateFormatToRead = "yyyy-MM-dd",
                    dateToRead = selectedDates[i].date.toString().trim(),
                    dateFormatToConvert = "dd-MMM-yyyy"
                )
                if (successResultMap[i]!!.count { it == ' ' } > 2)
                    list.add(
                        DialogModel(
                            title = "($regularizationDate) ${successResultMap[i]}",
                            subTitle = ""
                        )
                    )
                else
                    list.add(
                        DialogModel(
                            title = "Attendance on $regularizationDate is ${" " + successResultMap[i]}",
                            subTitle = ""
                        )
                    )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    private fun callLeaveCodeApi() {
        binding.apply {
            if (this@NewTimeSheetActivity.isNetworkAvailable()) {
                layoutNoInternet.root.visibility = View.GONE
                toggleLoader(true)
                attendanceViewModel.callLeaveHexCodeApi(
                    LeaveHexCodeRequestModel(
                        GnetassociateId = preferenceUtils.getValue(
                            Constant.PreferenceKeys.GnetAssociateID
                        )
                    )
                )
            } else {
                layoutNoInternet.root.visibility = View.VISIBLE
            }

        }
    }

    private fun callAttendanceCycleApi() {
        binding.apply {
            if (this@NewTimeSheetActivity.isNetworkAvailable()) {
                layoutNoInternet.root.visibility = View.GONE
                toggleLoader(true)
                attendanceViewModel.callAttendanceCycleApi(
                    AttendanceCycleRequestModel(
                        GnetassociateId = preferenceUtils.getValue(
                            Constant.PreferenceKeys.GnetAssociateID
                        )
                    )
                )
            } else {
                layoutNoInternet.root.visibility = View.VISIBLE
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
            tvTitle.text = getString(R.string.attendance_calendar)
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = View.VISIBLE
        }

    }
}