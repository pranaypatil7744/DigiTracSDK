package com.example.digitracksdk.presentation.onboarding.bank_account_verification

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityBankAccountBinding
import com.example.digitracksdk.domain.model.home_model.response.InnovIDCardResponseModel
import com.example.digitracksdk.domain.model.onboarding.bank_account_verification.BankAccountVerificationDetailsRequestModel
import com.example.digitracksdk.domain.model.onboarding.bank_account_verification.BankAccountVerificationRequestModel
import com.example.digitracksdk.domain.model.onboarding.bank_account_verification.Details
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.my_profile.adapter.MyProfileAdapter
import com.example.digitracksdk.presentation.my_profile.model.ProfileModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class BankAccountActivity : BaseActivity(), ValidationListener,
    MyProfileAdapter.ProfileClickManager {
    lateinit var binding: ActivityBankAccountBinding
    private var bankDetails: ArrayList<Details> = ArrayList()
    lateinit var preferenceUtils: PreferenceUtils
    private var innovId = ""
    var profilePic: String? = ""
    var mobile: String? = ""

    private val bankAccountVerificationViewModel: BankAccountVerificationViewModel by viewModel()

    //for view profile
    private var profileDetailsList: ArrayList<InnovIDCardResponseModel> = ArrayList()
    private lateinit var myProfileAdapter: MyProfileAdapter
    private var profileItemsList: ArrayList<ProfileModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBankAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        bankAccountVerificationViewModel.listener = this
        getPreferenceData()
        setUpToolbar()
        setUpObserverData()
        setUpListener()
        setUpViewData()
    }

    private fun setUpViewData() {
        profileItemsList.clear()
        profileItemsList.add(
            ProfileModel(
                summaryDetailsType = MyProfileAdapter.SummaryDetailsType.EMP_PROFILE_DETAILS
            )
        )
        profileItemsList.add(
            ProfileModel(
                summaryDetailsType = MyProfileAdapter.SummaryDetailsType.EMP_PROFILE_DETAILS_ITEM
            )
        )
    }

    private fun setUpAdapter() {
        binding.apply {
            myProfileAdapter = MyProfileAdapter(
                this@BankAccountActivity, 
                list = profileItemsList,
                profileDetailsList = profileDetailsList,
                this@BankAccountActivity,
                isFromVerifyBank = true, bankDetails = bankDetails
            )
            recyclerMyProfile.adapter = myProfileAdapter
        }
    }

    private fun getPreferenceData() {
        profilePic = preferenceUtils.getValue(Constant.PreferenceKeys.PROFILE_PIC)
        mobile = preferenceUtils.getValue(Constant.PreferenceKeys.MobileNo)
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
    }

    private fun setUpObserverData() {
        binding.apply {
            with(bankAccountVerificationViewModel) {
                bankAccountVerificationDetailsResponseData.observe(
                    this@BankAccountActivity
                )
                {
                    Log.e("TAG","result-> ${it.beneficiaryname}")
                    if (it.status.toString().lowercase()== Constant.success)
                    {
                        profileItemsList.removeAt(1)
                        bankDetails.clear()

                        bankDetails.addAll(ArrayList<Details>().apply {
                            clear()
                            add(Details(BeneName = it.beneficiaryname))
                        })
                        setUpViewProfileData()
                    }
                    else
                    {
                        callBankVerificationApi()
                    }
                }

                bankAccountVerificationResponseData.observe(
                    this@BankAccountActivity
                )
                {
                    if (it.Status?.lowercase() == Constant.success) {
                        showToast(it.Message.toString())
                        bankDetails.clear()

                        it.Details?.let { it1 ->
                            bankDetails.add(it1)
                            bankDetails[0].TransactionTime=it.request_timestamp
                            if(it1.BankRef.isNullOrEmpty() && it1.BeneName.isNullOrEmpty() &&
                                it1.Remark.isNullOrEmpty() && it1.Status.isNullOrEmpty()){

                            }else{
                                setUpViewProfileData()
                            }
                        }

                    } else {
                        showToast(it.Message.toString())
                    }
                }



                messageData.observe(this@BankAccountActivity) {
                    showToast(it.toString())
                }

                showProgressBar.observe(this@BankAccountActivity) {
                    toggleLoader(it)
                }
            }

        }
    }

    private fun setUpViewProfileData() {
        binding.apply {
            recyclerMyProfile.visibility = View.VISIBLE
            toolbar.tvLeftTitle1.visibility = View.INVISIBLE
            etAccountNumber.isEnabled = false
            etIfscNumber.isEnabled = false
            setUpAdapter()
        }
    }

    private fun getBankAccountVerificationRequestModel(): BankAccountVerificationRequestModel {
        return BankAccountVerificationRequestModel(
            Account = binding.etAccountNumber.text.toString().trim(),
            IFSC = binding.etIfscNumber.text.toString().trim().uppercase(),
            InnovId = innovId
        )

    }


    private fun setUpListener() {
        binding.apply {
            toolbar.tvLeftTitle1.setOnClickListener {
                clearError()
                hideKeyboard(this@BankAccountActivity)
                bankAccountVerificationViewModel.verifyBankDetailsDetails(
                    getBankAccountVerificationRequestModel()

                )
            }
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.bank_verification)
            divider.visibility = View.VISIBLE
            tvLeftTitle1.text = getString(R.string.save)
            tvLeftTitle1.visibility = View.VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
            tvLeftTitle1.setTextColor(
                ContextCompat
                    .getColor(this@BankAccountActivity, R.color.blue_ribbon)
            )
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

    override fun onValidationSuccess(type: String, msg: Int) {

            callBankAccountVerificationDetailsApi()

    }

    private fun callBankAccountVerificationDetailsApi() {

        binding.apply {
            if (isNetworkAvailable())
            {
                bankAccountVerificationViewModel.callBankAccountVerificationDetailsApi(getBankAccountVerificationDetailsRequestModel())

            }else{
                showToast(getString(R.string.no_internet_connection))
            }

        }
    }

    private fun callBankVerificationApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                bankAccountVerificationViewModel.callBankAccountVerificationApi(
                    getBankAccountVerificationRequestModel()
                )

            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    private fun getBankAccountVerificationDetailsRequestModel(): BankAccountVerificationDetailsRequestModel {
      return  BankAccountVerificationDetailsRequestModel(
          AccountNumber = binding.etAccountNumber.text.toString().trim(),
          IFSC = binding.etIfscNumber.text.toString().trim().uppercase(),
          InnovId = innovId

      )
    }

    override fun onValidationFailure(type: String, msg: Int) {
        binding.apply {
            when (type) {
                Constant.ListenerConstants.ACCOUNT_NUMBER_ERROR -> {
                    layoutAccountNumber.error = getString(msg)
                }
                Constant.ListenerConstants.IFSC_ERROR -> {
                    layoutIfscNumber.error = getString(msg)
                }
            }
        }
    }

    private fun clearError() {
        binding.apply {
            layoutAccountNumber.error = ""
            layoutIfscNumber.error = ""
        }
    }

    override fun onEditBtnClick(position: Int) {

    }

}