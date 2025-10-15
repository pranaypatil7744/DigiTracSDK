package com.example.digitracksdk.presentation.my_letters.form_16

import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityForm16Binding
import com.example.digitracksdk.domain.model.my_letters.FyListModel
import com.example.digitracksdk.domain.model.my_letters.GetFinancialYearsListRequestModel
import com.example.digitracksdk.domain.model.my_letters.GetForm16RequestModel
import com.example.digitracksdk.presentation.my_letters.other_letter.OtherLetterViewModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class Form16Activity : BaseActivity() {
    lateinit var binding: ActivityForm16Binding
    private val otherLetterViewModel: OtherLetterViewModel by viewModel()
    lateinit var preferenceUtils: PreferenceUtils
    var innovId: String = ""
    var gnetAssociateId: String = ""
    var selectedYearId: Int = -1
    var selectedYear = ""

    private var financialYearsList: ArrayList<FyListModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForm16Binding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        getPreferenceData()
        setUpObserver()
        callGetFinancialYearApi()
        setUpToolbar()
        setUpListener()
    }

    private fun getPreferenceData() {
        binding.apply {
            preferenceUtils.apply {
                innovId = getValue(Constant.PreferenceKeys.INNOV_ID)
                gnetAssociateId = getValue(Constant.PreferenceKeys.GnetAssociateID)
            }
        }
    }

    private fun callGetFinancialYearApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                otherLetterViewModel.callGetFinancialYearListApi(
                    request = GetFinancialYearsListRequestModel(
                        UserId = innovId
                    )
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    private fun setUpObserver() {
        binding.apply {
            with(otherLetterViewModel) {
                getForm16ResponseData.observe(this@Form16Activity) {
                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        if (!it.ImageArr.isNullOrEmpty()) {
                            val file = it.ImageArr?.let { it1 ->
                                com.example.digitracksdk.utils.ImageUtils.INSTANCE?.writePDFToFile(it1, "Form 16")
                            }
                            com.example.digitracksdk.utils.ImageUtils.INSTANCE?.openPdfFile(
                                this@Form16Activity,
                                file?.absolutePath.toString()
                            )
                        } else {
                            showToast(it.Message.toString())
                        }
                    } else {
                        showToast(it.Message.toString())
                    }
                }
                getFinancialYearResponseData.observe(this@Form16Activity) {
                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        if (!it.FYlist.isNullOrEmpty()) {
                            val yearList: ArrayList<String> = ArrayList()
                            yearList.clear()
                            financialYearsList.clear()
                            financialYearsList.addAll(it.FYlist)
                            for (i in financialYearsList) {
                                yearList.add(i.FinancialYearName.toString())
                            }
                            val adapter = ArrayAdapter<String>(
                                this@Form16Activity,
                                android.R.layout.simple_list_item_1,
                                yearList
                            )
                            etAssessmentYear.setAdapter(adapter)
                        } else {
                            showToast(it.Message.toString())
                        }
                    } else {
                        showToast(it.Message.toString())
                    }
                }
                messageData.observe(this@Form16Activity) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }
        }
    }

    private fun setUpListener() {
        binding.apply {
            btnDownload.setOnClickListener {
                if (selectedYear.isEmpty()){
                    showToast(getString(R.string.please_select_assessment_year))
                }else{
                    callGetForm16Api()
                }
            }

            etAssessmentYear.onItemClickListener =
                AdapterView.OnItemClickListener { parent, p1, position, p3 ->
                    selectedYear = financialYearsList[position].FinancialYearName ?: ""
                    selectedYearId = financialYearsList[position].FinancialYearID ?: 0
                }
        }
    }

    private fun callGetForm16Api() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                otherLetterViewModel.callGetForm16Api(
                    request = GetForm16RequestModel(
                        GNETAssociateID = gnetAssociateId,
                        FinancialYear = selectedYear
                    )
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.layoutLoading.root,
            binding.layoutLoading.imageLoading,
            showLoader
        )
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.form_16)
            divider.visibility = VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
        }
    }
}