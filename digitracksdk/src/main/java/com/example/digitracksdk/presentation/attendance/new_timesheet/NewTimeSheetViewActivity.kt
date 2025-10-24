package com.example.digitracksdk.presentation.attendance.new_timesheet

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.graphics.Color.parseColor
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityNewTimeSheetViewBinding
import com.example.digitracksdk.databinding.ItemCalendarDayHeaderBinding
import com.example.digitracksdk.databinding.ItemCalendarItemBinding
import com.example.digitracksdk.domain.model.attendance_model.AttendanceCycleRequestModel
import com.example.digitracksdk.domain.model.attendance_model.LeaveHexCodeRequestModel
import com.example.digitracksdk.domain.model.attendance_model.LeaveHexData
import com.example.digitracksdk.domain.model.attendance_model.ListViewAttendanceModel
import com.example.digitracksdk.domain.model.attendance_model.ViewAttendanceRequestModel
import com.example.digitracksdk.presentation.attendance.AttendanceViewModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import com.google.android.material.chip.Chip
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Calendar
import java.util.Date
import java.util.Locale

class NewTimeSheetViewActivity : BaseActivity() {
    lateinit var binding: ActivityNewTimeSheetViewBinding
    private val attendanceViewModel: AttendanceViewModel by viewModel()
    var colorAttendanceDateList = ArrayList<ListViewAttendanceModel>()
    var selectedDates = arrayListOf<CalendarDay>()
    val calendarDay = ArrayList<CalendarDay?>()
    val hexacolor = ArrayList<String>()
    var leaveHexData = java.util.ArrayList<LeaveHexData>()
    lateinit var preferenceUtils: PreferenceUtils
    val cal = Calendar.getInstance()

    var startDate: CalendarDay? = null
    var endDate: CalendarDay? = null
    var selectedFromDate: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNewTimeSheetViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setObserver()
        callAttendanceCycleApi()
        callLeaveCodeApi()
        setUpListener()

    }

    private fun setObserver() {
        with(attendanceViewModel) {

            attendanceCycleResponseData.observe(this@NewTimeSheetViewActivity) {
                toggleLoader(false)
                val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH)
                val formatter1 = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
                try {
                    if (!it.StartDate.isNullOrEmpty()) {
                        startDate = CalendarDay(
                            LocalDate.parse(it.StartDate, formatter)!!,
                            DayPosition.MonthDate
                        )
                        binding.etFromDate.setText(it.StartDate.toString())

                    }
                    if (!it.EndDate.isNullOrEmpty()) {
                        endDate = CalendarDay(
                            LocalDate.parse(it.EndDate, formatter)!!,
                            DayPosition.MonthDate
                        )
                        binding.etEndDate.setText(it.EndDate.toString())
                    }
                } catch (e: Exception) {
                    try {
                        if (!it.StartDate.isNullOrEmpty()) {
                            startDate = CalendarDay(
                                LocalDate.parse(it.StartDate, formatter1)!!,
                                DayPosition.MonthDate
                            )
                            binding.etFromDate.setText(it.StartDate.toString().replace(" ", "-"))

                        }
                        if (!it.EndDate.isNullOrEmpty()) {
                            endDate = CalendarDay(
                                LocalDate.parse(it.EndDate, formatter1)!!,
                                DayPosition.MonthDate
                            )
                            binding.etEndDate.setText(it.EndDate.toString().replace(" ", "-"))

                        }
                    } catch (e: Exception) {

                    }
                }
                callApi()
            }

            leaveHexCodeResponseData.observe(this@NewTimeSheetViewActivity) {
                toggleLoader(false)
                if (it.status.equals(Constant.success, true)) {
                    if (!it?.lstAttendance.isNullOrEmpty()) {
                        val data = it?.lstAttendance ?: java.util.ArrayList()
                        leaveHexData = data

                        setUpAdapter(data ?: arrayListOf())
                    }

                } else {
                    showToast(it.Message.toString())
                }

            }

            viewAttendanceResponseData.observe(this@NewTimeSheetViewActivity) {
                toggleLoader(false)
                if (it.Status.equals(Constant.SUCCESS, true)) {

                    if (!it.LSTAttendaceTimeAsheetDetails.isNullOrEmpty()) {
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

                    }
                } else {
                    showToast(it.Message.toString())
                }
                toggleLoader(false)
            }

            messageData.observe(this@NewTimeSheetViewActivity) {
                toggleLoader(false)
                showToast(msg = it.toString())
            }

        }

    }

    private fun setUpAdapter(list: ArrayList<LeaveHexData>) {
        binding.chipGroup.removeAllViews()
        repeat(list.size) {

            val chip = Chip(this@NewTimeSheetViewActivity)
            chip.apply {
                chipIconSize = 72f
                text = list[it].LeaveType.toString()
                val d = ContextCompat.getDrawable(
                    this@NewTimeSheetViewActivity,
                    R.drawable.ic_launcher_background
                )
                layoutDirection = View.LAYOUT_DIRECTION_LTR
                d?.setTint(
                    parseColor(
                        if (list[it].HexCode!!.contains("#"))
                            list[it].HexCode!! else "#${list[it].HexCode!!}"
                    )
                )
                chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this@NewTimeSheetViewActivity,
                        R.color.white
                    )
                )
                chipIcon = d
                setTextColor(
                    ContextCompat.getColor(
                        this@NewTimeSheetViewActivity,
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

    private fun setUpCalendar() {
        binding.calendarView.apply {
            class DayViewContainer(view: View) : ViewContainer(view) {
                // Will be set when this container is bound. See the dayBinder.
                lateinit var day: CalendarDay
                val itemCalendarItemBinding = ItemCalendarItemBinding.bind(view)
                val textView = itemCalendarItemBinding.calendarDayText
                val cardView = itemCalendarItemBinding.calendarDayCard
            }

            class MonthViewContainer(view: View) : ViewContainer(view) {
                // Will be set when this container is bound. See the dayBinder.
                val itemCalendarDayHeaderBinding = ItemCalendarDayHeaderBinding.bind(view)
                val textViewMonth = itemCalendarDayHeaderBinding.tvMonth

            }

            dayBinder = object : MonthDayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)

                @SuppressLint("ResourceType")
                override fun bind(container: DayViewContainer, data: CalendarDay) {
                    container.day = data
//                    val textView = container.textView
                    container.textView.text = data.date.dayOfMonth.toString()


                    //date 2023-01-29
                    if (data.position == DayPosition.MonthDate) {

                        if (data.date < startDate?.date || data.date > endDate?.date) {
                            (container.textView.parent as View).visibility = View.GONE

                        } else {

                            container.textView.visibility = View.VISIBLE
                            if (selectedDates.contains(data)) {
                                container.textView.setTextColor(
                                    ContextCompat.getColor(
                                        this@NewTimeSheetViewActivity,
                                        R.color.white
                                    )
                                )
//                                container.textView.setBackgroundResource(R.color.red)

//                                    container.cardView.setCardBackgroundColor(
//                                        ColorStateList.valueOf(
//                                            ContextCompat.getColor(
//                                                this@NewTimeSheetActivity, parseColor(hexacolor[c])
//                                            )
//                                        )
//                                    )
                                container.cardView.setCardBackgroundColor(
                                    ColorStateList.valueOf(
                                        R.color.colorGray
                                    )
                                )
                                container.textView.setBackgroundResource(R.color.transparent)
                            } else {
                                val c = calendarDay.indexOf(data)

                                container.cardView.setCardBackgroundColor(
                                    ColorStateList.valueOf(
                                        parseColor(hexacolor[c])
                                    )
                                )

                                val hexData =
                                    leaveHexData.firstOrNull { it.HexCode == hexacolor[c] }

                                if (hexData?.LeaveType == "Not Marked" || hexData?.LeaveType == "Present") {
                                    container.textView.setTextColor(
                                        ContextCompat.getColor(
                                            this@NewTimeSheetViewActivity,
                                            R.color.white
                                        )
                                    )
                                } else {
                                    container.textView.setTextColor(
                                        ContextCompat.getColor(
                                            this@NewTimeSheetViewActivity,
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


            monthHeaderBinder =
                object : MonthHeaderFooterBinder<MonthViewContainer> {
                    override fun bind(container: MonthViewContainer, data: CalendarMonth) {
//                    val month = data.yearMonth.month.value
//                    val year = data.yearMonth.year
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

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpListener() {
        binding.apply {

            layoutNoInternet.btnTryAgain.setOnClickListener {

                callAttendanceCycleApi()
                callLeaveCodeApi()
            }

            etFromDate.setOnTouchListener { v, event ->

                val cal = Calendar.getInstance()
                var y = cal.get(Calendar.YEAR)
                var m = cal.get(Calendar.MONTH)
                var d = cal.get(Calendar.DAY_OF_MONTH)


                if (event.action == MotionEvent.ACTION_DOWN) {
                    val datePickerDialog = DatePickerDialog(
                        this@NewTimeSheetViewActivity,
                        { view, year, monthOfYear, dayOfMonth ->

                            cal.set(Calendar.YEAR, year)
                            cal.set(Calendar.MONTH, monthOfYear)
                            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                            selectedFromDate = AppUtils.INSTANCE?.convertDateToString(
                                cal.time,
                                Constant.SIMPLE_DATE_FORMAT
                            ).toString()
                            etFromDate.setText(selectedFromDate)
                            etEndDate.setText("")
                        }, y, m, d
                    )

                    val referenceDate = Date()
                    val c = Calendar.getInstance()
                    c.time = referenceDate
                    c.add(Calendar.MONTH, -3)
                    datePickerDialog.datePicker.minDate = c.timeInMillis
                    datePickerDialog.datePicker.maxDate = Date().time

                    datePickerDialog.setOnCancelListener {
                        datePickerDialog.dismiss()
                    }
                    datePickerDialog.show()
                }
                true

            }

            etEndDate.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val cal = Calendar.getInstance()
                    val y = cal.get(Calendar.YEAR)
                    val m = cal.get(Calendar.MONTH)
                    val d = cal.get(Calendar.DAY_OF_MONTH)

                    val datePickerDialog = DatePickerDialog(
                        this@NewTimeSheetViewActivity,
                        { view, year, monthOfYear, dayOfMonth ->

                            cal.set(Calendar.YEAR, year)
                            cal.set(Calendar.MONTH, monthOfYear)
                            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                            val selectedDate = AppUtils.INSTANCE?.convertDateToString(
                                cal.time,
                                Constant.SIMPLE_DATE_FORMAT
                            )
                            etEndDate.setText(selectedDate)
                        }, y, m, d
                    )

                    if (selectedFromDate.isNullOrEmpty()) {
                        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                        val c = Calendar.getInstance()
                        c.add(Calendar.MONTH, -3)
                        datePickerDialog.datePicker.minDate = c.timeInMillis

                    } else {
                        val fromDate =
                            SimpleDateFormat("dd-MMM-yyyy", Locale.US).parse(selectedFromDate)
                        datePickerDialog.datePicker.minDate = fromDate.time
                        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                    }
                    datePickerDialog.setOnCancelListener {
                        datePickerDialog.dismiss()
                    }
                    datePickerDialog.show()
                }
                true

            }

            btnSubmit.setOnClickListener {
                if (etFromDate.text.isNullOrEmpty()) {
                    showToast(getString(R.string.please_choose_from_date))
                } else if (etEndDate.text.isNullOrEmpty()) {
                    showToast(getString(R.string.please_choose_end_date))
                } else {
                    callApi()
                }
            }
        }
    }

    private fun callApi() {
        binding.apply {
            val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.US)
            try {
                startDate = CalendarDay(
                    LocalDate.parse(etFromDate.text.toString(), formatter)!!,
                    DayPosition.MonthDate
                )


                endDate = CalendarDay(
                    LocalDate.parse(etEndDate.text.toString(), formatter)!!,
                    DayPosition.MonthDate
                )

            } catch (e: Exception) {

            }

            callDetailTimeSheetApi(etFromDate.text.toString(), etEndDate.text.toString())

        }

    }

    private fun callDetailTimeSheetApi(startDate: String, endDate: String) {

        binding.apply {

            if (this@NewTimeSheetViewActivity.isNetworkAvailable()) {
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

    private fun callLeaveCodeApi() {
        binding.apply {
            if (this@NewTimeSheetViewActivity.isNetworkAvailable()) {
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
            if (this@NewTimeSheetViewActivity.isNetworkAvailable()) {
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
            tvTitle.text = getString(R.string.view_calendar)
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = View.VISIBLE
        }

    }
}
