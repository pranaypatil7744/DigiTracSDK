package com.example.digitracksdk.presentation.onboarding.kyc_screen

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityKycBinding
import com.example.digitracksdk.domain.model.uploaded_documents.CandidateDocListModel
import com.example.digitracksdk.domain.model.uploaded_documents.UploadedDocumentsRequestModel
import com.example.digitracksdk.presentation.image_view.ImageViewActivity
import com.example.digitracksdk.presentation.onboarding.document.DocumentsDetailsViewModel
import com.example.digitracksdk.presentation.onboarding.kyc_screen.adapter.KycAdapter
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.enum.UploadedDocumentType
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class KycActivity : BaseActivity(), KycAdapter.DocumentViewManager {
    lateinit var binding: ActivityKycBinding
    lateinit var preferenceUtils: PreferenceUtils
    lateinit var kycAdapter: KycAdapter
    var kycDocList: ArrayList<CandidateDocListModel> = ArrayList()
    private val paperlessKycViewModel: PaperlessKycViewModel by viewModel()
    private val documentsViewModel: DocumentsDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityKycBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setUpObserver()
        setListener()
    }

    private fun setUpObserver() {
        binding.apply {
            with(documentsViewModel) {

                uploadedDocumentsListResponseData.observe(this@KycActivity){
                    toggleLoader(false)
                    if (it.Status == Constant.success) {
                        if (it.LstCandidateDoc.isNotEmpty()) {
                            showNoDataLayout(false)
                            kycDocList.clear()
                            kycDocList.addAll(it.LstCandidateDoc)
                           setUpKycListAdapter()
                        } else {
                            showNoDataLayout(true)
                        }
                    } else {
                        showNoDataLayout(true)
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(this@KycActivity) {
                    toggleLoader(false)
                    showNoDataLayout(true)
                    showToast(it)
                }
            }

//            with(paperlessKycViewModel) {
//
//                kYCDocsResponseData.observe(this@KycActivity
//                ) {
//                    if (it.lstDoc?.isNotEmpty() == true) {
//                        layoutNoData.root.visibility = GONE
//                        kycDocList = it.lstDoc
//                        setUpKycListAdapter()
//                    } else {
//                        layoutNoData.root.visibility = VISIBLE
//                    }
//                }
//
//                messageData.observe(this@KycActivity) {
//                    showToast(it)
//                }
//                showProgressBar.observe(this@KycActivity) {
//                    toggleLoader(it)
//                }
//            }
        }
    }

    override fun onResume() {
        super.onResume()
        callViewKycDocumentApi()
    }

    private fun setListener() {
        binding.apply {
            fabAdd.setOnClickListener {
                val i = Intent(this@KycActivity, AddPendingKycDocument::class.java)
                startActivity(i)
            }
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callViewKycDocumentApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing=false
                callViewKycDocumentApi()
            }
        }
    }

    private fun callViewKycDocumentApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                layoutNoInternet.root.visibility = GONE
                documentsViewModel.callUploadedDocumentsListApi(
                    request = UploadedDocumentsRequestModel(
                        InnovID = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID), DocType = "KYC"
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerKyc.visibility= GONE
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
                noDataLayout = layoutNoData.root, recyclerView = recyclerKyc, show = show
            )
        }
    }

    private fun setUpKycListAdapter() {
        kycAdapter = KycAdapter(this, kycDocList, this)
        binding.recyclerKyc.adapter = kycAdapter
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.kyc)
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
        }
    }

    override fun onViewClick(position: Int) {
        val data = kycDocList[position]
        val b = Bundle()
        val i = Intent(this@KycActivity, ImageViewActivity::class.java)
        b.putBoolean(Constant.IS_IMAGE, true)
        b.putBoolean(Constant.IS_FROM_DOC, true)
        b.putString(Constant.SCREEN_NAME, data.DocName.toString())
        b.putString(Constant.CandidateDocumentMappingID,data.CandidateDocumentMappingID)
        b.putInt(Constant.VIEW_TYPE, UploadedDocumentType.KYC.value)
        i.putExtras(b)
        startActivity(i)
    }
}