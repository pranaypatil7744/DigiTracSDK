package com.example.digitracksdk.presentation.home.home_fragment

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.digitracksdk.Constant
import com.example.digitracksdk.Constant.Companion.DIGI_ASSIST
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseFragment
import com.example.digitracksdk.databinding.FragmentHomeBinding
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.GnetIdRequestModel
import com.example.digitracksdk.domain.model.attendance_model.UpdateAttendanceStatusRequestModel
import com.example.digitracksdk.domain.model.client_policies.PolicyAcknowledgeRequestModel
import com.example.digitracksdk.domain.model.customer_id_card.CustomerIdCardRequestModel
import com.example.digitracksdk.domain.model.home_model.HomeBannerRequestModel
import com.example.digitracksdk.domain.model.home_model.ListBannerModel
import com.example.digitracksdk.domain.model.income_tax.IncomeTaxDeclarationRequestModel
import com.example.digitracksdk.domain.model.refine.RefineRequest
import com.example.digitracksdk.presentation.attendance.AttendanceActivity
import com.example.digitracksdk.presentation.attendance.AttendanceViewModel
import com.example.digitracksdk.presentation.attendance.attendance_regularization.AttendanceRegularizationActivity
import com.example.digitracksdk.presentation.attendance.mileage_tracking.MileageTrackingActivity
import com.example.digitracksdk.presentation.attendance.new_timesheet.NewTimeSheetActivity
import com.example.digitracksdk.presentation.attendance.new_timesheet.NewTimeSheetViewActivity
import com.example.digitracksdk.presentation.attendance.time_sheet.AttendanceTimeSheetActivity
import com.example.digitracksdk.presentation.attendance.view_attendance.ViewAttendanceActivity
import com.example.digitracksdk.presentation.attendance.view_attendance_new.ViewAttendanceNewActivity
import com.example.digitracksdk.presentation.home.BannerViewActivity
import com.example.digitracksdk.presentation.home.CustomerIdCardViewModel
import com.example.digitracksdk.presentation.home.IncomeTaxViewModel
import com.example.digitracksdk.presentation.home.RefineViewModel
import com.example.digitracksdk.presentation.home.client_policy.ClientPoliciesViewModel
import com.example.digitracksdk.presentation.home.client_policy.ClientPolicyActivity
import com.example.digitracksdk.presentation.home.exit_questionnaire.ExitQuestionnaireActivity
import com.example.digitracksdk.presentation.home.geo_tracking.GeoTrackingSummaryActivity
import com.example.digitracksdk.presentation.home.geo_tracking_2.GeoTrackingListingActivity
import com.example.digitracksdk.presentation.home.help_and_support.HelpAndSupportActivity
import com.example.digitracksdk.presentation.home.home_fragment.adapter.HomeAdapter
import com.example.digitracksdk.presentation.home.home_fragment.model.HomeDashboardMenu
import com.example.digitracksdk.presentation.home.home_fragment.model.HomeModel
import com.example.digitracksdk.presentation.home.innov_id_card.InnovIdCardActivity
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.JobsAndReferFdsActivity
import com.example.digitracksdk.presentation.home.new_reimbursement.Reimbursement1Activity
import com.example.digitracksdk.presentation.home.pending_reimbursement.PendingReimbursementActivity
import com.example.digitracksdk.presentation.home.reimbursements.ReimbursementActivity
import com.example.digitracksdk.presentation.home.rewards.RewardsActivity
import com.example.digitracksdk.presentation.home.training.TrainingActivity
import com.example.digitracksdk.presentation.home.view_payout.ViewPayoutActivity
import com.example.digitracksdk.presentation.leaves.LeavesActivity
import com.example.digitracksdk.presentation.my_letters.MyLettersActivity
import com.example.digitracksdk.presentation.onboarding.OnboardingActivity
import com.example.digitracksdk.presentation.payslip.PayslipActivity
import com.example.digitracksdk.utils.ImageUtils
import com.example.digitracksdk.presentation.pf_esic_insurance.PfEsicInsuranceActivity
import com.example.digitracksdk.presentation.resignation.ResignationActivity
import com.example.digitracksdk.presentation.resignation_new.ResignationNewActivity
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.presentation.web_view.WebViewActivity
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.DialogUtils
import com.example.digitracksdk.utils.PermissionUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import com.example.digitracksdk.utils.*
import org.koin.android.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment(), HomeAdapter.HomeOnClickManager, DialogUtils.DialogManager {

    lateinit var binding: FragmentHomeBinding
    private lateinit var homeAdapter: HomeAdapter
    private var homeItemList: ArrayList<HomeModel> = ArrayList()
    private var homeBannerList: ArrayList<ListBannerModel> = ArrayList()
    private var homeItemsWithoutCountsList: ArrayList<HomeDashboardMenu> = ArrayList()
    var profilePercentage: Int = 0
    private var today: String = ""
    private var isLeave: Boolean = false
    private var isTraining: Boolean = false
    private var isResignation: Boolean = false
    private var isRewards : Boolean = false
    private var isNewResignation: Boolean = false
    private var isReimbursement: Boolean = false
    private var isClientPolicy: Boolean = false
    private var isGpsAttendance: Boolean = false
    private var isAttedanceFromAnywhere: Boolean = false
    private var isAssociateId: Boolean = false
    private var isLetters: Boolean = false
    private var isInnovIdCard: Boolean = false
    private var isViewPayouts: Boolean = false
    private var isNewReimbursement: Boolean = false
    private var isPendingReimbursement: Boolean = false
    private var isPendingReimbursementNew: Boolean = false
    private var isIncomeTaxDeclaration: Boolean = false
    private var isCustomerIdCard: Boolean = false
    private var isGeoTracking: Boolean = false
    private var isPaySlip: Boolean = false
    private var isRefyneApplicable: Boolean = false
    private var isAttendanceCalendar: Boolean = false
    private var isQuickAttendance : Boolean = false

    private var isFirstOpenCheckIn: Boolean = true
    private var isFirstOpenCheckOut: Boolean = true

    private val homeBannerViewModel: HomeBannerViewModel by viewModel()
    private val homeDashboardViewModel: HomeDashboardViewModel by viewModel()
    private val attendanceViewModel: AttendanceViewModel by viewModel()
    private val refineViewModel: RefineViewModel by viewModel()
    private val incomeTaxViewModel: IncomeTaxViewModel by viewModel()

    private val customerIdCardViewModel: CustomerIdCardViewModel by viewModel()
    private val clientPoliciesViewModel: ClientPoliciesViewModel by viewModel()

    private var attendanceDate: String = ""
    private var attendanceStatus: String = ""
    private var innovId: String = ""
    private var gnetAssociateId: String = ""
    lateinit var preferenceUtils: PreferenceUtils

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceUtils = PreferenceUtils(this.requireContext())
        setUpAdapter()
        today = AppUtils.INSTANCE?.getCurrentDate().toString()
        getPreferenceData()
        setObserver()
        callHomeBannerApi()
        setUpView()
        setUpListener()
    }


    fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.root,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    private fun setObserver() {
        binding.apply {
            with(incomeTaxViewModel) {
                incomeTaxDeclarationResponseData.observe(requireActivity()) {
                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        val i = Intent(requireContext(), WebViewActivity::class.java)
                        val b = Bundle()
                        b.putString(
                            Constant.WEB_URL,
                            it.URL + "?innovid=" + preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
                        )
                        i.putExtras(b)
                        startActivity(i)
                    } else {
                        showToast(it.Message.toString())
                    }
                }
                messageData.observe(requireActivity()) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }

            with(refineViewModel) {
                refineResponseData.observe(requireActivity()) {
                    toggleLoader(false)
                    if (it.URL.isNotEmpty()) {
                        val i = Intent(requireContext(), WebViewActivity::class.java)
                        val b = Bundle()
                        b.putString(Constant.WEB_URL, it.URL)
                        i.putExtras(b)
                        startActivity(i)
                    } else {
                        showToast(getString(R.string.something_went_wrong))
                    }
                }
                messageData.observe(requireActivity()) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }

            with(homeBannerViewModel) {
                homeBannerViewModel.responseData.observe(this@HomeFragment.requireActivity()) {
                    homeBannerList.addAll(it.lstBanners)
                    homeAdapter.notifyDataSetChanged()
                }
                homeBannerViewModel.messageData.observe(this@HomeFragment.requireActivity()) {
                    showToast(it)
                }
            }

            with(homeDashboardViewModel) {
                birthdayResponseData.observe(this@HomeFragment.viewLifecycleOwner) {
                    if (it.IsBirthday == 1) {
                        val isCloseBirthdayBanner = preferenceUtils.getValue(
                            Constant.PreferenceKeys.CloseBirthdayBanner,
                            false
                        )
                        val bMsg =
                            if (it.BirthdayMessage != null && it.BirthdayMessage != "null" && it.BirthdayMessage.toString()
                                    .isNotEmpty()
                            ) {
                                it.BirthdayMessage.toString()
                            } else {
                                ""
                            }
                        if (!isCloseBirthdayBanner) {
                            openBirthdayDialog(
                                bMsg,
                                it.BdayImageArr.toString()
                            )
                        }
                    }
                }
                messageData.observe(this@HomeFragment.viewLifecycleOwner) {
                    showToast(it)
                }
            }

            with(attendanceViewModel) {
                attendanceStatusResponseData.observe(viewLifecycleOwner) {
//                toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        if (it.IsOUT == "0" && it.IsTodaysAttendance == "0") {
                            attendanceDate = it.Attendancedate.toString()
                            val msg =
                                getString(R.string.you_have_not_checkout_from_attendance_on) + "\n" + it.Attendancedate + "\n" + getString(
                                    R.string.please_checkout
                                )
                            DialogUtils.showLogOutDialog(
                                this@HomeFragment.requireContext(),
                                listener = this@HomeFragment,
                                msg = msg,
                                title = getString(R.string.confirm_checkout),
                                confirm = getString(R.string.yes_checkout)
                            )
                        } else {
                            startActivity(
                                Intent(
                                    this@HomeFragment.requireContext(),
                                    AttendanceActivity::class.java
                                )
                            )
                        }
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                dashboardAttendanceStatusResponseData.observe(viewLifecycleOwner) {
                    if (it.Status == Constant.success) {
                        if (it.InTime == null && it.OutTime == null) {
                            attendanceStatus = getString(R.string.pending)
                            if (isFirstOpenCheckIn) {
                                preferenceUtils.setValue(Constant.isFirstOpenCheckIn, false)
                                preferenceUtils.setValue(
                                    Constant.isFirstOpenCheckInDate,
                                    AppUtils.INSTANCE?.getCurrentDate()
                                )
                                DialogUtils.showMarkAttendanceDialog(
                                    this@HomeFragment.requireContext(),
                                    this@HomeFragment,
                                    getString(R.string.do_you_want_to_check_in),
                                    getString(R.string.check_in),
                                    R.drawable.ic_attendance_finger
                                )
                            }
                        } else if (it.InTime?.isNotEmpty() == true && it.OutTime == null) {
                            attendanceStatus = getString(R.string.pending)
                            if (isFirstOpenCheckOut) {
                                if ((it.CurrentServerTime?.split(":")?.first()
                                        ?.toIntOrNull()
                                        ?: 0) >= 7 && it.CurrentServerTime.toString()
                                        .contains("PM")
                                ) {
                                    preferenceUtils.setValue(Constant.isFirstOpenCheckOut, false)
                                    preferenceUtils.setValue(
                                        Constant.isFirstOpenCheckOutDate,
                                        AppUtils.INSTANCE?.getCurrentDate()
                                    )
                                    DialogUtils.showMarkAttendanceDialog(
                                        this@HomeFragment.requireContext(),
                                        this@HomeFragment,
                                        getString(R.string.do_you_want_check_out),
                                        getString(R.string.check_out),
                                        R.drawable.ic_attendance_check_out
                                    )
                                }
                            }
                        } else {
                            attendanceStatus = getString(R.string.completed)
                        }
                    } else {
                        showToast(it.Message.toString())
                    }
                    setUpHomeDataList()
                }

                attendanceStatusResponseData.observe(viewLifecycleOwner) {
//                toggleLoader(false)
                    if (it.Status != Constant.SUCCESS) {
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(viewLifecycleOwner) {
//                toggleLoader(false)
                    showToast(it.toString())
                }


                with(homeDashboardViewModel) {
                    customerIdCardViewModel.customerIdCardResponseData.observe(this@HomeFragment.viewLifecycleOwner) {
                        toggleLoader(false)

                        if (it.status?.lowercase() == Constant.success) {
                            val data = it.lstBanners
                            if (data.isNullOrEmpty()) {
                                showToast(it.Message.toString())
                            } else {

                                if (data[0].Hyperlink.isNullOrEmpty()) {
                                    showToast(it.Message.toString())
                                } else {


                                    /*       val customerIDCardPdf = it.PdfString
                                               ?.replace("data:application/pdf;base64,", "")

                                           val file = customerIDCardPdf?.let { it1 ->
                                               ImageUtils.INSTANCE?.writePDFToFile(
                                                   it1,
                                                   "Customer Id Card"
                                               )
                                           }
                                           ImageUtils.INSTANCE?.openPdfFile(
                                               requireContext(),
                                               file?.absolutePath.toString()
                                           )*/
                                    val idLink = data[0].Hyperlink + "?innovid=" + innovId
                                    val i = Intent(requireContext(), com.example.digitracksdk.presentation.web_view.WebViewActivity::class.java)
                                    val b = Bundle()
                                    b.putString(Constant.WEB_URL, idLink)
                                    i.putExtras(b)
                                    startActivity(i)
                                }
                            }
                        } else {
                            showToast(it.Message.toString())
                        }
                    }
                    messageData.observe(this@HomeFragment.viewLifecycleOwner) {
                        toggleLoader(false)
                        showToast(it)
                    }
                }
                with(clientPoliciesViewModel)
                {
                    policyAcknowledgeResponseData.observe(this@HomeFragment.viewLifecycleOwner)
                    {
                        toggleLoader(false)

                        if (it.status?.lowercase() == Constant.success) {
                            if (it.PolicyAcknowledgeStatus == 0) {
                                openPolicyAcknowledgeDialog(it.Message.toString())
                            }
                        } else {
                            showToast(it.Message.toString())
                        }

                    }
                    messageData.observe(this@HomeFragment.viewLifecycleOwner)
                    {
                        toggleLoader(false)
                        showToast(it)
                    }
                }


            }
        }
    }

    private fun openPolicyAcknowledgeDialog(message: String) {
        DialogUtils.showPolicyAcknowledgeDialog(this.requireContext(), message, this)
    }

    override fun onMarkAttendanceClick() {
        DialogUtils.closeAttendanceMarkDialog()
        startActivity(Intent(this.requireContext(), AttendanceActivity::class.java))
    }


    private fun callCheckBirthdayApi() {
        if (context?.isNetworkAvailable() == true) {
            homeDashboardViewModel.callCheckBirthdayApi(
                CommonRequestModel(
                    InnovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun openBirthdayDialog(msg: String, backgroundImage: String) {
        val fName = preferenceUtils.getValue(Constant.PreferenceKeys.FIRST_NAME)
        val mName = preferenceUtils.getValue(Constant.PreferenceKeys.MiddleName)
        val lName = preferenceUtils.getValue(Constant.PreferenceKeys.LAST_NAME)
        val fullName = "$fName $mName $lName"
        val background = ImageUtils.INSTANCE?.stringToBitMap(backgroundImage)
        val profile =
            ImageUtils.INSTANCE?.stringToBitMap(preferenceUtils.getValue(Constant.PreferenceKeys.PROFILE_PIC))
        DialogUtils.showBirthdayDialog(this.requireContext(), fullName, msg, profile, background)
        preferenceUtils.setValue(Constant.PreferenceKeys.CloseBirthdayBanner, true)
    }

    private fun getPreferenceData() {
        preferenceUtils.apply {
            innovId = getValue(Constant.PreferenceKeys.INNOV_ID)
            gnetAssociateId = getValue(Constant.PreferenceKeys.GnetAssociateID)
            isLeave = getValue(Constant.PreferenceKeys.IsLeave, false)
            isReimbursement = getValue(Constant.PreferenceKeys.IsDigiTracReimbursement, false)
            isResignation = getValue(Constant.PreferenceKeys.IsDigiTracResignation, false)
            isRewards = getValue(Constant.PreferenceKeys.IsRewards, false)
            isNewResignation = getValue(Constant.PreferenceKeys.NewResignation, false)
            isTraining = getValue(Constant.PreferenceKeys.IsTraining, false)
            isClientPolicy = getValue(Constant.PreferenceKeys.IsClientPolicies, false)
            isInnovIdCard = getValue(Constant.PreferenceKeys.IsInnovIdCard, false)
            isCustomerIdCard = getValue(Constant.PreferenceKeys.IsCustomeridCard, false)
            isAttedanceFromAnywhere =
                getValue(Constant.PreferenceKeys.AttendanceFromAnyWhere, false)
            isGpsAttendance = getValue(Constant.PreferenceKeys.IsGPSAttendance, false)
            isAssociateId = getValue(Constant.PreferenceKeys.IsAssociate, false)
            isLetters = getValue(Constant.PreferenceKeys.Letters) == "Yes"
            isFirstOpenCheckIn = getValue(Constant.isFirstOpenCheckIn, true)
            isFirstOpenCheckOut = getValue(Constant.isFirstOpenCheckOut, true)
            isViewPayouts = getValue(Constant.PreferenceKeys.IsViewPayouts, true)
            isNewReimbursement = getValue(Constant.PreferenceKeys.IsNewReimbursement, true)
            isIncomeTaxDeclaration = getValue(Constant.PreferenceKeys.IsIncomeTaxDeclaration, true)
            isGeoTracking = getValue(Constant.PreferenceKeys.IsGeoTracking, true)
            isPendingReimbursement = getValue(Constant.PreferenceKeys.IsPendingReimbursement, true)
            isPendingReimbursementNew =
                getValue(Constant.PreferenceKeys.IsPendingReimbursementNew, true)
            isPaySlip = getValue(Constant.PreferenceKeys.IsPaySlip, true)
            isRefyneApplicable = getValue(Constant.PreferenceKeys.IsRefyneApplicable, true)
            isAttendanceCalendar = getValue(Constant.PreferenceKeys.AttendanceCalendar, true)
            isQuickAttendance = getValue(Constant.PreferenceKeys.QuickAttendance ,true)
            if (!isFirstOpenCheckIn) {
                val lastCheckInDate = preferenceUtils.getValue(Constant.isFirstOpenCheckInDate)
                preferenceUtils.getValue(Constant.isFirstOpenCheckOutDate)
                val currentDate = AppUtils.INSTANCE?.getCurrentDate()

                if (lastCheckInDate.isNotEmpty()) {
                    if (currentDate != lastCheckInDate) {
                        isFirstOpenCheckIn = true
                        isFirstOpenCheckOut = true
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getPreferenceData()
        if (isAttedanceFromAnywhere || isGpsAttendance) {
            callCheckAttendanceStatusApi()
        }
    }

    private fun setUpView() {
//        if (isAttedanceFromAnywhere || isGpsAttendance) {
//            callCheckAttendanceStatusApi()
//        }

        if (profilePercentage != 100) {
            DialogUtils.closeCompleteYourProfileDialog()
            DialogUtils.showCompleteYourProfileDialog(this.requireContext(), this)
        }

        val dob = preferenceUtils.getValue(Constant.PreferenceKeys.DOB)
        val birthDay = dob.subSequence(0, 6).toString().replace("-", " ")
        if (today.subSequence(0, 6) == birthDay) {
            callCheckBirthdayApi()
        }
        setUpHomeDataList()
        if (isClientPolicy) {
            callPolicyAcknowledgementApi()
        }
    }

    private fun callPolicyAcknowledgementApi() {
        if (context?.isNetworkAvailable() == true) {
            clientPoliciesViewModel.callPolicyAcknowledgeApi(
                PolicyAcknowledgeRequestModel(
                    InnovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID),
                    GnetAssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun callCheckAttendanceStatusApi() {
        binding.apply {
            if (context?.isNetworkAvailable() == true) {
                attendanceViewModel.callDashboardAttendanceStatusApi(
                    request = CommonRequestModel(
                        InnovId = innovId
                    )
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    override fun onContinueClick() {
        super.onContinueClick()
        DialogUtils.closeCompleteYourProfileDialog()
        startActivity(Intent(this.requireContext(), OnboardingActivity::class.java))
    }

    private fun callHomeBannerApi() {
        if (this.context?.isNetworkAvailable() == true) {
            homeBannerViewModel.callHomeBannerApi(getHomeBannerRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun getHomeBannerRequestModel(): HomeBannerRequestModel {
        val requestModel = HomeBannerRequestModel()
        requestModel.EmployeeId = "0"
        requestModel.InnovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
        requestModel.Source = "DT"
        return requestModel
    }

    private fun setUpListener() {
        binding.apply {
            fabHome.setOnClickListener {
                AppUtils.INSTANCE?.sendEventsToFirebase("Click on Chat_Button", requireContext())
                val urlString =
                    "https://chatbot.firstmeridian.com/kya_backend/share?code=mobile_app_13186&state=1&notes=$gnetAssociateId"
                val intent = Intent(requireContext(), BannerViewActivity::class.java)
                intent.putExtra("bannerLink", urlString)
                startActivity(intent)
            }
            homeBottomItems.imgIcon1.setOnClickListener {
                if (PermissionUtils.getCallPermission(this@HomeFragment.requireActivity())) {
                    preferenceUtils.setValue(Constant.AskedPermission.CALL_PERMISSION_COUNT, 0)
                    callInnov()
                } else {
                    val callPermission = arrayOf(
                        Manifest.permission.CALL_PHONE
                    )
                    callPermissionResult.launch(callPermission)
                }
            }
            homeBottomItems.imgBanner.setOnClickListener {
                openInnovWeb()
            }
        }
    }

    private var callPermissionResult: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            var allAreGranted = true
            for (b in result.values) {
                allAreGranted = allAreGranted && b
            }
            if (allAreGranted) {
                callInnov()
            } else {
                val callPermission =
                    preferenceUtils.getIntValue(Constant.AskedPermission.CALL_PERMISSION_COUNT, 0)
                preferenceUtils.setValue(
                    Constant.AskedPermission.CALL_PERMISSION_COUNT,
                    callPermission.plus(1)
                )
                if (preferenceUtils.getIntValue(
                        Constant.AskedPermission.CALL_PERMISSION_COUNT,
                        0
                    ) >= 2
                ) {
                    DialogUtils.showPermissionDialog(
                        this.requireActivity(),
                        getString(R.string.please_grant_the_phone_call_permission_to_continue),
                        getString(R.string.allow_permission),
                        getString(R.string.go_to_settings),
                        getString(R.string.deny),
                        false
                    )
                } else {
                    showToast(getString(R.string.please_grant_the_phone_call_permission_to_continue))
                }
            }
        }

    private fun callInnov() {
        val callIntent = Intent(Intent.ACTION_CALL)
        val intent = callIntent.setData(Uri.parse("tel:1800224456"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        requireContext().startActivity(callIntent)
    }

    private fun emailInnov() {
        val mailto = "mailto:appsupport@innov.in"

        /*+
                "&subject=" + Uri.encode("Subject") +
                "&body=" + Uri.encode("");*/
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse(mailto)
        startActivity(emailIntent)
    }

    private fun openInnovWeb() {
        val i = Intent(this@HomeFragment.requireContext(), WebViewActivity::class.java)
        val b = Bundle()
        b.putString(Constant.WEB_URL, Constant.INNOV_URL)
        i.putExtras(b)
        this@HomeFragment.requireActivity().startActivity(i)
    }


    private fun setUpHomeDataList() {
        homeItemsWithoutCountsList.clear()
        homeItemList.clear()
        homeItemsWithoutCountsList.add(
            HomeDashboardMenu(
                title = getString(R.string.onboarding),
                icon1 = R.drawable.menu_onboarding
            )
        )
        if (isLetters) {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.my_letters),
                    icon1 = R.drawable.menu_my_letters
                )
            )
        }

        if (isAttedanceFromAnywhere || isGpsAttendance) {

            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.time_sheet),
                    icon1 = R.drawable.menu_time_sheet
                )
            )

            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.mileage_tracking),
                    icon1 = R.drawable.menu_mileage_tracking
                )
            )

        }
        if (isAttedanceFromAnywhere || isGpsAttendance) {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.view_attendance),
                    icon1 = R.drawable.menu_view_attendance
                )
            )

            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.view_attendance_new),
                    icon1 = R.drawable.menu_view_attendance
                )
            )

        }
        if (isQuickAttendance) {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.attendance),
                    icon1 = R.drawable.menu_attendacne_regularization
                )
            )
        }
        if (isAttendanceCalendar) {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.attendance_calendar),
                    icon1 = R.drawable.menu_attendance_calendar
                )
            )

            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.view_calendar),
                    icon1 = R.drawable.menu_view_calendar
                )
            )

        }

        if (isPaySlip) {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.payslip),
                    icon1 = R.drawable.menu_payslip
                )
            )
        }
        //pf/esic
        if (isAssociateId) {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.pf_esic_insurance),
                    icon1 = R.drawable.menu_pf
                )
            )
        }
        if (isRefyneApplicable) {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.instance_salary),
                    icon1 = R.drawable.menu_new_refyne
                )
            )
        }
        if (isReimbursement) {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.reimbursements),
                    icon1 = R.drawable.menu_reimbursement
                )
            )
        }
        if (isNewReimbursement) {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.reimbursement1),
                    icon1 = R.drawable.menu_reimbursement
                )
            )
        }
        if (isPendingReimbursement) {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.pending_reimbursement),
                    icon1 = R.drawable.menu_pending_reimbursement
                )
            )
        }
        if (isLeave) {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.leaves),
                    icon1 = R.drawable.menu_leave
                )
            )
        }
   /*     homeItemsWithoutCountsList.add(
            HomeDashboardMenu(
                title = getString(R.string.digi_assist),
                icon1 = R.drawable.menu_digi_assist
            )

        )*/

        homeItemsWithoutCountsList.add(
            HomeDashboardMenu(
                title = getString(R.string.smart_bots),
                icon1 = R.drawable.menu_digi_assist
            )

        )

        if (isTraining) {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.training),
                    icon1 = R.drawable.menu_training
                )
            )
        }
        if (isClientPolicy) {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.client_policy),
                    icon1 = R.drawable.menu_client_policy
                )
            )
        }
        homeItemsWithoutCountsList.add(
            HomeDashboardMenu(
                title = getString(R.string.help_and_support),
                icon1 = R.drawable.menu_help
            )
        )

        homeItemsWithoutCountsList.add(
            HomeDashboardMenu(
                title = getString(R.string.refer_jobs_to_friend),
                icon1 = R.drawable.menu_jobs_referral
            )
        )

        if (isInnovIdCard) {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.innov_id_card),
                    icon1 = R.drawable.menu_innov_id_card
                )
            )
        }
        if (isCustomerIdCard) {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.customer_id_card),
                    icon1 = R.drawable.menu_customer_card
                )
            )
        }
        if (isResignation) {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.resignation),
                    icon1 = R.drawable.menu_resignation
                )
            )
        }
        if (isNewResignation) {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.resignation_new),
                    icon1 = R.drawable.menu_resignation
                )
            )
        }
        if (isIncomeTaxDeclaration) {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.investment_declaration),
                    icon1 = R.drawable.menu_investment_declaration
                )
            )
        }

        if (isViewPayouts) {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.view_payout),
                    icon1 = R.drawable.menu_view_payout
                )
            )
        }

     if (isGeoTracking) {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.geo_tracking),
                    icon1 = R.drawable.menu_geotracking
                )
            )
        }
        if (isGeoTracking) {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.geo_tracking1),
                    icon1 = R.drawable.menu_geotracking
                )
            )
        }

        if (isRewards)
        {
            homeItemsWithoutCountsList.add(
                HomeDashboardMenu(
                    title = getString(R.string.rewards),
                    icon1 = R.drawable.menu_rewards
                )
            )
        }
        homeItemsWithoutCountsList.add(
            HomeDashboardMenu(
                title = getString(R.string.exit_questionnaire),
                icon1 = R.drawable.menu_exit_questionnaire
            )
        )
//        homeItemList.add(
//            HomeModel(
//                homeItemType = HomeAdapter.HomeItemType.HOME_ITEM_TITLE,
//                title = "Good morning,",
//                subTitle = preferenceUtils.getValue("name")
//            )
//        )
        if (profilePercentage < 100) {
            homeItemList.add(
                HomeModel(
                    homeItemType = HomeAdapter.HomeItemType.HOME_ITEM_PROFILE_STATUS,
                    progress = profilePercentage, btnText = getString(R.string.complete_now)
                )
            )
        }

        homeItemList.add(
            HomeModel(
                homeItemType = HomeAdapter.HomeItemType.HOME_BANNER_ITEM,
                bannerList = homeBannerList
            )
        )


        if (isAttedanceFromAnywhere || isGpsAttendance)
        {
            homeItemList.add(
                HomeModel(
                    homeItemType = HomeAdapter.HomeItemType.HOME_ITEM_ATTENDANCE,
                    title = getString(R.string.attendance),
                    subTitle = today,
                    btnText = attendanceStatus
                )
            )
        }
        val homeDashboardMenuList: ArrayList<HomeModel> = ArrayList()
        homeDashboardMenuList.add(
            HomeModel(
                homeItemType = HomeAdapter.HomeItemType.HOME_ITEM_WITHOUT_COUNTS,
                homeDashboardMenu = homeItemsWithoutCountsList
            )
        )
        homeItemList.addAll(homeDashboardMenuList)
//        homeItemList.add(
//            HomeModel(
//                homeItemType = HomeAdapter.HomeItemType.HOME_ITEM_BOTTOM,
//                icon1 = R.drawable.ic_calling,
//                icon2 = R.drawable.ic_message_text,
//                icon3 = R.drawable.ic_headphone,
//                bannerImage = R.drawable.banner_innov
//            )
//        )
        homeAdapter.notifyDataSetChanged()
    }

    private fun setUpAdapter() {
        homeAdapter = HomeAdapter(this.requireContext(), homeItemList, this)
        binding.recyclerHome.adapter = homeAdapter
    }

    override fun onPolicyAcknowledgeClick() {
        DialogUtils.closePolicyAcknowledgeDialog()
        startActivity(Intent(this.requireContext(), ClientPolicyActivity::class.java))
    }

    override fun clickOnItem(position: Int, itemName: String) {
        when (itemName) {
            getString(R.string.onboarding) -> {
                startActivity(Intent(this.requireContext(), OnboardingActivity::class.java))
            }

            getString(R.string.leaves) -> {
                startActivity(Intent(this.requireContext(), LeavesActivity::class.java))
            }

            getString(R.string.help_and_support) -> {
                startActivity(Intent(this.requireContext(), HelpAndSupportActivity::class.java))
            }

            getString(R.string.training) -> {
                startActivity(Intent(this.requireContext(), TrainingActivity::class.java))
            }

            getString(R.string.attendance_regularization) -> {
                startActivity(
                    Intent(
                        this.requireContext(),
                        AttendanceRegularizationActivity::class.java
                    )
                )
            }

            getString(R.string.mileage_tracking) -> {
                startActivity(Intent(this.requireContext(), MileageTrackingActivity::class.java))
            }

            getString(R.string.reimbursements) -> {
                startActivity(Intent(this.requireContext(), ReimbursementActivity::class.java))
            }

            getString(R.string.resignation) -> {
                startActivity(Intent(this.requireContext(), ResignationActivity::class.java))
            }

            getString(R.string.resignation_new) -> {

                startActivity(Intent(this.requireContext(), ResignationNewActivity::class.java))
            }

            getString(R.string.pending_reimbursement) -> {
                startActivity(
                    Intent(
                        this.requireContext(),
                        PendingReimbursementActivity::class.java
                    )
                )
            }

            getString(R.string.payslip) -> {
                startActivity(Intent(this.requireContext(), PayslipActivity::class.java))
            }

            getString(R.string.pf_esic_insurance) -> {
                startActivity(Intent(this.requireContext(), PfEsicInsuranceActivity::class.java))
            }

            getString(R.string.my_letters) -> {
                startActivity(Intent(this.requireContext(), MyLettersActivity::class.java))
            }

            getString(R.string.client_policy) -> {
                startActivity(Intent(this.requireContext(), ClientPolicyActivity::class.java))
            }

            getString(R.string.innov_id_card) -> {
                startActivity(Intent(this.requireContext(), InnovIdCardActivity::class.java))
            }

            getString(R.string.refer_jobs_to_friend) -> {
                startActivity(Intent(this.requireContext(), JobsAndReferFdsActivity::class.java))
            }

            getString(R.string.time_sheet) -> {
                startActivity(
                    Intent(
                        this.requireContext(),
                        AttendanceTimeSheetActivity::class.java
                    )
                )
            }

            getString(R.string.view_attendance) -> {
                startActivity(Intent(this.requireContext(), ViewAttendanceActivity::class.java))
            }

            getString(R.string.view_attendance_new) -> {
                startActivity(Intent(this.requireContext(), ViewAttendanceNewActivity::class.java))
            }

            getString(R.string.attendance) -> {
                val bundle = Bundle()
                bundle.putString("from", getString(R.string.attendance))
                val intent = Intent(this.requireContext(), AttendanceActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            getString(R.string.view_payout) -> {
                startActivity(Intent(this.requireContext(), ViewPayoutActivity::class.java))
            }

            getString(R.string.instance_salary) -> {
                callRefineUrlApi()
            }

            getString(R.string.investment_declaration) -> {
                callIncomeTaxDeclarationApi()
            }

            getString(R.string.digi_assist) -> {
                callDigiAssistUrl()
            }


            getString(R.string.smart_bots) -> {
                AppUtils.INSTANCE?.sendEventsToFirebase("Click on Smart_Button", requireContext())
                val urlString =
                    "https://chatbot.firstmeridian.com/kya_backend/share?code=mobile_app_13186&state=1&notes=$gnetAssociateId"
                val intent = Intent(requireContext(), BannerViewActivity::class.java)
                intent.putExtra("bannerLink", urlString)
                startActivity(intent)

            }

            getString(R.string.geo_tracking) -> {
                startActivity(Intent(this.requireContext(), GeoTrackingSummaryActivity::class.java))
            }
            getString(R.string.geo_tracking1)->{
                startActivity(Intent(this.requireContext(), GeoTrackingListingActivity::class.java))
            }

            getString(R.string.reimbursement1) -> {
                startActivity(Intent(this.requireContext(), Reimbursement1Activity::class.java))
            }

            getString(R.string.attendance_calendar) -> {
                startActivity(Intent(this.requireContext(), NewTimeSheetActivity::class.java))
            }

            getString(R.string.view_calendar) -> {
                startActivity(Intent(this.requireContext(), NewTimeSheetViewActivity::class.java))
            }

            getString(R.string.customer_id_card) -> {
                callCustomerIdApi()
            }

            getString(R.string.rewards) -> {
                startActivity(Intent(this.requireContext(), RewardsActivity::class.java))
            }

            getString(R.string.exit_questionnaire) -> {
                startActivity(Intent(this.requireContext(), ExitQuestionnaireActivity::class.java))

            }
        }

    }

    private fun callDigiAssistUrl() {

        val i = Intent(requireContext(), WebViewActivity::class.java)
        val b = Bundle()
        b.putString(Constant.WEB_URL, DIGI_ASSIST)
        i.putExtras(b)
        startActivity(i)

    }

    private fun callCustomerIdApi() {
        binding.apply {

            if (requireActivity().isNetworkAvailable()) {
                toggleLoader(true)
                customerIdCardViewModel.callCustomerIdCardApi(
                    request =
                    CustomerIdCardRequestModel(
                        InnovID = innovId,
                        EmployeeId = preferenceUtils.getValue(Constant.PreferenceKeys.EMPLOYEE_ID),
                        Source = "DT"
                    )

                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }

        }
    }

    private fun callIncomeTaxDeclarationApi() {
        binding.apply {
            if (requireActivity().isNetworkAvailable()) {
                toggleLoader(true)
                incomeTaxViewModel.callIncomeTaxDeclarationApi(
                    request =
                    IncomeTaxDeclarationRequestModel(InnovID = innovId)
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    private fun callRefineUrlApi() {
        if (requireActivity().isNetworkAvailable()) {
            toggleLoader(true)
            refineViewModel.callRefineUrlApi(
                request = RefineRequest(
                    preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun clickOnAttendance(position: Int) {
        callAttendanceStatusApi()
    }

    private fun callAttendanceStatusApi() {
        if (this.context?.isNetworkAvailable() == true) {
//            toggleLoader(true)
            attendanceViewModel.callAttendanceStatusApi(
                request = GnetIdRequestModel(
                    GNETAssociateId = preferenceUtils.getValue(
                        Constant.PreferenceKeys.GnetAssociateID
                    )
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun onOkClick() {
        callLogOutAttendanceApi(attendanceDate)
    }

    private fun callLogOutAttendanceApi(attendanceDate: String) {
        if (this.context?.isNetworkAvailable() == true) {
//            toggleLoader(true)
            attendanceViewModel.callUpdateAttendanceStatusApi(
                request =
                UpdateAttendanceStatusRequestModel(
                    AttendanceDate = attendanceDate,
                    GNETAssociateID = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun clickOnCompleteProfile(position: Int) {
        startActivity(Intent(this.requireContext(), OnboardingActivity::class.java))
    }

}