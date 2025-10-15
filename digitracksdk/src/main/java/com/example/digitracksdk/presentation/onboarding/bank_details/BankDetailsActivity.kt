package com.example.digitracksdk.presentation.onboarding.bank_details

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityBankDetailsBinding
import com.innov.digitrac.databinding.BottomSheetAddPhotoBinding
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.BankListModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.UpdateBankDetailsRequestModel
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.utils.AddImageUtils
import com.example.digitracksdk.utils.ImageUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.digitracksdk.utils.AppUtils
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File

class BankDetailsActivity : BaseActivity(), ValidationListener {

    lateinit var preferenceUtils: PreferenceUtils
    private val bankDetailsViewModel: BankDetailsViewModel by viewModel()
    private lateinit var addPhotoBottomSheetDialogBinding: BottomSheetAddPhotoBinding

    lateinit var binding: ActivityBankDetailsBinding
    private var bankList: ArrayList<String> = ArrayList()
    private var bankListWithId: ArrayList<BankListModel> = ArrayList()
    private var chequeImage: String = ""
    private var chequeImageExtn: String = ""
    var isChequeUpload: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      enableEdgeToEdge()
        binding = ActivityBankDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        bankDetailsViewModel.validationListener = this
        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setObserver()
        callChequeValidationApi()
        setUpListener()
    }

    private fun setObserver() {
        binding.apply {
            with(bankDetailsViewModel) {
                chequeValidationResponseData.observe(this@BankDetailsActivity) {
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        isChequeUpload = it.IsChequeUploaded ?: false
                        setUpView()
                        callViewBankDetailsApi()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                viewBankDetailsResponseData.observe(
                    this@BankDetailsActivity
                ) {
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        etPanCardNumber.setText(if (!it.PANNumber.isNullOrEmpty()) it.PANNumber.toString() else "")
                        etBankName.setText(if (!it.BankName.isNullOrEmpty()) it.BankName.toString() else "")
                        etAccountNumber.setText(if (!it.AccountNumber.isNullOrEmpty()) it.AccountNumber.toString() else "")
                        etIfsc.setText(if (!it.IFSCcode.isNullOrEmpty()) it.IFSCcode.toString() else "")
                        chequeImage = it.ChequeDocImageArr.toString()
                        chequeImageExtn = it.Extn.toString()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                getBankListResponseData.observe(this@BankDetailsActivity) {
                    toggleLoader(false)
                    if (!it.BankList.isNullOrEmpty()) {
                        bankListWithId.clear()
                        bankListWithId.add(BankListModel("0", getString(R.string.Select)))
                        bankListWithId.addAll(it.BankList ?: arrayListOf())
                        bankList.clear()
                        for (i in bankListWithId) {
                            bankList.add(i.BanKName.toString())
                        }
                        val adapter = ArrayAdapter(
                            this@BankDetailsActivity,
                            android.R.layout.simple_list_item_1,
                            bankList
                        )
                        binding.etBankName.setAdapter(adapter)
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                updateBankDetailsResponseData.observe(this@BankDetailsActivity) {
                    if (it.status == Constant.SUCCESS) {
                        showToast(it.Message.toString())
                        finish()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(this@BankDetailsActivity) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }
        }
    }

    private fun callChequeValidationApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                mainLayout.visibility = VISIBLE
                toggleLoader(true)
                bankDetailsViewModel.callChequeValidationApi(
                    request = CommonRequestModel(
                        preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
                    )
                )
            } else {

                mainLayout.visibility = GONE
                layoutNoInternet.root.visibility = VISIBLE
                showToast(getString(R.string.no_internet_connection))
            }

        }
    }

    private fun setUpView() {
        binding.apply {
            if (!isChequeUpload) {
                layoutUploadCheque.visibility = VISIBLE
                btnUploadCheque.visibility = VISIBLE
            } else {
                layoutUploadCheque.visibility = GONE
                btnUploadCheque.visibility = GONE
            }
        }
    }

    private fun callBankListApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            bankDetailsViewModel.callBankListApi()
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callChequeValidationApi()
            }
            btnUploadCheque.setOnClickListener {
                openAddPhotoBottomSheet()
            }
            btnDelete.setOnClickListener {
                chequeImage = ""
                it.visibility = GONE
                etUploadCheque.setText("")
            }
        }
    }

    private fun callViewBankDetailsApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                bankDetailsViewModel.callViewBankApi(
                    request = InnovIDRequestModel(
                        InnovID = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
                    )
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
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

    private fun setUpToolbar() {
        binding.toolbar.apply {
            divider.visibility = VISIBLE
            tvTitle.text = getString(R.string.bank_details)
            btnBack.setOnClickListener {
                finish()
            }
            tvSave.apply {
                visibility = VISIBLE
                text = getString(R.string.edit)
                setOnClickListener {
                    if (text == getString(R.string.edit)) {
                        text = getString(R.string.save)
                        enableFields()
                    } else {
                        clearError()
                        bankDetailsViewModel.validateBankDetails(
                            getUpdateBankDetailsRequestModel(),
                            isChequeUpload
                        )
                    }
                }
            }
        }
    }

    private fun getUpdateBankDetailsRequestModel(): UpdateBankDetailsRequestModel {
        binding.apply {
            val request = UpdateBankDetailsRequestModel()
            request.PANNumber = etPanCardNumber.text.toString().trim()
            request.AccountNumber = etAccountNumber.text.toString().trim()
            request.BankName = etBankName.text.toString().trim()
            request.IFSCcode = etIfsc.text.toString().trim()
            request.ChequeDocImageArr = chequeImage
            request.Extn = chequeImageExtn
            request.BranchDetails = etBranchDetails.text.toString().trim()
            request.InnovID = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
            return request
        }
    }

    private fun enableFields() {
        binding.apply {
            etPanCardNumber.isEnabled = true
            etBankName.isEnabled = true
            etAccountNumber.isEnabled = true
            etIfsc.isEnabled = true
            etBranchDetails.isEnabled = true
            btnUploadCheque.isEnabled = true
            layoutBranchDetails.visibility = VISIBLE
        }
        callBankListApi()
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
                val image = ImageUtils.INSTANCE?.bitMapToString(bitmap).toString()
                chequeImage = image
                chequeImageExtn = fileName.toString().split(".").last()
                binding.apply {
                    etUploadCheque.setText(fileName.toString())
                    btnDelete.visibility = VISIBLE
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

    override fun onValidationSuccess(type: String, msg: Int) {
        callUpdateBankDetailsApi()
    }

    private fun callUpdateBankDetailsApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            bankDetailsViewModel.callUpdateBankDetailsApi(getUpdateBankDetailsRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun onValidationFailure(type: String, msg: Int) {
        binding.apply {
            when (type) {
                Constant.ListenerConstants.PAN_CARD_ERROR -> {
                    layoutPanCardNumber.error = getString(msg)
                    layoutPanCardNumber.requestFocus()
                }

                Constant.ListenerConstants.BANK_NAME_ERROR -> {
                    layoutBankNames.error = getString(msg)
                    layoutBankNames.requestFocus()
                }

                Constant.ListenerConstants.ACCOUNT_NUMBER_ERROR -> {
                    layoutAccountNumber.error = getString(msg)
                    layoutAccountNumber.requestFocus()
                }

                Constant.ListenerConstants.IFSC_ERROR -> {
                    layoutIfsc.error = getString(msg)
                    layoutIfsc.requestFocus()
                }

                Constant.ListenerConstants.CHEQUE_ERROR -> {
                    layoutUploadCheque.error = getString(msg)
                    layoutUploadCheque.requestFocus()
                }
            }
        }
    }

    private fun clearError() {
        binding.apply {
            layoutPanCardNumber.error = ""
            layoutPanCardNumber.clearFocus()
            layoutBankNames.error = ""
            layoutBankNames.clearFocus()
            layoutAccountNumber.error = ""
            layoutAccountNumber.clearFocus()
            layoutIfsc.error = ""
            layoutIfsc.clearFocus()
            layoutUploadCheque.error = ""
            layoutUploadCheque.clearFocus()
        }
    }
}