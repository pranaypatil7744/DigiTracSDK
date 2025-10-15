package com.example.digitracksdk.presentation.onboarding.pan_verification

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.example.digitracksdk.Constant
import com.innov.digitrac.base.BaseActivity
import com.example.digitracksdk.domain.model.home_model.response.InnovIDCardResponseModel
import com.example.digitracksdk.domain.model.onboarding.pan_verification.PanCardVerificationRequestModel
import com.example.digitracksdk.domain.model.onboarding.pan_verification.Result
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.my_profile.adapter.MyProfileAdapter
import com.example.digitracksdk.presentation.my_profile.model.ProfileModel
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import com.innov.digitrac.R
import com.innov.digitrac.databinding.ActivityPanVerificationBinding
import com.example.digitracksdk.domain.model.onboarding.pan_verification.GetPanCardVerificationDetailsRequestModel
import com.example.digitracksdk.utils.AppUtils
import org.koin.android.viewmodel.ext.android.viewModel

class PanVerificationActivity : BaseActivity(), ValidationListener,
    MyProfileAdapter.ProfileClickManager {
    lateinit var binding: ActivityPanVerificationBinding
    private var isView: Boolean = false
    private var panDetails: ArrayList<Result> = ArrayList()

    lateinit var preferenceUtils: PreferenceUtils
    private var innovId = ""
    var mobile: String? = ""

    private val panVerificationViewModel: PanVerificationViewModel by viewModel()

    //for view profile
    private var profileDetailsList: ArrayList<InnovIDCardResponseModel> = ArrayList()
    private lateinit var myProfileAdapter: MyProfileAdapter
    private var profileItemsList: ArrayList<ProfileModel> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPanVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        panVerificationViewModel.listener = this
        getPreferenceData()
        getIntentData()
        setUpToolbar()
        setUpObserverData()
        setUpListener()
        setUpViewData()
    }

    private fun getIntentData() {
        binding.apply {
            intent?.extras?.run {
                isView = getBoolean(Constant.IS_VIEW, false)
            }
        }
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
                this@PanVerificationActivity,
                list = profileItemsList,
                profileDetailsList = profileDetailsList,
                this@PanVerificationActivity,
                isFromVerifyPanCard = true,
                panCardDetails = panDetails
            )
            recyclerMyProfile.adapter = myProfileAdapter
        }
    }

    private fun getPreferenceData() {
        mobile = preferenceUtils.getValue(Constant.PreferenceKeys.MobileNo)
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
    }

    private fun setUpObserverData() {
        binding.apply {
            with(panVerificationViewModel) {

                panVerificationDetailsResponseData.observe(this@PanVerificationActivity)
                {
                    if (it.status?.lowercase().equals(Constant.success, true)) {
                        profileItemsList.removeAt(1)
                        panDetails.clear()
                        panDetails.addAll(ArrayList<Result>().apply {
                            clear()
                            add(Result(user_full_name = it.user_full_name))
                            add(Result(pan_number = it.Pan_number))
                        })

                        setUpViewProfileData()

                    } else {
                        panVerificationViewModel.callPanVerificationApi(
                            getPanVerificationRequestModel()
                        )
                    }
                }

                panVerificationResponseData.observe(
                    this@PanVerificationActivity
                ) {
                    if (it.success == true) {
                        showToast(it.response_message.toString())
                        panDetails.clear()
                        it.result?.let { it1 ->
                            panDetails.add(it1)
                            setUpViewProfileData()
                        }
                    } else {
                        showToast(it.response_message.toString())
                    }
                }

                messageData.observe(this@PanVerificationActivity) {
                    showToast(it.toString())
                }

                showProgressBar.observe(this@PanVerificationActivity) {
                    toggleLoader(it)
                }
            }

        }
    }

    private fun setUpViewProfileData() {
        binding.apply {
            recyclerMyProfile.visibility = View.VISIBLE
            toolbar.tvLeftTitle1.visibility = View.INVISIBLE
            etPanNumber.isEnabled = false
            checkAck.isEnabled = false
            setUpAdapter()
        }
    }

    private fun getPanCardDetailsRequestModel(): GetPanCardVerificationDetailsRequestModel {
        return GetPanCardVerificationDetailsRequestModel(
            InnovID = innovId,
            PanNo = binding.etPanNumber.text.toString().trim().uppercase(),
        )
    }

    private fun getPanVerificationRequestModel(): PanCardVerificationRequestModel {
        return PanCardVerificationRequestModel(
            PANNumber = binding.etPanNumber.text.toString().trim().uppercase(),
            InnovId = innovId
        )

    }


    private fun setUpListener() {
        binding.apply {
            toolbar.tvLeftTitle1.setOnClickListener {
                clearError()
                hideKeyboard(this@PanVerificationActivity)
                panVerificationViewModel.verifyPanDetails(
                    getPanVerificationRequestModel()
                )
            }
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.pan_card_verification)
            divider.visibility = View.VISIBLE
            tvLeftTitle1.text = getString(R.string.save)
            tvLeftTitle1.visibility = View.VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
            tvLeftTitle1.setTextColor(
                ContextCompat
                    .getColor(this@PanVerificationActivity, R.color.blue_ribbon)
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
        if (binding.checkAck.isChecked)
            callPanVerificationApi()
        else
            showToast(getString(R.string.please_select_term_condition))
    }

    private fun callPanVerificationApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                panVerificationViewModel.callPanVerificationDetailsApi(getPanCardDetailsRequestModel())

            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }


    override fun onValidationFailure(type: String, msg: Int) {
        binding.apply {
            when (type) {
                Constant.ListenerConstants.PAN_CARD_ERROR -> {
                    layoutPanCardNumber.error = getString(msg)
                }
            }
        }
    }

    private fun clearError() {
        binding.apply {
            layoutPanCardNumber.error = ""
        }
    }

    override fun onEditBtnClick(position: Int) {

    }

}