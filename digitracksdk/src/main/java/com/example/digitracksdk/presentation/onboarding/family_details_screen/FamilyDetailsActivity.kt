package com.example.digitracksdk.presentation.onboarding.family_details_screen

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityFamilyDetailsBinding
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.ListFamily
import com.example.digitracksdk.presentation.onboarding.family_details_screen.adapter.FamilyDetailsAdapter
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class FamilyDetailsActivity : BaseActivity(), FamilyDetailsAdapter.FamilyListener {
    lateinit var binding: ActivityFamilyDetailsBinding
    lateinit var preferenceUtils: PreferenceUtils
    lateinit var familyDetailsAdapter: FamilyDetailsAdapter

    var familyList: ArrayList<ListFamily> = ArrayList()
    private val paperlessViewFamilyDetailsViewModel: PaperlessViewFamilyDetailsViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFamilyDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setObserver()
        setUpAdapter()
        setUpListener()
    }

    private fun setObserver() {
        binding.apply {
            with(paperlessViewFamilyDetailsViewModel) {
                viewFamilyDetailsResponseData.observe(this@FamilyDetailsActivity) {
                    if (it.status == Constant.SUCCESS) {
                        if (!it.lstFamily.isNullOrEmpty()) {
                            showNoDataLayout(false)
                            familyList.clear()
                            familyList.addAll(it.lstFamily ?: arrayListOf())
                            familyDetailsAdapter.notifyDataSetChanged()
                        } else {
                            showNoDataLayout(true)
                            binding.layoutNoData.tvNoData.text = it.Message
                        }
                    } else {
                        showNoDataLayout(true)
                        showToast(it.Message.toString())
                    }
                }
                messageData.observe(this@FamilyDetailsActivity) {
                    showToast(it)
                }
                showProgressBar.observe(this@FamilyDetailsActivity) {
                    toggleLoader(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        callGetFamilyDetailsApi()
    }

    private fun setUpListener() {
        binding.apply {
            fabAdd.setOnClickListener {
                val b = Bundle()
                val i = Intent(this@FamilyDetailsActivity, AddFamilyMemberActivity::class.java)
                b.putBoolean(Constant.IS_FROM_ADD, true)
                i.putExtras(b)
                startActivity(i)
            }
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callGetFamilyDetailsApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing=false
                callGetFamilyDetailsApi()
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
                noDataLayout = layoutNoData.root, recyclerView = recyclerFamilyDetails, show = show
            )
        }
    }

    private fun callGetFamilyDetailsApi() {

        with(binding){

            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                paperlessViewFamilyDetailsViewModel.callViewFamilyDetailsApi(
                    request = InnovIDRequestModel(
                        InnovID = preferenceUtils.getValue(com.example.digitracksdk.Constant.PreferenceKeys.INNOV_ID)
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerFamilyDetails.visibility= GONE
            }
            layoutNoData.root.visibility= GONE
        }
    }

    private fun setUpAdapter() {
        if (::familyDetailsAdapter.isInitialized) {
            familyDetailsAdapter.notifyDataSetChanged()
        } else {
            familyDetailsAdapter = FamilyDetailsAdapter(this, familyList, this)
            binding.recyclerFamilyDetails.adapter = familyDetailsAdapter
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.family_details)
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
        }
    }

    override fun onFamilyDetailClicked(position: Int, data: ListFamily) {
        val detailIntent = Intent(this, AddFamilyMemberActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable(Constant.DATA, data)
        detailIntent.putExtras(bundle)
        startActivity(detailIntent)
    }
}