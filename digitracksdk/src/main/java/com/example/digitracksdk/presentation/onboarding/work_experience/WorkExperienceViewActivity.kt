package com.example.digitracksdk.presentation.onboarding.work_experience

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityWorkExperienceViewBinding
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.ListWorkExpModel
import com.example.digitracksdk.presentation.onboarding.work_experience.adapter.WorkExpViewAdapter
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class WorkExperienceViewActivity : BaseActivity(), WorkExpViewAdapter.WorkExpClickManager {
    lateinit var preferenceUtils: PreferenceUtils
    lateinit var binding:ActivityWorkExperienceViewBinding
    lateinit var workExpViewAdapter: WorkExpViewAdapter
    private val paperlessViewWorkExpDetailsViewModel: PaperlessViewWorkExpDetailsViewModel by viewModel()
    var innovId:String =""
    var totalExpYear = ""
    var totalExpMonth = ""
    var relevantExpYear = ""
    var relevantExpMonth = ""
    var isCurrentlyEmployed = ""
    var workExpList:ArrayList<ListWorkExpModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWorkExperienceViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setUpAdapter()
        setObserver()
        getPreferenceData()
        setUpListener()
    }

    private fun setObserver() {
        binding.apply {
            with(paperlessViewWorkExpDetailsViewModel) {
                viewWorkExpDetailsResponseData.observe(this@WorkExperienceViewActivity) {
                    if (it.status == Constant.SUCCESS) {
                        if (!it.LstWorkExp.isNullOrEmpty()) {
                            showNoDataLayout(false)
                            workExpList.clear()
                            workExpList.addAll(it.LstWorkExp)
                            isCurrentlyEmployed = it.CurrentlyEmployed.toString()
                            totalExpYear = it.TotalExpInYear.toString()
                            totalExpMonth = it.TotalExpMonth.toString()
                            relevantExpYear = it.totalRelevantExpYear.toString()
                            relevantExpMonth = it.totalRelevantExpMonth.toString()
                            workExpViewAdapter.notifyDataSetChanged()

                        } else {
                            showNoDataLayout(true)
                            binding.layoutNoData.tvNoData.text = it.Message
                        }
                    } else {
                        showNoDataLayout(true)
                        showToast(it.Message.toString())
                    }
                }
                messageData.observe(this@WorkExperienceViewActivity) {
                    showNoDataLayout(true)
                    showToast(it)
                }
                showProgressBar.observe(this@WorkExperienceViewActivity){
                    toggleLoader(it)
                }
            }
        }
    }

    private fun setUpAdapter() {
        workExpViewAdapter = WorkExpViewAdapter(this,workExpList,this)
        binding.recyclerWorkExperience.adapter = workExpViewAdapter
    }

    override fun onResume() {
        super.onResume()
        callWorkExperienceViewApi()
    }
    private fun getPreferenceData() {
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
    }
    private fun callWorkExperienceViewApi() {

        with(binding){

            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                paperlessViewWorkExpDetailsViewModel.callViewWorkExpDetailsApi(
                    request = InnovIDRequestModel(
                        InnovID = preferenceUtils.getValue(com.example.digitracksdk.Constant.PreferenceKeys.INNOV_ID)
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerWorkExperience.visibility= GONE
            }
            layoutNoData.root.visibility= GONE
        }
    }

    private fun setUpListener() {
        binding.apply {
            fabAddWorkExp.setOnClickListener {
                val i = Intent(this@WorkExperienceViewActivity, WorkExperienceActivity::class.java)
                val b = Bundle()
                b.putBoolean(Constant.IS_FROM_ADD,true)
                b.putString(Constant.TOTAL_EXP_YEAR,totalExpYear)
                b.putString(Constant.TOTAL_EXP_MONTH,totalExpMonth)
                b.putString(Constant.RELEVANT_EXP_YEAR,relevantExpYear)
                b.putString(Constant.RELEVANT_EXP_MONTH,relevantExpMonth)
                b.putBoolean(Constant.IS_FIRST_TIME, workExpList.size <= 0)
                i.putExtras(b)
                startActivity(i)
            }
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callWorkExperienceViewApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing=false
                callWorkExperienceViewApi()
            }
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.work_experience)
            divider.visibility = VISIBLE
            btnBack.setOnClickListener {
                finish()
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
    private fun showNoDataLayout(show: Boolean) {
        binding.apply {
            noDataLayout(
                noDataLayout = layoutNoData.root, recyclerView = recyclerWorkExperience, show = show
            )
        }
    }

    override fun clickOnItem(position: Int) {
        val i = Intent(this@WorkExperienceViewActivity, WorkExperienceActivity::class.java)
        val b = Bundle()
        b.putSerializable(Constant.WORK_EXP_MODEL,workExpList[position])
        b.putString(Constant.TOTAL_EXP_YEAR,totalExpYear)
        b.putString(Constant.TOTAL_EXP_MONTH,totalExpMonth)
        b.putString(Constant.RELEVANT_EXP_YEAR,relevantExpYear)
        b.putString(Constant.RELEVANT_EXP_MONTH,relevantExpMonth)
        b.putString(Constant.IS_CURRENTLY_EMPLOYED,isCurrentlyEmployed)
        i.putExtras(b)
        startActivity(i)
    }
}