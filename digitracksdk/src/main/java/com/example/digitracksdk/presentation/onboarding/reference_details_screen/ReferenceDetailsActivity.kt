package com.example.digitracksdk.presentation.onboarding.reference_details_screen

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityReferenceDetailsBinding
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.LstReferenceDetail
import com.example.digitracksdk.presentation.onboarding.reference_details_screen.adapter.ReferenceDetailsAdapter
import com.example.digitracksdk.presentation.onboarding.reference_details_screen.add_person_screen.AddPersonActivity
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class ReferenceDetailsActivity : BaseActivity(), ReferenceDetailsAdapter.ReferenceListener {
    lateinit var binding: ActivityReferenceDetailsBinding
    lateinit var preferenceUtils: PreferenceUtils
    private var referenceDetailsList: ArrayList<LstReferenceDetail> = ArrayList()
    lateinit var referenceDetailsAdapter: ReferenceDetailsAdapter
    private val paperlessViewCandidateReferenceDetailsViewModel: PaperlessViewCandidateReferenceDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReferenceDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setUpReferenceDetailsAdapter()
        setUpObserver()
        setUpListener()
    }

    private fun setUpObserver() {
        binding.apply {
            with(paperlessViewCandidateReferenceDetailsViewModel) {
                viewCandidateReferenceDetailsResponseData.observe(this@ReferenceDetailsActivity) {
                    if (it.status == Constant.success) {
                        if (!it.lstReferenceDetails.isNullOrEmpty()) {
                            showNoDataLayout(false)
                            referenceDetailsList.clear()
                            referenceDetailsAdapter.refresh(it.lstReferenceDetails)
                        } else {
                            showNoDataLayout(true)
                            binding.layoutNoData.tvNoData.text = it.Message
                        }
                    } else {
                        showNoDataLayout(true)
                        showToast(it.Message.toString())
                    }
                }
                messageData.observe(this@ReferenceDetailsActivity) {
                    showToast(it)
                }
                showProgressBar.observe(this@ReferenceDetailsActivity){
                    toggleLoader(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        callGetReferenceDetailsApi()
    }

    private fun callGetReferenceDetailsApi() {

        with(binding){

            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                paperlessViewCandidateReferenceDetailsViewModel.callViewCandidateReferenceDetailsApi(
                    request = InnovIDRequestModel(
                        InnovID = preferenceUtils.getValue(com.example.digitracksdk.Constant.PreferenceKeys.INNOV_ID)
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerReferenceDetails.visibility= GONE
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
                noDataLayout = layoutNoData.root, recyclerView = recyclerReferenceDetails, show = show
            )
        }
    }

    private fun setUpReferenceDetailsAdapter() {
        referenceDetailsAdapter = ReferenceDetailsAdapter(this, referenceDetailsList, this)
        binding.recyclerReferenceDetails.adapter = referenceDetailsAdapter
    }

    private fun setUpListener() {
        binding.apply {
            fabAddPerson.setOnClickListener {
                val i = Intent(this@ReferenceDetailsActivity, AddPersonActivity::class.java)
                val b = Bundle()
                b.putBoolean(Constant.IS_FROM_ADD,true)
                i.putExtras(b)
                startActivity(i)
            }
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callGetReferenceDetailsApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing=false
                callGetReferenceDetailsApi()
            }
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.reference_details)
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
        }
    }

    override fun onReferenceClicked(position: Int, data: LstReferenceDetail) {
        val detailIntent = Intent(this, AddPersonActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean(Constant.IS_FROM_ADD,false)
        bundle.putSerializable(Constant.DATA, data)
        detailIntent.putExtras(bundle)
        startActivity(detailIntent)
    }

}