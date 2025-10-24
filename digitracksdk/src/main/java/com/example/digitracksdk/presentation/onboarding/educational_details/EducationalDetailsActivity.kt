package com.example.digitracksdk.presentation.onboarding.educational_details

import android.content.Intent
import android.os.Bundle
import android.view.View.*
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityEducationalDetailsBinding
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.ListEducationDetails
import com.example.digitracksdk.presentation.onboarding.educational_details.adapter.EducationalDetailAdapter
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class EducationalDetailsActivity : BaseActivity(), EducationalDetailAdapter.EducationListener {

    lateinit var binding: ActivityEducationalDetailsBinding
    lateinit var preferenceUtils: PreferenceUtils
    lateinit var adapter: EducationalDetailAdapter
    var list: ArrayList<ListEducationDetails> = ArrayList()
    private val paperlessViewEducationDetailsViewModel: PaperlessViewEducationDetailsViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEducationalDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setUpAdapter()
        setObserver()
        setListener()
    }

    private fun setObserver() {
        binding.apply {
            with(paperlessViewEducationDetailsViewModel) {
                viewEducationDetailsResponseData.observe(this@EducationalDetailsActivity) {
                    if (it.status == Constant.SUCCESS) {
                        if (!it.lstEducationDetails.isNullOrEmpty()) {
                            showNoDataLayout(false)
                            list.clear()
                            list.addAll(it.lstEducationDetails)
                            adapter.notifyDataSetChanged()
                        } else {
                            showNoDataLayout(true)
                            binding.layoutNoData.tvNoData.text = it.Message
                        }
                    } else {
                        showNoDataLayout(true)
                        showToast(getString(R.string.something_went_wrong))
                    }
                }
                messageData.observe(this@EducationalDetailsActivity) {
                    showNoDataLayout(true)
                    showToast(it)
                }
                showProgressBar.observe(this@EducationalDetailsActivity) {
                    toggleLoader(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        callEducationDetailsApi()
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.educational_details)
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
        }
    }

    private fun setListener() {
        binding.apply {
            fabAdd.setOnClickListener {
                val i = Intent(this@EducationalDetailsActivity, AddEducationalDetailsActivity::class.java)
                val b = Bundle()
                b.putBoolean(Constant.IS_FROM_ADD,true)
                i.putExtras(b)
                startActivity(i)
            }
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callEducationDetailsApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing=false
                callEducationDetailsApi()
            }
        }
    }

    private fun callEducationDetailsApi() {

        with(binding){

            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                paperlessViewEducationDetailsViewModel.callViewEducationDetailsApi(
                    request = InnovIDRequestModel(
                        InnovID = preferenceUtils.getValue(com.example.digitracksdk.Constant.PreferenceKeys.INNOV_ID)
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerEducationalDetails.visibility= GONE
            }
            layoutNoData.root.visibility= GONE
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
                noDataLayout = layoutNoData.root, recyclerView = recyclerEducationalDetails, show = show
            )
        }
    }


    private fun setUpAdapter() {
        adapter = EducationalDetailAdapter(this, list,this)
        binding.recyclerEducationalDetails.adapter = adapter
    }

    override fun onEducationItemClicked(position: Int, data: ListEducationDetails) {
        val detailIntent = Intent(this, AddEducationalDetailsActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable(Constant.VIEW_TYPE, Constant.ViewType.EDIT)
        bundle.putSerializable(Constant.DATA,data)
        detailIntent.putExtras(bundle)
        startActivity(detailIntent)
    }
}