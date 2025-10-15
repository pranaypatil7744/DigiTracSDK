package com.example.digitracksdk.presentation.walkthrough

import android.os.Bundle
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivitySelectLanguageBinding
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.LocaleHelper
import com.example.digitracksdk.utils.PreferenceUtils

class SelectLanguageActivity : BaseActivity(), SelectLanguageAdapter.LanguageManager {
    lateinit var binding:ActivitySelectLanguageBinding
    private lateinit var selectLanguageAdapter: SelectLanguageAdapter
    lateinit var preferenceUtils: PreferenceUtils
    private var languageList:ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySelectLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setUpLanguageList()
        setUpAdapter()
    }

    private fun setUpAdapter() {
        binding.apply {
            selectLanguageAdapter = SelectLanguageAdapter(this@SelectLanguageActivity,languageList,this@SelectLanguageActivity)
            recyclerLanguage.adapter = selectLanguageAdapter
        }
    }

    private fun setUpLanguageList() {
        languageList.clear()
        languageList.add(getString(R.string.english))
        languageList.add(getString(R.string.hindi))
        languageList.add(getString(R.string.marathi))
        languageList.add(getString(R.string.gujarati))
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            btnBack.setOnClickListener {
                finish()
            }
            tvTitle.text = getString(R.string.language)
            divider.visibility = VISIBLE
        }
    }

    override fun ClickOnLanguage(position: Int) {
        when(languageList[position]){
            getString(R.string.english) -> {
                LocaleHelper.setLocale(this, Constant.Languages.English)
                preferenceUtils.setValue(Constant.SELECTED_LANGUAGE, Constant.Languages.English)
                finish()
            }
            getString(R.string.hindi) -> {
                LocaleHelper.setLocale(this, Constant.Languages.Hindi)
                preferenceUtils.setValue(Constant.SELECTED_LANGUAGE, Constant.Languages.Hindi)
                finish()
            }
            getString(R.string.marathi) -> {
                LocaleHelper.setLocale(this, Constant.Languages.Marathi)
                preferenceUtils.setValue(Constant.SELECTED_LANGUAGE, Constant.Languages.Marathi)
                finish()
            }
            getString(R.string.gujarati) -> {
                LocaleHelper.setLocale(this, Constant.Languages.Gujarati)
                preferenceUtils.setValue(Constant.SELECTED_LANGUAGE, Constant.Languages.Gujarati)
                finish()
            }
        }
    }
}