package com.example.digitracksdk.presentation.home

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View.*
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityHomeBinding
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.home_model.SurveyLinkRequestModel
import com.example.digitracksdk.domain.model.home_model.response.HomeDashboardRequestModel
import com.example.digitracksdk.domain.model.home_model.response.HomeDashboardResponseModel
import com.example.digitracksdk.presentation.home.home_fragment.HomeDashboardViewModel
import com.example.digitracksdk.presentation.home.home_fragment.HomeFragment
import com.example.digitracksdk.presentation.home.nav_drawer.NavDrawerFragment
import com.example.digitracksdk.presentation.home.notification.NotificationActivity
import com.example.digitracksdk.presentation.my_profile.view_profile.MyProfileActivity
import com.example.digitracksdk.presentation.web_view.WebViewActivity
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.DialogUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity(), DialogUtils.DialogManager {
    lateinit var binding: ActivityHomeBinding
    private val homeDashboardViewModel: HomeDashboardViewModel by viewModel()
    private var navFragment = NavDrawerFragment.newInstance()
    private var homeFragment = HomeFragment.newInstance()
    lateinit var preferenceUtils: PreferenceUtils
    var profilePic: Bitmap? = null
    private var inductionTrainingUrl: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        preferenceUtils = PreferenceUtils(this)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        setUpToolbar()
        setUpObserver()
        callInductionTrainingApi()
//        callHomeDashboardApi()
        setUpListener()
    }


    private fun setUpObserver() {
        binding.apply {
            with(homeDashboardViewModel) {

                inductionTrainingResponseData.observe(
                    this@HomeActivity
                )
                {
                    toggleLoader(false)
                    if (it.Status?.lowercase() == Constant.failed) {
                        inductionTrainingUrl = it.URL.toString()
                        DialogUtils.showPolicyAcknowledgeDialog(
                            this@HomeActivity,
                            listener = this@HomeActivity,
                            message = it.Message.toString(),
                        )
                    } else {

                        callHomeDashboardApi()
                    }
                }

                responseData.observe(this@HomeActivity) {
                    toggleLoader(false)
                    val name = "${it.FirstName} ${it.LastName}"
                    toolbar.apply {
                        tvTitle.visibility = VISIBLE
                        tvTitle.text = name
                        if (it.Image.isNullOrEmpty()) {
                            imgProfile.setImageResource(R.drawable.profile_placeholder)
                        } else {
                            profilePic = com.example.digitracksdk.utils.ImageUtils.INSTANCE?.stringToBitMap(it.Image)
                            com.example.digitracksdk.utils.ImageUtils.INSTANCE?.loadBitMap(imgProfile, profilePic)
                        }
                        if ((it.NotificationCount ?: 0) >= 1) {
                            badgeNotification.visibility = VISIBLE
                            badgeNotification.text = it.NotificationCount.toString()
                        } else {
                            badgeNotification.visibility = GONE
                        }
                    }

                    homeFragment.profilePercentage = it.ProfilePercentage ?: 0
                    navFragment.name = name
                    navFragment.email = "" //todo add email key
                    navFragment.profilePic = com.example.digitracksdk.utils.ImageUtils.INSTANCE?.stringToBitMap(it.Image)
                    savePreferenceData(it)
                    if (it.SurveyPopup == 1)
                    {
                        DialogUtils.showSurveyDialog(
                            this@HomeActivity,
                            listener = this@HomeActivity,
                            closedIcon = it.AllowPopUpClosed
                        )
                    }else {
                        setUpView()
                    }

                }
                surveyLinkResponseData.observe(this@HomeActivity)
                {
                    toggleLoader(false)
                    if (it.status?.lowercase() == Constant.success)
                    {
                        val i = Intent(this@HomeActivity, WebViewActivity::class.java)
                        val b = Bundle()
                        b.putString(Constant.WEB_URL, it.SurveyLink)
                        b.putString(Constant.SCREEN_NAME ,getString(R.string.coc))
                        i.putExtras(b)
                        startForResult.launch(i)
                    }else{
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(this@HomeActivity) {
                    toggleLoader(false)
                    showToast(it)
                }
            }

        }
    }


    private fun callInductionTrainingApi() {
        if (this.isNetworkAvailable()) {
            toggleLoader(true)
            homeDashboardViewModel.callInductionTrainingApi(
                CommonRequestModel(
                    InnovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }


    }

    private fun setUpListener() {
        binding.layoutNoInternetConnection.btnTryAgain.setOnClickListener {
//            callHomeDashboardApi()
            callInductionTrainingApi()
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            btnBack.visibility = INVISIBLE
            imgProfile.visibility = VISIBLE
            tvHi.visibility = VISIBLE
            btnOne.setImageResource(R.drawable.ic_notification)
            btnOne.visibility = VISIBLE
            divider.visibility = VISIBLE

            imgProfile.setOnClickListener {

                startActivity(Intent(this@HomeActivity, MyProfileActivity::class.java))
            }

            btnOne.setOnClickListener {
                startActivity(Intent(this@HomeActivity, NotificationActivity::class.java))
            }

        }
    }

    private fun setUpView() {
        setHomeFragment(homeFragment)
    }

    private fun setHomeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.containerHome.id, fragment)
        transaction.commit()
    }

    private fun showNoInternetLayout(isShow: Boolean) {
        binding.layoutNoInternetConnection.root.visibility = if (isShow) VISIBLE else GONE
    }

    private fun callHomeDashboardApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            showNoInternetLayout(false)
            homeDashboardViewModel.callHomeDashboardApi(getHomeDashboardRequestModel())
        } else {
            showNoInternetLayout(true)
        }
    }

    private fun savePreferenceData(data: HomeDashboardResponseModel) {
        preferenceUtils.setValue(Constant.PreferenceKeys.PROFILE_PIC, data.Image)
        preferenceUtils.setValue(Constant.PreferenceKeys.FIRST_NAME, data.FirstName)
        preferenceUtils.setValue(Constant.PreferenceKeys.MiddleName, data.MiddleName)
        preferenceUtils.setValue(Constant.PreferenceKeys.LAST_NAME, data.LastName)
        preferenceUtils.setValue(
            Constant.PreferenceKeys.FULL_NAME,
            "${data.FirstName} ${data.MiddleName} ${data.LastName}"
        )
        preferenceUtils.setValue(
            Constant.PreferenceKeys.LogYourVisit,
            data.LogYourVisit ?: "0"
        )
        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsAttendanceCamera,
            data.IsAttendanceCamera.toString()
        )
        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsDocumentDownLoad,
            data.IsDocumentDownLoad?.let { AppUtils.INSTANCE?.getBooleanFromInt(it) } ?: false)
        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsClientPolicies,
            data.IsClientPolicies?.let { AppUtils.INSTANCE?.getBooleanFromInt(it) } ?: false)
        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsLeave,
            data.IsLeave?.let { AppUtils.INSTANCE?.getBooleanFromInt(it) } ?: false)
        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsTraining,
            data.IsTraining?.let { AppUtils.INSTANCE?.getBooleanFromInt(it) } ?: false)
        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsInnovIdCard,
            data.IsInnovIdCard?.let { AppUtils.INSTANCE?.getBooleanFromInt(it) } ?: false)
        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsCustomeridCard,
            data.IsCustomeridCard?.let { AppUtils.INSTANCE?.getBooleanFromInt(it) } ?: false)
        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsAssociate,
            data.IsAssociate?.let { AppUtils.INSTANCE?.getBooleanFromInt(it) } ?: false)
        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsDigiTracReimbursement,
            data.IsDigiTracReimbursement?.let { AppUtils.INSTANCE?.getBooleanFromInt(it) } ?: false)


        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsDigiTracResignation,
            data.IsDigiTracResignation?.let { AppUtils.INSTANCE?.getBooleanFromInt(it) } ?: false)

        preferenceUtils.setValue(
            Constant.PreferenceKeys.NewResignation,
            data.NewResignationDD?.let {
                AppUtils.INSTANCE?.getBooleanFromInt(it)
            } ?: false
        )

        preferenceUtils.setValue(
            Constant.PreferenceKeys.AttendanceFromAnyWhere,
            data.AttendanceFromAnyWhere?.let { AppUtils.INSTANCE?.getBooleanFromInt(it) } ?: false)
        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsChatBoardEnabled,
            data.IsChatBoardEnabled?.let { AppUtils.INSTANCE?.getBooleanFromInt(it) } ?: false)
        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsGPSAttendance,
            data.IsGPSAttendance?.let { AppUtils.INSTANCE?.getBooleanFromInt(it) } ?: false)
        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsViewPayouts,
            data.IsViewPayouts?.let { AppUtils.INSTANCE?.getBooleanFromInt(it) } ?: false)
        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsNewReimbursement,
            data.IsNewReimbursement?.let { AppUtils.INSTANCE?.getBooleanFromInt(it) } ?: false)
        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsPendingReimbursementNew,
            data.IsPendingReimbursementNew?.let { AppUtils.INSTANCE?.getBooleanFromInt(it) }
                ?: false)
        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsPendingReimbursement,
            data.IsPendingReimbursement?.let { AppUtils.INSTANCE?.getBooleanFromInt(it) } ?: false)
        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsIncomeTaxDeclaration,
            data.IsIncomeTaxDeclaration?.let { AppUtils.INSTANCE?.getBooleanFromInt(it) } ?: false)

        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsGeoTracking,
            data.geoTracking?.let { AppUtils.INSTANCE?.getBooleanFromInt(it) } ?: false)

        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsRefyneApplicable,
            data.IsRefyneApplicable?.let { AppUtils.INSTANCE?.getBooleanFromInt(it) } ?: false)

        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsPaySlip,
            data.IsPaySlip?.let { AppUtils.INSTANCE?.getBooleanFromInt(it) } ?: false)
        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsAadharVerification,
            data.IsAadharVerification
        )
        preferenceUtils.setValue(Constant.PreferenceKeys.IsPANVerification, data.IsPANVerification)
        preferenceUtils.setValue(Constant.PreferenceKeys.IsCibilScore, data.IsPANVerification)

        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsBankVerification,
            data.IsBankVerification
        )


        preferenceUtils.setValue(Constant.PreferenceKeys.ShowAwakeTracking, data.ShowAwakeTracking)
        preferenceUtils.setValue(Constant.PreferenceKeys.CandidateID, data.CandidateID)
        preferenceUtils.setValue(Constant.PreferenceKeys.AssociateID, data.AssociateID)
        preferenceUtils.setValue(Constant.PreferenceKeys.DOB, data.DOB)
        preferenceUtils.setValue(Constant.PreferenceKeys.MobileNo, data.MobileNo)
        preferenceUtils.setValue(Constant.PreferenceKeys.ChecksumValue, data.ChecksumValue)
        preferenceUtils.setValue(
            Constant.PreferenceKeys.ProfilePercentage,
            data.ProfilePercentage ?: 0
        )
        preferenceUtils.setValue(Constant.PreferenceKeys.IsCheckSum, data.IsCheckSum ?: 0)
        preferenceUtils.setValue(Constant.PreferenceKeys.RegID, data.RegID)
        preferenceUtils.setValue(Constant.PreferenceKeys.GnetAssociateID, data.GnetAssociateID)
        preferenceUtils.setValue(Constant.PreferenceKeys.Isjoiningpending, data.Isjoiningpending)
        preferenceUtils.setValue(Constant.PreferenceKeys.DateofJoining, data.DateofJoining)
        preferenceUtils.setValue(
            Constant.PreferenceKeys.EmergencyContactName,
            data.EmergencyContactName
        )
        preferenceUtils.setValue(
            Constant.PreferenceKeys.EmergencyContactNo,
            data.EmergencyContactNo
        )
        preferenceUtils.setValue(Constant.PreferenceKeys.ActualDOJ, data.ActualDOJ)
        preferenceUtils.setValue(
            Constant.PreferenceKeys.NotificationCount,
            data.NotificationCount ?: 0
        )
        preferenceUtils.setValue(Constant.PreferenceKeys.ClientID, data.ClientID)
        preferenceUtils.setValue(Constant.PreferenceKeys.onboardingStatus, data.onboardingStatus)
        preferenceUtils.setValue(Constant.PreferenceKeys.Letters, data.Letters)
        preferenceUtils.setValue(Constant.PreferenceKeys.DailyMileageLimit, data.DailyMileageLimit)
        preferenceUtils
        preferenceUtils.setValue(
            Constant.PreferenceKeys.DailyMileageFourWheelar,
            data.DailyMileageFourWheelar
        )
        preferenceUtils.setValue(
            Constant.PreferenceKeys.AllowForAttendance,
            data.AllowForAttendance
        )
        preferenceUtils.setValue(Constant.PreferenceKeys.LastWorkingDate, data.LastWorkingDate)
        preferenceUtils.setValue(
            Constant.PreferenceKeys.ReimbursementAllowDays,
            data.ReimbursementAllowDays
        )

        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsLeave,
            data.IsLeave?.let { AppUtils.INSTANCE?.getBooleanFromInt(it) } ?: false)

        preferenceUtils.setValue(
            Constant.PreferenceKeys.AttendanceCalendar, data.QuickRegularization?.let {
                AppUtils.INSTANCE?.getBooleanFromInt(it)
            } ?: false
        )

        preferenceUtils.setValue(
            Constant.PreferenceKeys.QuickAttendance,
            data.QuickAttendance?.let {
                AppUtils.INSTANCE?.getBooleanFromInt(it)
            } ?: false
        )

        preferenceUtils.setValue(
            Constant.PreferenceKeys.IsRewards,
            data.IsRewardsVisible?.let {
                AppUtils.INSTANCE?.getBooleanFromInt(it)
            } ?: false
        )
      /*  preferenceUtils.setValue(
            Constant.PreferenceKeys.SurveyPopup,
            data.SurveyPopup?.let {
                AppUtils.INSTANCE?.getBooleanFromInt(it)
            }?:false
        )
        preferenceUtils.setValue(
            Constant.PreferenceKeys.AllowPopUpClosed,
            data.AllowPopUpClosed?.let {
                AppUtils.INSTANCE?.getBooleanFromInt(it)
            }?:false
        )*/

    }

    private fun getHomeDashboardRequestModel(): HomeDashboardRequestModel {
        val request = HomeDashboardRequestModel()
        request.IMEI = AppUtils.INSTANCE?.getDeviceId(this)
        request.InnovID = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
        return request
    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.root,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    override fun onContinueClick()
    {
        DialogUtils.closeSurveyDialog()
        if (isNetworkAvailable()) {
            toggleLoader(true)
            showNoInternetLayout(false)
            homeDashboardViewModel.callSurveyLinkApi(getSurveyLinkRequestModel())
        } else {
            showNoInternetLayout(true)
        }

    }

    private fun getSurveyLinkRequestModel(): SurveyLinkRequestModel {
       val request = SurveyLinkRequestModel()
        request.InnovID = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
        request.AssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.AssociateID)
        return  request
    }


    override fun onPolicyAcknowledgeClick() {
        DialogUtils.closePolicyAcknowledgeDialog()
        val i = Intent(this@HomeActivity, WebViewActivity::class.java)
        val b = Bundle()
        b.putString(Constant.WEB_URL, inductionTrainingUrl)
        b.putString(Constant.SCREEN_NAME, getString(R.string.induction_training))
        i.putExtras(b)
        startForResult.launch(i)
    }


    private val startForResult = registerForActivityResult(
        ActivityResultContracts
            .StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            callInductionTrainingApi()
        }
    }

}