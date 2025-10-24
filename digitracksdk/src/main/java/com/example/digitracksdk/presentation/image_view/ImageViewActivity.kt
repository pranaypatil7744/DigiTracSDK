package com.example.digitracksdk.presentation.image_view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityImageViewBinding
import com.example.digitracksdk.domain.model.uploaded_documents.GetDocumentRequestModel
import com.example.digitracksdk.presentation.onboarding.document.DocumentDetailsActivity.Companion.viewImage
import com.example.digitracksdk.presentation.onboarding.document.DocumentsDetailsViewModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.ImageUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import com.squareup.picasso.Picasso
import org.koin.android.viewmodel.ext.android.viewModel
import java.net.URL
import java.util.*


class ImageViewActivity : BaseActivity() {
    lateinit var binding: ActivityImageViewBinding
    var screenName: String = ""
    var isImage: Boolean = false
    var isFromDocuments: Boolean = false
    var candidateDocumentMappingID: String = ""
    var documentType: Int = 0
    private val documentsViewModel: DocumentsDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        getIntentData()
        setUpListener()
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callGetDocumentApi()
            }
        }
    }

    private fun getIntentData() {
        intent.extras?.run {
            binding.apply {
                val image = getString(Constant.IMAGE_PATH)
                screenName = getString(Constant.SCREEN_NAME).toString()
                isImage = getBoolean(Constant.IS_IMAGE, false)
                isFromDocuments = getBoolean(Constant.IS_FROM_DOC, false)
                if (isFromDocuments) {
                    candidateDocumentMappingID =
                        getString(Constant.CandidateDocumentMappingID).toString()
                    documentType = getInt(Constant.VIEW_TYPE)
                    setUpObserver()
                    callGetDocumentApi()
                } else {
                    if (isImage) {
                        if (screenName == getString(R.string.rewards)) {
                            Picasso.get()
                                .load(image)
                                .into(imageView)
                            binding.toolbar.apply {
                                tvLeftTitle1.text = getString(R.string.share)
                                tvLeftTitle1.visibility = VISIBLE
                                tvLeftTitle1.setTextColor(
                                    ContextCompat
                                        .getColor(this@ImageViewActivity, R.color.blue_ribbon)
                                )

                                tvLeftTitle1.setOnClickListener {
                                    shareImage(image)
                                }
                            }

                        } else {
                            val imageBitMap = ImageUtils.INSTANCE?.stringToBitMap(viewImage)
                            ImageUtils.INSTANCE?.loadBitMap(binding.imageView, imageBitMap)
                        }


                    } else {
                        //for pdf file
                        val file =
                            image?.let { ImageUtils.INSTANCE?.writePDFToFile(it, screenName) }
                        file?.let {
                            ImageUtils.INSTANCE?.displayFile(
                                it,
                                binding.imageView,
                                this@ImageViewActivity
                            )
                        }

                    }
                }
                setUpToolbar()
            }
        }
    }

    private fun shareImage(imagePath: String?) {


        if (imagePath != null) {
            if (imagePath.isNotEmpty()) {
                Picasso.get().load(imagePath).into(object : com.squareup.picasso.Target {
                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    }

                    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                        // loaded bitmap is here (bitmap)

                        val i = Intent(Intent.ACTION_SEND)
                        i.setType("image/*")
                        i.putExtra(
                            Intent.EXTRA_STREAM,
                            ImageUtils.INSTANCE?.getImageUri(this@ImageViewActivity, bitmap)
                        )
                        startActivity(Intent.createChooser(i, "Share Reward"))
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

                })
            }
        }
    }

    private fun setUpObserver() {
        binding.apply {
            with(documentsViewModel) {
                getUploadedDocumentResponseData.observe(this@ImageViewActivity) {
                    toggleLoader(false)
                    if (it.Status == Constant.success) {
                        if (it.Ext.toString().lowercase(Locale.getDefault()) == ".pdf") {
                            if (!it.File.isNullOrEmpty()) {
                                layoutNoData.root.visibility = GONE
                                val file =
                                    it.File?.let {
                                        com.example.digitracksdk.utils.ImageUtils.INSTANCE?.writePDFToFile(
                                            it,
                                            screenName
                                        )
                                    }
                                pdfViewer.visibility = VISIBLE
                                pdfViewer.fromFile(file)
                                    .enableSwipe(true) // allows to block changing pages using swipe
                                    .swipeHorizontal(false)
                                    .enableDoubletap(true)
                                    .defaultPage(0)
                                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                                    .password(null)
                                    .scrollHandle(null)
                                    .load()
//                                file?.let {
//                                    ImageUtils.INSTANCE?.displayFile(
//                                        it,
//                                        binding.imageView,
//                                        this@ImageViewActivity
//                                    )
//                                }
                                toolbar.btnOne.apply {
                                    visibility = VISIBLE
                                    setImageResource(R.drawable.ic_download)
                                    setOnClickListener {
                                        com.example.digitracksdk.utils.ImageUtils.INSTANCE?.openPdfFile(
                                            this@ImageViewActivity,
                                            file?.absolutePath.toString()
                                        )
                                    }
                                }
                            } else if (!it.Link.isNullOrEmpty()) {
                                val pdf = URL(it.Link.toString()).openStream()
                                layoutNoData.root.visibility = GONE
                                pdfViewer.visibility = VISIBLE
                                pdfViewer.fromStream(pdf)
                                    .enableSwipe(true) // allows to block changing pages using swipe
                                    .swipeHorizontal(false)
                                    .enableDoubletap(true)
                                    .defaultPage(0)
                                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                                    .password(null)
                                    .scrollHandle(null)
                                    .load();
                                toolbar.btnOne.apply {
                                    visibility = VISIBLE
                                    setImageResource(R.drawable.ic_download)
                                    setOnClickListener { v ->
                                        val i = Intent(Intent.ACTION_VIEW)
                                        i.setDataAndType(
                                            Uri.parse(it.Link.toString()),
                                            "application/pdf"
                                        )
                                        startActivity(i)
                                    }
                                }
                            } else {
                                toolbar.btnOne.visibility = GONE
                                layoutNoData.root.visibility = VISIBLE
                                layoutNoData.tvNoData.text = getString(R.string.no_document_found)
                            }

                        } else {
                            toolbar.btnOne.visibility = GONE
                            pdfViewer.visibility = GONE
                            imageView.visibility = VISIBLE
                            if (!it.Link.isNullOrEmpty()) {
                                com.example.digitracksdk.utils.ImageUtils.INSTANCE?.loadRemoteImage(
                                    imageView,
                                    it.Link.toString().replace("\\", "/")
                                )
                            } else if (!it.File.isNullOrEmpty()) {
                                val imageBitMap = com.example.digitracksdk.utils.ImageUtils.INSTANCE?.stringToBitMap(it.File)
                                com.example.digitracksdk.utils.ImageUtils.INSTANCE?.loadBitMap(imageView, imageBitMap)
                            } else {
                                layoutNoData.root.visibility = VISIBLE
                                layoutNoData.tvNoData.text = getString(R.string.no_document_found)
                            }
                        }
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(this@ImageViewActivity) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
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

    private fun callGetDocumentApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                layoutNoInternet.root.visibility = View.GONE
                documentsViewModel.callGetUploadedDocumentApi(
                    request = GetDocumentRequestModel(
                        CandidateDocumentMappingID = candidateDocumentMappingID
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewImage = ""
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = screenName
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
        }
    }


}