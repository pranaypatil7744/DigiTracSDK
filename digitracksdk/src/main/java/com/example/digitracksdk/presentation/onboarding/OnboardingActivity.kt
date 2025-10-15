package com.example.digitracksdk.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityOnboardingBinding
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.onboarding.OnboardingDashboardResponseModel
import com.example.digitracksdk.presentation.onboarding.aadhaar_verification_DigiLocker.AadhaarVerificationDigiLockerActivity
import com.example.digitracksdk.presentation.onboarding.adapter.OnboardingListAdapter
import com.example.digitracksdk.presentation.onboarding.bank_account_verification.BankAccountActivity
import com.example.digitracksdk.presentation.onboarding.bank_details.BankDetailsActivity
import com.example.digitracksdk.presentation.onboarding.candidate_details.CandidateDetailsActivity
import com.example.digitracksdk.presentation.onboarding.cibil_score.CibilScoreActivity
import com.example.digitracksdk.presentation.onboarding.document.DocumentDetailsActivity
import com.example.digitracksdk.presentation.onboarding.educational_details.EducationalDetailsActivity
import com.example.digitracksdk.presentation.onboarding.epf_details.EPFDetailsActivity
import com.example.digitracksdk.presentation.onboarding.esic_details.ESICDetailsActivity
import com.example.digitracksdk.presentation.onboarding.family_details_screen.FamilyDetailsActivity
import com.example.digitracksdk.presentation.onboarding.kyc_screen.KycActivity
import com.example.digitracksdk.presentation.onboarding.model.OnboardingListModel
import com.example.digitracksdk.presentation.onboarding.model.OnboardingStatus
import com.example.digitracksdk.presentation.onboarding.pan_verification.PanVerificationActivity
import com.example.digitracksdk.presentation.onboarding.pf_uan.PFUanDetailsActivity
import com.example.digitracksdk.presentation.onboarding.reference_details_screen.ReferenceDetailsActivity
import com.example.digitracksdk.presentation.onboarding.signature_screen.SignatureActivity
import com.example.digitracksdk.presentation.onboarding.work_experience.WorkExperienceViewActivity
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class OnboardingActivity : BaseActivity(), OnboardingListAdapter.OnboardingClickManager {
    lateinit var binding: ActivityOnboardingBinding
    lateinit var onboardingListAdapter: OnboardingListAdapter
    lateinit var preferenceUtils: PreferenceUtils
    private val onBoardingDashboardViewModel: OnboardingDashboardViewModel by viewModel()
    var onboardingList: ArrayList<OnboardingListModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setUpOnboardingAdapter()
        setUpObserver()
        setUpListener()
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callOnBoardingDashboardApi()
            }
        }
    }

    private fun setUpObserver() {
        binding.apply {
            with(onBoardingDashboardViewModel) {
                onBoardingDashboardResponseData.observe(this@OnboardingActivity) {
                    if (it.Status == Constant.SUCCESS) {
                        setUpOnboardingListData(it)
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(this@OnboardingActivity) {
                    showToast(it.toString())
                }

                showProgressBar.observe(this@OnboardingActivity) {
                    toggleLoader(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        AppUtils.INSTANCE?.setLang(this)
        callOnBoardingDashboardApi()
    }

    private fun callOnBoardingDashboardApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                recyclerOnboading.visibility = VISIBLE
                onBoardingDashboardViewModel.callOnBoardingDashboardApi(
                    request =
                    CommonRequestModel(
                        InnovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerOnboading.visibility = GONE
            }
        }
    }

    private fun setUpOnboardingListData(data: OnboardingDashboardResponseModel) {
        onboardingList.clear()
        preferenceUtils = PreferenceUtils(this@OnboardingActivity)

        if (preferenceUtils.getValue(Constant.PreferenceKeys.IsAadharVerification) == "1") {
            onboardingList.add(
                OnboardingListModel(
                    itemIcon = R.drawable.ic_aadhaar_verification,
                    itemName = getString(R.string.aadhaar_verification),
                    status = if ((data.Aadhaarverification.toString()
                            .toIntOrNull() ?: 0) == 1
                    ) OnboardingStatus.COMPLETED else OnboardingStatus.IN_COMPLETED
                )
            )
        }

        if (preferenceUtils.getValue(Constant.PreferenceKeys.IsPANVerification) == "1") {
            onboardingList.add(
                OnboardingListModel(
                    itemIcon = R.drawable.ic_pan_verification,
                    itemName = getString(R.string.pan_card_verification),
                    status = if ((data.PanCardverification.toString()
                            .toIntOrNull() ?: 0) == 1
                    ) OnboardingStatus.COMPLETED else OnboardingStatus.IN_COMPLETED
                )
            )
        }
        if (preferenceUtils.getValue(Constant.PreferenceKeys.IsBankVerification) == "1") {
            onboardingList.add(
                OnboardingListModel(
                    itemIcon = R.drawable.ic_bank_verification,
                    itemName = getString(R.string.bank_verification),
                    status = if ((data.Bankverification.toString()
                            .toIntOrNull() ?: 0) == 1
                    ) OnboardingStatus.COMPLETED else OnboardingStatus.IN_COMPLETED
                )
            )
        }
        onboardingList.add(
            OnboardingListModel(
                itemIcon = R.drawable.ic_pan_verification,
                itemName = getString(R.string.cibil_score),
                status = OnboardingStatus.COMPLETED
            )
        )


        onboardingList.add(
            OnboardingListModel(
                itemIcon = R.drawable.ic_candi_details,
                itemName = getString(R.string.candidate_details),
                status = if ((data.CandidateDetails.toString()
                        .toIntOrNull() ?: 0) == 1
                ) OnboardingStatus.COMPLETED else OnboardingStatus.IN_COMPLETED
            )
        )

        onboardingList.add(
            OnboardingListModel(
                itemIcon = R.drawable.ic_kyc, itemName = getString(R.string.kyc),
                status = if ((data.KYCDocumentDetails.toString()
                        .toIntOrNull() ?: 0) == 1
                ) OnboardingStatus.COMPLETED else OnboardingStatus.IN_COMPLETED
            )
        )
        onboardingList.add(
            OnboardingListModel(
                itemIcon = R.drawable.ic_users2, itemName = getString(R.string.family_details),
                status = if ((data.FamilyDetails.toString()
                        .toIntOrNull() ?: 0) == 1
                ) OnboardingStatus.COMPLETED else OnboardingStatus.IN_COMPLETED
            )
        )
        onboardingList.add(
            OnboardingListModel(
                itemIcon = R.drawable.ic_education,
                itemName = getString(R.string.educational_details),
                status = if ((data.EducationDetails.toString()
                        .toIntOrNull() ?: 0) == 1
                ) OnboardingStatus.COMPLETED else OnboardingStatus.IN_COMPLETED
            )
        )
        onboardingList.add(
            OnboardingListModel(
                itemIcon = R.drawable.ic_work, itemName = getString(R.string.work_experience),
                status = if ((data.WorkExperienceDetails.toString()
                        .toIntOrNull() ?: 0) == 1
                ) OnboardingStatus.COMPLETED else OnboardingStatus.IN_COMPLETED
            )
        )
        onboardingList.add(

            OnboardingListModel(
                itemIcon = R.drawable.ic_epf, itemName = getString(R.string.pf_uan),
                // need to change ,pfUan flag not added in api
                 status = if ((data.PFUAN.toString()
                        .toIntOrNull() ?: 0) == 1
                ) OnboardingStatus.COMPLETED else OnboardingStatus.IN_COMPLETED
            )
        )
        onboardingList.add(
            OnboardingListModel(
                itemIcon = R.drawable.ic_epf, itemName = getString(R.string.epf_details),
                status = if ((data.EPFDetails.toString()
                        .toIntOrNull() ?: 0) == 1
                ) OnboardingStatus.COMPLETED else OnboardingStatus.IN_COMPLETED
            )
        )
        onboardingList.add(
            OnboardingListModel(
                itemIcon = R.drawable.ic_esic, itemName = getString(R.string.esic_details),
                status = if ((data.ESICDetails.toString()
                        .toIntOrNull() ?: 0) == 1
                ) OnboardingStatus.COMPLETED else OnboardingStatus.IN_COMPLETED
            )
        )
        onboardingList.add(
            OnboardingListModel(
                itemIcon = R.drawable.ic_documents, itemName = getString(R.string.document_details),
                status = if ((data.DocumentDetails.toString()
                        .toIntOrNull() ?: 0) == 1
                ) OnboardingStatus.COMPLETED else OnboardingStatus.IN_COMPLETED
            )
        )
        onboardingList.add(
            OnboardingListModel(
                itemIcon = R.drawable.ic_bank, itemName = getString(R.string.bank_details),
                status = if ((data.CandidateBankDetails.toString()
                        .toIntOrNull() ?: 0) == 1
                ) OnboardingStatus.COMPLETED else OnboardingStatus.IN_COMPLETED
            )
        )
        onboardingList.add(
            OnboardingListModel(
                itemIcon = R.drawable.ic_referance_friends,
                itemName = getString(R.string.reference_details),
                status = if ((data.CandidateReferenceDetails.toString()
                        .toIntOrNull() ?: 0) == 1
                ) OnboardingStatus.COMPLETED else OnboardingStatus.IN_COMPLETED
            )
        )
        onboardingList.add(
            OnboardingListModel(
                itemIcon = R.drawable.ic_sign, itemName = getString(R.string.signature),
                status = if ((data.CandidateSignature.toString()
                        .toIntOrNull() ?: 0) == 1
                ) OnboardingStatus.COMPLETED else OnboardingStatus.IN_COMPLETED
            )
        )

        onboardingListAdapter.notifyDataSetChanged()
    }

    private fun setUpOnboardingAdapter() {
        onboardingListAdapter = OnboardingListAdapter(this, onboardingList, this)
        binding.recyclerOnboading.adapter = onboardingListAdapter
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.onboarding)
            divider.visibility = VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    override fun clickOnboardingItem(position: Int) {
        when (onboardingList[position].itemName) {
            getString(R.string.aadhaar_verification) -> {
//                startActivity(Intent(this, AadhaarVerificationActivity::class.java))
                startActivity(Intent(this, AadhaarVerificationDigiLockerActivity::class.java))
            }

            getString(R.string.pan_card_verification) -> {
                startActivity(Intent(this, PanVerificationActivity::class.java))
            }

            getString(R.string.cibil_score) -> {
                startActivity(Intent(this, CibilScoreActivity::class.java))
            }

            getString(R.string.bank_verification) -> {
                startActivity(Intent(this, BankAccountActivity::class.java))
            }

            getString(R.string.candidate_details) -> {
                startActivity(Intent(this, CandidateDetailsActivity::class.java))
            }

            getString(R.string.kyc) -> {
                startActivity(Intent(this, KycActivity::class.java))
            }

            getString(R.string.family_details) -> {
                startActivity(Intent(this, FamilyDetailsActivity::class.java))
            }

            getString(R.string.educational_details) -> {
                startActivity(Intent(this, EducationalDetailsActivity::class.java))
            }

            getString(R.string.work_experience) -> {
                startActivity(Intent(this, WorkExperienceViewActivity::class.java))
            }

            getString(R.string.epf_details) -> {
                startActivity(Intent(this, EPFDetailsActivity::class.java))
            }

            getString(R.string.esic_details) -> {
                startActivity(Intent(this, ESICDetailsActivity::class.java))
            }
            getString(R.string.pf_uan)->{
                startActivity(Intent(this , PFUanDetailsActivity::class.java))
            }


            getString(R.string.document_details) -> {
                startActivity(Intent(this, DocumentDetailsActivity::class.java))
            }

            getString(R.string.bank_details) -> {
                startActivity(Intent(this, BankDetailsActivity::class.java))
            }

            getString(R.string.reference_details) -> {
                startActivity(Intent(this, ReferenceDetailsActivity::class.java))
            }

            getString(R.string.signature) -> {
                startActivity(Intent(this, SignatureActivity::class.java))
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
}