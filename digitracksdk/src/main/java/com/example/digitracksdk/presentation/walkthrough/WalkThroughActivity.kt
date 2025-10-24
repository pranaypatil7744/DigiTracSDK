package com.example.digitracksdk.presentation.walkthrough

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityWalkThroughBinding
import com.example.digitracksdk.presentation.login.LoginActivity
import com.example.digitracksdk.presentation.signup.SignUpActivity
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.LocaleHelper
import com.example.digitracksdk.utils.PreferenceUtils


class WalkThroughActivity : BaseActivity() {

    lateinit var preferenceUtils: PreferenceUtils
    lateinit var binding: ActivityWalkThroughBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWalkThroughBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        AppUtils.INSTANCE?.setLang(this)
        setListener()
    }


    private fun setListener() {
        binding.apply {
            btnLoginWithMobileNumber.setOnClickListener {
                startActivity(Intent(this@WalkThroughActivity, LoginActivity::class.java))
            }
            btnSignUp.setOnClickListener {
                startActivity(Intent(this@WalkThroughActivity, SignUpActivity::class.java))
            }
            tvMore.setOnClickListener {
                startActivity(Intent(this@WalkThroughActivity, SelectLanguageActivity::class.java))
            }
            tvHindi.setOnClickListener{
                LocaleHelper.setLocale(this@WalkThroughActivity, Constant.Languages.Hindi)
                preferenceUtils.setValue(Constant.SELECTED_LANGUAGE, Constant.Languages.Hindi)
                btnSignUp.text = getString(R.string.sign_up)
                btnLoginWithMobileNumber.text = getString(R.string.login_with_mobile_number)
                tvMore.text = getString(R.string.more)
                labelOr.text = getString(R.string.or)
                labelDescription.text = getString(R.string.mark_attendance_view_payslip_nrequest_for_leave_and_much_more)
            }
            tvEnglish.setOnClickListener{
                LocaleHelper.setLocale(this@WalkThroughActivity, Constant.Languages.English)
                preferenceUtils.setValue(Constant.SELECTED_LANGUAGE, Constant.Languages.English)
                btnSignUp.text = getString(R.string.sign_up)
                btnLoginWithMobileNumber.text = getString(R.string.login_with_mobile_number)
                tvMore.text = getString(R.string.more)
                labelOr.text = getString(R.string.or)
                labelDescription.text = getString(R.string.mark_attendance_view_payslip_nrequest_for_leave_and_much_more)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.apply {
            AppUtils.INSTANCE?.setLang(this@WalkThroughActivity)
            btnSignUp.text = getString(R.string.sign_up)
            btnLoginWithMobileNumber.text = getString(R.string.login_with_mobile_number)
            tvMore.text = getString(R.string.more)
            labelOr.text = getString(R.string.or)
            labelDescription.text = getString(R.string.mark_attendance_view_payslip_nrequest_for_leave_and_much_more)
        }
    }
}