package com.example.digitracksdk.presentation.home.reimbursements.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Insets
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Size
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementDetailsModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementDetailsRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.UploadReimbursementBillRequestModel
import com.example.digitracksdk.utils.ImageUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.digitracksdk.Constant
import com.example.digitracksdk.domain.model.reimbursement_model.UpdateReimbursementStatusRequestModel
import com.innov.digitrac.R
import com.innov.digitrac.databinding.BottomSheetAddPhotoBinding
import com.innov.digitrac.databinding.FragmentReimbursementDetailsBottomSheetBinding
import com.innov.digitrac.domain.model.reimbursement_model.*
import com.example.digitracksdk.presentation.home.reimbursements.ReimbursementActivity
import com.example.digitracksdk.presentation.home.reimbursements.ReimbursementViewModel
import com.example.digitracksdk.presentation.home.reimbursements.UpdateReimbursementVoucher
import com.example.digitracksdk.presentation.home.reimbursements.adapter.ReimbursementDetailsListAdapter
import com.example.digitracksdk.presentation.home.reimbursements.adapter.ReimbursementDetailsViewAdapter
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimburseDetailsModel
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementDetailsListModel
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementType
import com.example.digitracksdk.utils.AddImageUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.presentation.image_view.ImageViewActivity
import com.example.digitracksdk.presentation.onboarding.document.DocumentDetailsActivity.Companion.viewImage
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import com.innov.digitrac.utils.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File

class ReimbursementDetailsBottomSheetFragment : BottomSheetDialogFragment(),
    ReimbursementDetailsListAdapter.ReimbursementClickManager {

    lateinit var binding: FragmentReimbursementDetailsBottomSheetBinding
    private lateinit var addPhotoBottomSheetDialogBinding: BottomSheetAddPhotoBinding
    lateinit var dialog: BottomSheetDialog
    var reimbursementType: ReimbursementType? = null
    var associateReimbursementId: String = ""
    var selectedItemPosition: Int = 0

    var date: String = ""
    var isRejected: Boolean = false
    lateinit var preferenceUtils: PreferenceUtils
    lateinit var reimbursementDetailsViewAdapter: ReimbursementDetailsViewAdapter
    var reimbursementDetailsList: ArrayList<ReimburseDetailsModel> = ArrayList()
    var reimbursementDetailsResponseList: ArrayList<ReimbursementDetailsModel> = ArrayList()
    private val reimbursementViewModel: ReimbursementViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            FragmentReimbursementDetailsBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceUtils = PreferenceUtils(this.requireContext())
        setUpView()
        setObserver()
        setUpAdapter()
        callReimbursementDetailsApi()
        setUpListener()
    }

    private fun setObserver() {
        binding.apply {
            with(reimbursementViewModel) {
                updateReimbursementStatusResponseData.observe(
                    this@ReimbursementDetailsBottomSheetFragment
                ) {
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        dialog.dismiss()
                        (context as ReimbursementActivity).callReimbursementListApi()
                        showToast(it.Message.toString())
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                reimbursementBillResponseData.observe(
                    this@ReimbursementDetailsBottomSheetFragment
                ) { it ->
                    toggleLoader(false)
                    if (it.status == Constant.success) {
                        if (!it.AssociateReimbursementFileList.isNullOrEmpty()) {
                            val data = it.AssociateReimbursementFileList!![0]
                            if (data.Extn == ".pdf") {
                                val file =
                                    it.AssociateReimbursementFileList?.get(0)?.FilePath?.let { it1 ->
                                        com.example.digitracksdk.utils.ImageUtils.INSTANCE?.writePDFToFile(
                                            it1,
                                            requireContext().getString(R.string.bill)
                                        )
                                    }
                                com.example.digitracksdk.utils.ImageUtils.INSTANCE?.openPdfFile(
                                    this@ReimbursementDetailsBottomSheetFragment.requireContext(),
                                    file?.absolutePath.toString()
                                )
                            } else {
                                val b = Bundle()
                                val i = Intent(
                                    this@ReimbursementDetailsBottomSheetFragment.requireContext(),
                                    ImageViewActivity::class.java
                                )
                                viewImage =
                                    it.AssociateReimbursementFileList?.get(0)?.FilePath.toString()
//                                    b.putString(
//                                        Constant.IMAGE_PATH,
//                                        it.AssociateReimbursementFileList?.get(0)?.FilePath
//                                    )
                                b.putString(Constant.SCREEN_NAME, getString(R.string.view_bill))
                                b.putBoolean(Constant.IS_IMAGE, true)
                                i.putExtras(b)
                                startActivity(i)
                            }

                        } else {
                            showToast(it.Message.toString())
                        }
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                uploadReimbursementBillResponseData.observe(
                    this@ReimbursementDetailsBottomSheetFragment
                ) {
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        showToast(it.Message.toString())
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                reimbursementDetailsResponseData.observe(
                    this@ReimbursementDetailsBottomSheetFragment
                ) {
                    toggleLoader(false)
                    val list: ArrayList<ReimbursementDetailsListModel> = ArrayList()
                    list.clear()
                    reimbursementDetailsList.clear()
                    if (!it.ListOfAssociateReimbursement.isNullOrEmpty()) {
                        reimbursementDetailsResponseList.clear()
                        reimbursementDetailsResponseList.addAll(
                            it.ListOfAssociateReimbursement ?: arrayListOf()
                        )
                        for (i in it.ListOfAssociateReimbursement ?: arrayListOf()) {
                            list.add(
                                ReimbursementDetailsListModel(
                                    title = getString(R.string.reimbursement_id),
                                    subTitle = i.AssociateReimbursementDetailId,
                                    title2 = getString(R.string.claim_date),
                                    subTitle2 = i.ClaimDate,
                                    reimbursementDetailsType = com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementDetailsType.MULTI_LINE
                                )
                            )


                            list.add(
                                ReimbursementDetailsListModel(
                                    title = getString(R.string.expensive_type),
                                    subTitle = i.ReimbursementCategory,
                                    title2 = getString(R.string.sub_category),
                                    subTitle2 = i.ReimbursementSubCategory,
                                    reimbursementDetailsType = com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementDetailsType.MULTI_LINE
                                )
                            )
                            list.add(
                                ReimbursementDetailsListModel(
                                    title = getString(R.string.claim_year),
                                    subTitle = i.ClaimYear,
                                    title2 = getString(R.string.claim_month),
                                    subTitle2 = i.ClaimMonth,
                                    reimbursementDetailsType = com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementDetailsType.MULTI_LINE
                                )
                            )
                            list.add(
                                ReimbursementDetailsListModel(
                                    title = getString(R.string.bill_date),
                                    subTitle = i.BillDate,
                                    title2 = getString(R.string.bill_number),
                                    subTitle2 = i.BillNo,
                                    reimbursementDetailsType = com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementDetailsType.MULTI_LINE
                                )
                            )
                            list.add(
                                ReimbursementDetailsListModel(
                                    title = getString(R.string.bill_from),
                                    subTitle = if (i.FromDate == "01 Jan 1900") "" else i.FromDate,
                                    title2 = getString(R.string.bill_to),
                                    subTitle2 = if (i.ToDate == "01 Jan 1900") "" else i.ToDate,
                                    reimbursementDetailsType = com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementDetailsType.MULTI_LINE
                                )
                            )
                            list.add(
                                ReimbursementDetailsListModel(
                                    title = getString(R.string.journey_from),
                                    subTitle = i.FromLocation,
                                    title2 = getString(R.string.journey_to),
                                    subTitle2 = i.ToLocation,
                                    reimbursementDetailsType = com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementDetailsType.MULTI_LINE
                                )
                            )
                            list.add(
                                ReimbursementDetailsListModel(
                                    title = getString(R.string.base_amount),
                                    subTitle = i.Amount,
                                    title2 = getString(R.string.travel_mode),
                                    subTitle2 = i.ModeOfTravel,
                                    reimbursementDetailsType = com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementDetailsType.MULTI_LINE
                                )
                            )
                            list.add(
                                ReimbursementDetailsListModel(
                                    title = getString(R.string.tax_amount),
                                    subTitle = i.TaxAmount,
                                    title2 = getString(R.string.gross_amount),
                                    subTitle2 = i.GrossAmount,
                                    reimbursementDetailsType = com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementDetailsType.MULTI_LINE
                                )
                            )
                            list.add(
                                ReimbursementDetailsListModel(
                                    title = getString(R.string.start_km),
                                    subTitle = i.StartKM,
                                    title2 = getString(R.string.end_km),
                                    subTitle2 = i.EndKM,
                                    reimbursementDetailsType = com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementDetailsType.MULTI_LINE
                                )
                            )
                            list.add(
                                ReimbursementDetailsListModel(
                                    reimbursementDetailsType = com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementDetailsType.TWO_LINE,
                                    title = getString(R.string.remark),
                                    subTitle = i.Remark
                                )
                            )
                            list.add(
                                ReimbursementDetailsListModel(
                                    reimbursementDetailsType = com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementDetailsType.TWO_LINE,
                                    title = getString(R.string.bill),
                                    subTitle = i.FilePath1 ?: "Bill.pdf",
                                    associateId = i.AssociateReimbursementDetailId
                                )
                            )
                            list.add(ReimbursementDetailsListModel(reimbursementDetailsType = com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementDetailsType.DIVIDER))
                        }
                        reimbursementDetailsList.clear()
                        reimbursementDetailsList.add(
                            ReimburseDetailsModel(
                                reimbursementDetailsList = list
                            )
                        )
                        reimbursementDetailsViewAdapter.notifyDataSetChanged()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(this@ReimbursementDetailsBottomSheetFragment) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            setupFullHeight(dialog)
        }
        return dialog
    }

    private fun setUpView() {
        binding.apply {
            if (reimbursementType == ReimbursementType.REJECTED) {
                tvType.apply {
                    text = getString(R.string.rejected)
                    setTextColor(
                        ContextCompat.getColorStateList(
                            this@ReimbursementDetailsBottomSheetFragment.requireContext(),
                            R.color.rejected_color
                        )
                    )
                }
                isRejected = true
            } else if (reimbursementType == ReimbursementType.APPROVED) {
                tvType.apply {
                    text = getString(R.string.approved)
                    setTextColor(
                        ContextCompat.getColorStateList(
                            this@ReimbursementDetailsBottomSheetFragment.requireContext(),
                            R.color.jungle_green
                        )
                    )
                }
                isRejected = false
            } else {
                tvType.apply {
                    text = getString(R.string.awaiting)
                    setTextColor(
                        ContextCompat.getColorStateList(
                            this@ReimbursementDetailsBottomSheetFragment.requireContext(),
                            R.color.cornflower_blue
                        )
                    )
                }
                isRejected = false

            }

            tvDate.text = date
        }

    }

    private fun setUpListener() {
        binding.apply {
            btnClose.setOnClickListener {
                dialog.dismiss()
            }

            btnReApply.setOnClickListener {
                callRequestApproveBillApi()
            }
        }
    }

    private fun callRequestApproveBillApi() {
        if (context?.isNetworkAvailable() == true) {
            toggleLoader(true)
            reimbursementViewModel.callUpdateReimbursementStatusApi(
                request = UpdateReimbursementStatusRequestModel(associateReimbursementId)
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }


    private fun setUpAdapter() {
        reimbursementDetailsViewAdapter =
            ReimbursementDetailsViewAdapter(
                this.requireContext(),
                reimbursementDetailsList,
                this,
                isRejected
            )
        binding.recyclerIssueDetails.adapter = reimbursementDetailsViewAdapter
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet.layoutParams
        val windowHeight: Int = getWindowHeight()
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        /*  val displayMetrics = DisplayMetrics()
          (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
          return displayMetrics.heightPixels*/

        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
//        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        //TODO need to check.
        val s = (context as Activity?)!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics: WindowMetrics =
                s.currentWindowMetrics

            val windowInsets = metrics.windowInsets
            val insets: Insets = windowInsets.getInsetsIgnoringVisibility(
                WindowInsets.Type.navigationBars()
                        or WindowInsets.Type.displayCutout()
            )
            val insetsWidth: Int = insets.right + insets.left
            val insetsHeight: Int = insets.top + insets.bottom
            val bounds: Rect = metrics.bounds
            return Size(
                bounds.width() - insetsWidth,
                bounds.height() - insetsHeight
            ).height
        } else {
            return displayMetrics.heightPixels
        }
    }

    override fun clickOnBill(position: Int, associateId: String) {
        if (context?.isNetworkAvailable() == true) {
            toggleLoader(true)
            reimbursementViewModel.callReimbursementBillApi(
                request = ReimbursementDetailsRequestModel(
                    AssociateReimbursementId = associateId
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun clickOnUploadBill(position: Int, associateId: String) {
        associateReimbursementId = associateId
        openAddPhotoBottomSheet()
    }

    override fun clickOnEdit(position: Int, associateId: String) {
        val billList: ReimbursementDetailsModel? = reimbursementDetailsResponseList.find {
            it.AssociateReimbursementDetailId == associateId
        }
        if (billList != null) {
            val i = Intent(this.requireContext(), UpdateReimbursementVoucher::class.java)
            val b = Bundle()
            b.putSerializable(Constant.DATA, billList)
            b.putString(Constant.associateReimbursementId, associateReimbursementId)
            b.putBoolean(Constant.IS_FOR_EDIT_BILL, true)
            i.putExtras(b)
            editBillResult.launch(i)
        }
    }

    private val editBillResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            binding.apply {
                if (result.resultCode == Activity.RESULT_OK) {
                    callReimbursementDetailsApi()
                    btnReApply.visibility = VISIBLE
                } else {
                    btnReApply.visibility = GONE
                }
            }
        }

    private val addImageUtils =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data?.extras
                val imageName = data?.getString(Constant.IntentExtras.EXTRA_FILE_NAME).toString()
                val url = data?.getString(Constant.IntentExtras.EXTRA_FILE_PATH)
                val bitmapFile =
                    File(url.toString())
                val bitmap: Bitmap = BitmapFactory.decodeFile(bitmapFile.toString())
                val bill = ImageUtils.INSTANCE?.bitMapToString(bitmap).toString()
                callUploadBillApi(bill, imageName.split(".").last())
            }
        }

    private val pdfResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data?.extras
                val url = data?.getString(Constant.IntentExtras.EXTRA_FILE_PATH)
                val base64: String =
                    Base64.encodeToString(
                        AppUtils.INSTANCE?.getPdfToBase64(
                            Uri.parse(url.toString()),
                            this.requireContext()
                        ), Base64.NO_WRAP
                    )
                if (base64.length > 1000000) {
                    showToast(getString(R.string.pdf_file_size_more_than_1_mb_not_allowed))
                } else {
                    callUploadBillApi(base64, "pdf")
                }
            }
        }

    private fun callUploadBillApi(bill: String, extn: String) {
        if (context?.isNetworkAvailable() == true) {
            toggleLoader(true)
            reimbursementViewModel.callUploadReimbursementBillApi(
                request = UploadReimbursementBillRequestModel(
                    AssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.AssociateID),
                    AssociateReimbursementId = associateReimbursementId,
                    Extn = extn,
                    FilePath = bill
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun openAddPhotoBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this.requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_add_photo, null)
        addPhotoBottomSheetDialogBinding = BottomSheetAddPhotoBinding.bind(view)
        bottomSheetDialog.apply {
            setCancelable(true)
            setContentView(view)
            show()
        }
        val intent = Intent(context, AddImageUtils::class.java)
        val b = Bundle()
        addPhotoBottomSheetDialogBinding.apply {
            imgPdf.visibility = VISIBLE
            tvPdf.visibility = VISIBLE
            tvAddPhoto.text = getString(R.string.choose_image_pdf)
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
            imgPdf.setOnClickListener {
                b.putBoolean(Constant.IS_CAMERA, false)
                b.putBoolean(Constant.IS_PDF, true)
                intent.putExtras(b)
                pdfResult.launch(intent)
                bottomSheetDialog.dismiss()
            }
        }

    }

    fun showToast(msg: String) {
        AppUtils.INSTANCE?.showLongToast(requireContext(), msg)
    }

    fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.root,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    fun toggleFadeView(
        parent: View,
        loader: View,
        imageView: ImageView,
        showLoader: Boolean
    ) {

        if (showLoader) {
            AppUtils.INSTANCE?.hideFadeView(parent, Constant.VIEW_ANIMATE_DURATION)
            AppUtils.INSTANCE?.showFadeView(loader, Constant.VIEW_ANIMATE_DURATION)
            ImageUtils.INSTANCE?.loadLocalGIFImage(imageView, R.drawable.loader)
            loader.visibility = VISIBLE
        } else {
            AppUtils.INSTANCE?.hideView(loader, Constant.VIEW_ANIMATE_DURATION)
            AppUtils.INSTANCE?.showView(parent, Constant.VIEW_ANIMATE_DURATION)
            loader.visibility = GONE
        }
    }

    fun callReimbursementDetailsApi() {
        if (context?.isNetworkAvailable() == true) {
            toggleLoader(true)
            reimbursementViewModel.callReimbursementDetailsApi(
                request = ReimbursementDetailsRequestModel(
                    AssociateReimbursementId = associateReimbursementId
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

}