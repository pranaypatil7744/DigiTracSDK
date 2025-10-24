package com.example.digitracksdk.utils

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings.Secure
import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.presentation.attendance.model.AttendanceStatus
import com.example.digitracksdk.presentation.leaves.apply_leave.model.LeavesStatus
import com.example.digitracksdk.presentation.my_letters.candidate_loi.model.CandidateLoiStatus
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.regex.Matcher
import java.util.regex.Pattern


class AppUtils {

    lateinit var preferenceUtils: PreferenceUtils

    companion object {
        var INSTANCE: AppUtils? = null

        fun setInstance() {
            if (INSTANCE == null) {
                INSTANCE = AppUtils()
            }
        }
    }

    fun logMe(tag: String, message: String?) {
//        if (BuildConfig.DEBUG) {
//            Log.e(tag, message ?: "")
//        }
    }

    fun setLang(context: Context) {
        when (preferenceUtils.getValue(Constant.SELECTED_LANGUAGE)) {
            Constant.Languages.Hindi -> {
                LocaleHelper.setLocale(context, Constant.Languages.Hindi)
                preferenceUtils.setValue(Constant.SELECTED_LANGUAGE, Constant.Languages.Hindi)
            }

            Constant.Languages.Marathi -> {
                LocaleHelper.setLocale(context, Constant.Languages.Marathi)
                preferenceUtils.setValue(Constant.SELECTED_LANGUAGE, Constant.Languages.Marathi)
            }

            Constant.Languages.Gujarati -> {
                LocaleHelper.setLocale(context, Constant.Languages.Gujarati)
                preferenceUtils.setValue(Constant.SELECTED_LANGUAGE, Constant.Languages.Gujarati)
            }

            else -> {
                LocaleHelper.setLocale(context, Constant.Languages.English)
                preferenceUtils.setValue(Constant.SELECTED_LANGUAGE, Constant.Languages.English)
            }
        }
    }

    fun getPdfToBase64(uri: Uri, context: Context): ByteArray? {
        try {
            val iStream: InputStream? = context.contentResolver.openInputStream(uri)
            val byteBuffer = ByteArrayOutputStream()
            val bufferSize = 1024
            val buffer = ByteArray(bufferSize)
            var len = 0
            while (iStream?.read(buffer).also {
                    if (it != null) {
                        len = it
                    }
                } != -1) {
                byteBuffer.write(buffer, 0, len)
            }
            return byteBuffer.toByteArray()
        } catch (e: java.lang.Exception) {
            return null
        }

    }

    fun getBooleanFromInt(value: Int): Boolean {
        return value == 1
    }

    fun isLocationEnabled(): Boolean {
//        val locationManager: LocationManager =
//            BaseApplication.mContext.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
//            LocationManager.NETWORK_PROVIDER
//        )
        // TODO
        return true
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String? {
        return Secure.getString(
            context.contentResolver,
            Secure.ANDROID_ID
        )
    }

    fun getFullMonthName(context: Context, month: String): String {
        return when (month) {
            Constant.MonthList.JAN -> {
                return context.getString(R.string.january)
            }

            Constant.MonthList.FEB -> {
                return context.getString(R.string.february)
            }

            Constant.MonthList.MAR -> {
                return context.getString(R.string.march)
            }

            Constant.MonthList.APR -> {
                return context.getString(R.string.april)
            }

            Constant.MonthList.MAY -> {
                return context.getString(R.string.may)
            }

            Constant.MonthList.JUN -> {
                return context.getString(R.string.june)
            }

            Constant.MonthList.JUL -> {
                return context.getString(R.string.july)
            }

            Constant.MonthList.AUG -> {
                return context.getString(R.string.august)
            }

            Constant.MonthList.SEP -> {
                return context.getString(R.string.september)
            }

            Constant.MonthList.OCT -> {
                return context.getString(R.string.october)
            }

            Constant.MonthList.NOV -> {
                return context.getString(R.string.november)
            }

            Constant.MonthList.DEC -> {
                return context.getString(R.string.december)
            }

            else -> ""
        }
    }

    fun isValidMobileNumber(s: String): Boolean {
        return if (s.isNotEmpty()) {
            val p = Pattern.compile("(0/91)?[6-9][0-9]{9}")
            val m = p.matcher(s)
            m.find() && m.group() == s
        } else {
            true
        }
    }


    //    fun showShortToast(context: Context,msg:String){
//        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
//    }
    fun showLongToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun getMonthYear(): String {
        val instance = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val day = instance.get(Calendar.DAY_OF_WEEK)
        val date = instance.get(Calendar.DAY_OF_MONTH)
        val year = instance.get(Calendar.YEAR)
        val month = instance.get(Calendar.MONTH)
        val m = SimpleDateFormat("MM", Locale.ENGLISH).format(month)
        val y = SimpleDateFormat("yyyy", Locale.ENGLISH).format(year)
        val dateFormat: DateFormat = SimpleDateFormat("MMM yyyy", Locale.ENGLISH)
        val date2 = Date()
        return dateFormat.format(date2)
    }

    fun getCurrentMonth(): Int {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        val cMonth = cal.get(Calendar.MONTH)
        return cMonth.plus(1)
    }

    fun getCurrentMonthText(): String {
        val dateFormat: DateFormat = SimpleDateFormat("MMM", Locale.ENGLISH)
        val date = Date()
        return dateFormat.format(date)
    }

    fun getCurrentYear(): Int {
        val cal = Calendar.getInstance()
        return cal.get(Calendar.YEAR)
    }

    fun getPrevYearDate(pastYears: Int): Long {
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -pastYears)
        return cal.time.time
    }

    fun getCurrentYearStartDate(): Long {
        val cYear = getCurrentYear()
        val date = "01-01-$cYear"
        val dateCal = convertStringToDate("dd-mm-yyyy", date)
        val final = dateCal.time
        return final
    }

    fun convertStringToDate(
        dateFormatToRead: String,
        dateToRead: String
    ): Date {
        return try {
            val sdf = SimpleDateFormat(dateFormatToRead, Locale.ENGLISH)
            sdf.parse(dateToRead) ?: Date()
        } catch (e: java.lang.Exception) {
            Date()
        }
    }

    fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("hh:mm", Locale.ENGLISH)
        return sdf.format(Date())
    }

    fun getCurrentDate(): String {
        val dateFormat: DateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        val date = Date()
        return dateFormat.format(date)
    }

    fun showFadeView(view: View, duration: Long) {
        view.animate().alpha(1.0f).duration = duration
    }

    fun hideFadeView(view: View, duration: Long) {
        view.animate().alpha(0.2f).duration = duration
    }

    fun showView(view: View, duration: Long) {
        view.animate().alpha(1.0f).duration = 400
    }

    fun hideView(view: View, duration: Long) {
        view.animate().alpha(0f).duration = 400
    }

    fun convertDateToString(
        date: Date,
        formatToConvert: String
    ): String {
//        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        val df = SimpleDateFormat(formatToConvert, Locale.ENGLISH)
        val formattedDate = df.format(date.time)
        return formattedDate
    }

    fun convertDateFormat(
        dateFormatToRead: String,
        dateToRead: String,
        dateFormatToConvert: String,
        timeZone: TimeZone = TimeZone.getTimeZone("UTC"),
        localTimeZone: TimeZone = TimeZone.getDefault(),
        exceptionDateFormatToRead: String? = ""
    ): String {
        /**
         * To Convert Date From One Format To Another
         * dateFormatToRead - Indicates Format in which date is Present
         * dateToRead - Has Value of Date To Read
         * dateFormatToConvert - Indicates Format in which date is expected
         */
        try {
            var sdf = SimpleDateFormat(dateFormatToRead, Locale.ENGLISH)
            sdf.timeZone = timeZone
            val date = sdf.parse(dateToRead)
            sdf = SimpleDateFormat(dateFormatToConvert, Locale.ENGLISH)
            sdf.timeZone = localTimeZone
            return sdf.format(date)
        } catch (e: java.lang.Exception) {
            return try {
                var sdf = SimpleDateFormat(exceptionDateFormatToRead, Locale.ENGLISH)
                sdf.timeZone = timeZone
                val date = sdf.parse(dateToRead)
                sdf = SimpleDateFormat(dateFormatToConvert, Locale.ENGLISH)
                sdf.timeZone = localTimeZone
                sdf.format(date)
            } catch (e: Exception) {
                dateToRead

            }
        }
    }

    fun getLeaveStatus(status: String): LeavesStatus {
        return when (status) {
            Constant.Approved -> {
                LeavesStatus.APPROVED
            }

            Constant.Rejected -> {
                LeavesStatus.REJECTED
            }

            else -> {
                LeavesStatus.PENDING
            }
        }
    }

    fun getMyLettersStatus(status: String): CandidateLoiStatus {
        return when (status) {
            Constant.Accepted, Constant.Accept -> {
                CandidateLoiStatus.ACCEPTED
            }

            Constant.Rejected, Constant.Reject -> {
                CandidateLoiStatus.REJECTED
            }

            else -> CandidateLoiStatus.AWAITING
        }
    }

    fun openTimePicker(textView: EditText, context: Context) {

        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            textView.setText(SimpleDateFormat("HH:mm", Locale.ENGLISH).format(cal.time))
        }

        textView.setOnClickListener {
            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    fun validateAadharNumber(aadharNumber: String?): Boolean {
        val aadharPattern = Pattern.compile("\\d{12}")
        var isValidAadhar =
            if (aadharNumber != null) aadharPattern.matcher(aadharNumber).matches() else false
        if (isValidAadhar) {
            isValidAadhar = AadhaarValidationAlgorithm.validateVerhoeff(aadharNumber)
        }
        return isValidAadhar
    }

    fun isValidPanCard(panCard: String): Boolean {
        val panCardPattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")
        val matcher = panCardPattern.matcher(panCard)
        return matcher.matches()
    }

    fun isValidAccountNumber(account_number: String): Boolean {
        val p = Pattern.compile("^\\d{9,18}$")
        val m = p.matcher(account_number)
        return m.find() && m.group() == account_number
    }

    fun isValidEmail(email: String?) =
        if (email != null) Patterns.EMAIL_ADDRESS.matcher(email).matches() else false

    fun checkSpecialSymbol(text: String): Boolean {
        val removedSpace = text.replace(" ", "")
        val pattern = Pattern.compile("[^A-Za-z0-9]")
        val matcher = pattern.matcher(removedSpace)
        return matcher.find()
    }

    fun isValidIFSCCode(ifsc: String): Boolean {
//        val regExp = "^[A-Za-z]{4}[a-zA-Z0-9]{7}$"
        val regExp = "^[A-Z]{4}0[A-Z0-9]{6}\$"
        val p = Pattern.compile(regExp)
        val m = p.matcher(ifsc)
        return m.find() && m.group() == ifsc
    }

    fun getYesNoFromBoolean(booleanValue: String): String {
        return when (booleanValue) {
            "false" -> {
                "No"
            }

            "true" -> {
                "Yes"
            }

            else -> {
                ""
            }
        }
    }

    fun getAttendanceStatus(inTime: String, outTime: String): AttendanceStatus {
        return if ((inTime.isEmpty() || inTime == "null") && (outTime.isEmpty() || outTime == "null")) {
            AttendanceStatus.NONE
        } else if ((inTime.isNotEmpty() || inTime != "null") && (outTime.isEmpty() || outTime == "null")) {
            AttendanceStatus.CHECK_IN
        } else {
            AttendanceStatus.CHECK_OUT
        }
    }

    fun getCalculatedDate(date: String?, dateFormat: String, days: Int): String? {
        val cal = Calendar.getInstance()
        val s = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        try {
            val myDate = s.parse(date)
            cal.time = myDate
            cal.add(Calendar.DAY_OF_YEAR, days)
            return s.format(cal.time)
        } catch (e: ParseException) {
            Log.e("TAG", " " + e.message)
        }
        return null
    }

    fun getResignationDate(noticePeriodDate: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, noticePeriodDate)
        val lastWorkingDate = INSTANCE?.convertDateToString(
            calendar.time,
            "dd MMM yyyy"
        )
        return lastWorkingDate.toString()
    }
    fun  getFormattedString(stringOne:String?="",stringTwo:String?=""): Spanned {
        val formattedString = "<b>$stringOne </b>  \t   $stringTwo"
        return HtmlCompat.fromHtml(formattedString, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }


    fun getStringImage(imageFile: String, appContext: Context): String? {
        val file = File(imageFile.trim { it <= ' ' })
        val fileUri = Uri.fromFile(file)
        try {
            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(appContext.contentResolver, fileUri)
            } else {
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(appContext.contentResolver, fileUri)
                ImageDecoder.decodeBitmap(source)
            }
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageBytes = baos.toByteArray()
            return Base64.encodeToString(imageBytes, Base64.DEFAULT)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    internal class DecimalDigitsInputFilter(digitsBeforeZero: Int, digitsAfterZero: Int) :
        InputFilter {
        private val mPattern: Pattern =
            Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?")

        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            val matcher: Matcher = mPattern.matcher(dest)
            return if (!matcher.matches()) "" else null
        }

    }

    fun sendEventsToFirebase(event: String?, appContext: Context) {
        try {
            val mFirebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(appContext)
            val bundle = Bundle()
            bundle.putString("event_name", event)
            mFirebaseAnalytics.logEvent("OnClickEvent", bundle)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun systemBarPadding( view  : View)
    {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}

open class DecimalDigitsInputFilter(digitsBeforeZero: Int?, digitsAfterZero: Int?) :
    InputFilter {
    private val mDigitsBeforeZero: Int = digitsBeforeZero ?: DIGITS_BEFORE_ZERO_DEFAULT
    private val mDigitsAfterZero: Int = digitsAfterZero ?: DIGITS_AFTER_ZERO_DEFAULT
    private val mPattern: Pattern = Pattern.compile(
        "-?[0-9]{0," + mDigitsBeforeZero + "}+((\\.[0-9]{0," + mDigitsAfterZero
                + "})?)||(\\.)?"
    )

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val replacement = source.subSequence(start, end).toString()
        val newVal = (dest.subSequence(0, dstart).toString() + replacement
                + dest.subSequence(dend, dest.length).toString())
        val matcher = mPattern.matcher(newVal)
        if (matcher.matches()) return null
        return if (TextUtils.isEmpty(source)) dest.subSequence(dstart, dend) else ""
    }

    companion object {
        private const val DIGITS_BEFORE_ZERO_DEFAULT = 100000
        private const val DIGITS_AFTER_ZERO_DEFAULT = 100000
    }

}