package com.example.digitracksdk.presentation.onboarding.document

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityAddPendingDocumentsBinding
import com.example.digitracksdk.databinding.BottomSheetAddPhotoBinding
import com.example.digitracksdk.domain.model.onboarding.documents.InsertPendingDocumentsRequestModel
import com.example.digitracksdk.domain.model.onboarding.documents.ListDocumentModel
import com.example.digitracksdk.domain.model.onboarding.documents.PendingDocumentsListRequestModel
import com.example.digitracksdk.utils.ImageUtils
import com.example.digitracksdk.presentation.onboarding.epf_details.adapter.DetailAdapter
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailItemType
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailModel
import com.example.digitracksdk.utils.AddImageUtils
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.digitracksdk.utils.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File

class AddPendingDocumentsActivity : BaseActivity(), DetailAdapter.DetailListener {
    lateinit var binding: ActivityAddPendingDocumentsBinding
    var list: ArrayList<DetailModel> = ArrayList()
    var pendingDocumentsList:ArrayList<ListDocumentModel>? = ArrayList()
    lateinit var adapter: DetailAdapter
    private val documentsDetailsViewModel: DocumentsDetailsViewModel by viewModel()
    private lateinit var addPhotoBottomSheetDialogBinding: BottomSheetAddPhotoBinding
    lateinit var preferenceUtils: PreferenceUtils
    var innovId: String = ""
    var docId:String =""
    var docType:String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddPendingDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        getPreferenceData()
        setUpAdapter()
        setObserver()
        setUpListener()
    }

    private fun setObserver() {
        binding.apply {
            with(documentsDetailsViewModel){
                pendingDocumentsListResponseData.observe(this@AddPendingDocumentsActivity) {
                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        if (!it.lstDocument.isNullOrEmpty()) {
                            showNoDataLayout(false)
                            pendingDocumentsList?.clear()
                            list.clear()
                            pendingDocumentsList?.addAll(it.lstDocument ?: arrayListOf())
                            for (i in it.lstDocument ?: arrayListOf()) {
                                list.add(
                                    DetailModel(
                                        title = i.Doctype,
                                        value = "",
                                        itemType = DetailItemType.DOCUMENT,
                                        btnText = getString(R.string.add)
                                    )
                                )
                            }
                            adapter.notifyDataSetChanged()
                        } else {
                            showNoDataLayout(true)
                            layoutNoData.apply {
                                //TODO confirm and update layout
                                tvNoData.text = getString(R.string.all_documents_uploaded)
                            }
                        }
                    } else {
                        showNoDataLayout(true)
                        showToast(it.Message.toString())
                    }
                }

                insertPendingDocumentsResponseData.observe(this@AddPendingDocumentsActivity) {
//                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        showToast(it.Message.toString())
                        callPendingDocumentListApi()
                    } else {
                        toggleLoader(false)
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(this@AddPendingDocumentsActivity) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }
        }
    }

    private fun getPreferenceData() {
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callPendingDocumentListApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing=false
                callPendingDocumentListApi()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        callPendingDocumentListApi()
    }

    private fun callPendingDocumentListApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                toggleLoader(true)
                documentsDetailsViewModel.callPendingDocumentsListApi(
                    request = PendingDocumentsListRequestModel(
                        InnovID = innovId,"Y"
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerPendingDoc.visibility= GONE
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
                noDataLayout = layoutNoData.root, recyclerView = recyclerPendingDoc, show = show
            )
        }
    }

    private fun setUpAdapter() {
        adapter = DetailAdapter(this, list, listener = this)
        binding.recyclerPendingDoc.adapter = adapter
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.add_documents)
            divider.visibility = VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun openAddPhotoBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_add_photo, null)
        addPhotoBottomSheetDialogBinding = BottomSheetAddPhotoBinding.bind(view)
        bottomSheetDialog.apply {
            setCancelable(true)
            setContentView(view)
            show()
        }
        val intent = Intent(this, AddImageUtils::class.java)
        val b = Bundle()
        addPhotoBottomSheetDialogBinding.apply {
            imgPdf.visibility = VISIBLE
            tvPdf.visibility = VISIBLE
            tvAddPhoto.text = getString(R.string.choose_image_pdf)
            imgPdf.setOnClickListener {
                b.putBoolean(Constant.IS_CAMERA, false)
                b.putBoolean(Constant.IS_PDF, true)
                intent.putExtras(b)
                pdfResult.launch(intent)
                bottomSheetDialog.dismiss()
            }
            imgCamera.setOnClickListener {
                b.putBoolean(Constant.IS_CAMERA, true)
                intent.putExtras(b)
                addImageUtils.launch(intent)
                bottomSheetDialog.dismiss()
            }
            imgGallery.setOnClickListener {
                b.putBoolean(Constant.IS_CAMERA, false)
                intent.putExtras(b)
                addImageUtils.launch(intent)
                bottomSheetDialog.dismiss()
            }
        }

    }

    private val pdfResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data?.extras
                val url = data?.getString(Constant.IntentExtras.EXTRA_FILE_PATH)
                val fileName = data?.getString(Constant.IntentExtras.EXTRA_FILE_NAME)
                val pdf =
                    Base64.encodeToString(
                        AppUtils.INSTANCE?.getPdfToBase64(
                            Uri.parse(url.toString()),
                            this
                        ), Base64.NO_WRAP
                    )
                if (!pdf.isNullOrEmpty()){
                    callUploadPendingDocumentApi(pdf,"pdf")
                }else{
                    showToast(getString(R.string.pdf_upload_failed))
                }
//                binding.apply {
//                    if (pdf.length > 1000000) {
//                        pdf = ""
//                        showToast(getString(R.string.pdf_file_size_more_than_1_mb_not_allowed))
//                    } else {
//                        callUploadPendingDocumentApi(pdf,"pdf")
//                    }
//                }
            }
        }

    private val addImageUtils =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data?.extras
                val fileName = data?.getString(Constant.IntentExtras.EXTRA_FILE_NAME)
                val url = data?.getString(Constant.IntentExtras.EXTRA_FILE_PATH)
                val bitmapFile =
                    File(url.toString())
                val bitmap = BitmapFactory.decodeFile(bitmapFile.toString())
                val img = ImageUtils.INSTANCE?.bitMapToString(bitmap).toString()
                callUploadPendingDocumentApi(img,fileName.toString().split(".").last())
            }
        }

    private fun callUploadPendingDocumentApi(image:String,extn:String) {
        if (isNetworkAvailable()){
            toggleLoader(true)
            documentsDetailsViewModel.callInsertPendingDocumentsApi(request = InsertPendingDocumentsRequestModel(
                InnovID = innovId,
                ImageArr = image,
                DocID = docId,
                Doctype = docType,
                Extn = extn
            )
            )
        }else{
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun clickOnAddOrViewButton(position: Int) {
        super.clickOnAddOrViewButton(position)
        docId = pendingDocumentsList?.get(position)?.DocID.toString()
        docType = pendingDocumentsList?.get(position)?.Doctype.toString()
        openAddPhotoBottomSheet()
    }
}