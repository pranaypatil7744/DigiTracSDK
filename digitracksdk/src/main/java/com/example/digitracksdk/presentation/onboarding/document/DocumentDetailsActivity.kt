package com.example.digitracksdk.presentation.onboarding.document

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityDocumentDetailsBinding
import com.example.digitracksdk.domain.model.uploaded_documents.CandidateDocListModel
import com.example.digitracksdk.domain.model.uploaded_documents.UploadedDocumentsRequestModel
import com.example.digitracksdk.presentation.image_view.ImageViewActivity
import com.example.digitracksdk.presentation.onboarding.epf_details.adapter.DetailAdapter
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailItemType
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.enum.UploadedDocumentType
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class DocumentDetailsActivity : BaseActivity(), DetailAdapter.DetailListener {

    lateinit var binding: ActivityDocumentDetailsBinding
    private val documentsViewModel: DocumentsDetailsViewModel by viewModel()
    var list: ArrayList<DetailModel> = ArrayList()
    lateinit var preferenceUtils: PreferenceUtils
    var documentsList: ArrayList<CandidateDocListModel> = ArrayList()
    lateinit var adapter: DetailAdapter
    companion object{
        var viewImage = ""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentDetailsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setUpAdapter()
        setUpListener()
        setUpObserver()
    }

    private fun setUpObserver(){
        binding.apply {
            with(documentsViewModel) {

                uploadedDocumentsListResponseData.observe(this@DocumentDetailsActivity){
                    toggleLoader(false)
                    if (it.Status == Constant.success) {
                        if (!it.LstCandidateDoc.isNullOrEmpty()) {
                            showNoDataLayout(false)
                            list.clear()
                            documentsList.clear()
                            documentsList.addAll(it.LstCandidateDoc)
                            for (i in it.LstCandidateDoc) {
                                list.add(
                                    DetailModel(
                                        title = i.DocName,
                                        value = i.DocName,
                                        itemType = DetailItemType.DOCUMENT,
                                        btnText = getString(R.string.view)
                                    )
                                )
                            }
                            adapter.notifyDataSetChanged()
                        } else {
                            showNoDataLayout(true)
                        }
                    } else {
                        showNoDataLayout(true)
                        showToast(it.Message.toString())
                    }
                }

//                viewDocumentsListResponseData.observe(this@DocumentDetailsActivity) {
//                    toggleLoader(false)
//                    if (it.Status == Constant.SUCCESS) {
//                        if (!it.lstDoc.isNullOrEmpty()) {
//                            layoutNoData.root.visibility = GONE
//                            list.clear()
//                            documentsList.clear()
//                            documentsList.addAll(it.lstDoc ?: arrayListOf())
//                            for (i in it.lstDoc ?: arrayListOf()) {
//                                list.add(
//                                    DetailModel(
//                                        title = i.DocName,
//                                        value = i.DocName + ".jpg",
//                                        itemType = DetailItemType.DOCUMENT,
//                                        btnText = getString(R.string.view)
//                                    )
//                                )
//                            }
//                            adapter.notifyDataSetChanged()
//                        } else {
//                            layoutNoData.root.visibility = VISIBLE
//                        }
//                    } else {
//                        showToast(it.Message.toString())
//                    }
//                }
                messageData.observe(this@DocumentDetailsActivity) {
                    toggleLoader(false)
                    showToast(it)
                }
            }
        }
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callUploadDocumentListApi()
            }
            fabAddDoc.setOnClickListener {
                startActivity(Intent(this@DocumentDetailsActivity, AddPendingDocumentsActivity::class.java))
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing=false
                callUploadDocumentListApi()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        callUploadDocumentListApi()
    }

    private fun callUploadDocumentListApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                layoutNoInternet.root.visibility = GONE
                documentsViewModel.callUploadedDocumentsListApi(
                    request = UploadedDocumentsRequestModel(
                        InnovID = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID), DocType = Constant.Other
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerDocumentDetails.visibility= GONE
            }
            layoutNoData.root.visibility= GONE
        }
    }

    private fun setUpAdapter() {
        adapter = DetailAdapter(this, list,listener = this)
        binding.recyclerDocumentDetails.adapter = adapter
    }

    private fun setUpToolbar() {

        binding.toolbar.apply {
            tvTitle.text = getString(R.string.document_details)
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
                noDataLayout = layoutNoData.root, recyclerView = recyclerDocumentDetails, show = show
            )
        }
    }

    override fun clickOnAddOrViewButton(position: Int) {
        super.clickOnAddOrViewButton(position)
        val data = documentsList[position]
        val b = Bundle()
        val i = Intent(this, ImageViewActivity::class.java)
        b.putBoolean(Constant.IS_IMAGE, true)
        b.putBoolean(Constant.IS_FROM_DOC, true)
        b.putString(Constant.SCREEN_NAME, data.DocName)
        b.putString(Constant.CandidateDocumentMappingID,data.CandidateDocumentMappingID)
        b.putInt(Constant.VIEW_TYPE, UploadedDocumentType.OTHER.value)
        i.putExtras(b)
        startActivity(i)
    }
}