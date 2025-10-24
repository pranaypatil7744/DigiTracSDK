package com.example.digitracksdk.presentation.home.help_and_support

import android.os.Bundle
import android.text.TextUtils
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.domain.model.help_and_support.AssociateIssueRequestModel
import com.example.digitracksdk.domain.model.help_and_support.HelpAndSupportListRequestModel
import com.example.digitracksdk.domain.model.help_and_support.IssueCategoryRequestModel
import com.example.digitracksdk.domain.model.help_and_support.IssueCategoryResponseModel
import com.example.digitracksdk.domain.model.help_and_support.IssueDetailsRequestModel
import com.example.digitracksdk.domain.model.help_and_support.IssueSubCategoryRequestModel
import com.example.digitracksdk.domain.model.help_and_support.IssueSubCategoryResponseModel
import com.example.digitracksdk.domain.model.help_and_support.ListAssociateIssueDetails
import com.example.digitracksdk.domain.model.help_and_support.ListAssociateIssueUpdatesModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityHelpAndSupportBinding
import com.example.digitracksdk.databinding.BottomSheetHelpAndSupportBinding
import com.example.digitracksdk.databinding.BottomSheetIssueDetailsBinding
import com.example.digitracksdk.presentation.home.help_and_support.adapter.HelpAndSupportAdapter
import com.example.digitracksdk.presentation.home.help_and_support.model.HelpAndSupportModel
import com.example.digitracksdk.presentation.home.help_and_support.model.HelpSupportDetails
import com.example.digitracksdk.presentation.home.help_and_support.model.HelpSupportItemType
import com.example.digitracksdk.presentation.home.help_and_support.model.HelpSupportStatus
import com.example.digitracksdk.presentation.home.help_and_support.model.IssueDetailsModel
import com.example.digitracksdk.presentation.home.help_and_support.model.IssueDetailsType
import com.example.digitracksdk.presentation.home.help_and_support.adapter.IssueDetailsAdapter
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.collections.ArrayList

class HelpAndSupportActivity : BaseActivity(), HelpAndSupportAdapter.HelpClickManager {
    lateinit var binding: ActivityHelpAndSupportBinding
    private lateinit var bottomSheetHelpAndSupportBinding: BottomSheetHelpAndSupportBinding
    lateinit var bottomSheetIssueDetailsBinding: BottomSheetIssueDetailsBinding
    private var helpSupportList: ArrayList<HelpAndSupportModel> = ArrayList()
    private var issueCategoryList: ArrayList<IssueCategoryResponseModel> = ArrayList()
    private var issueSubCategoryList: ArrayList<IssueSubCategoryResponseModel> = ArrayList()
    private lateinit var helpAndSupportAdapter: HelpAndSupportAdapter
    private lateinit var issueDetailsAdapter: IssueDetailsAdapter
    lateinit var preferenceUtils: PreferenceUtils
    private lateinit var bottomSheetNewLeave: BottomSheetDialog
    private lateinit var bottomSheetIssueDetailsDialog: BottomSheetDialog
    private val issueDetailsList: ArrayList<IssueDetailsModel> = ArrayList()
    var innovId: String = ""
    var selectedIssueCategoryId: Int = 0
    var selectedIssueSubCategoryId: Int = 0
    var selectedIssueItemPosition: Int = 0
    private val helpAndSupportViewModel: HelpAndSupportViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHelpAndSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        getPreferenceData()
        setUpToolbar()
        setObserver()
        setUpHelpSupportAdapter()
        callHelpAndSupportListApi()
        setUpListener()
    }

    private fun setObserver() {
        binding.apply {
            with(helpAndSupportViewModel) {
                helpAndSupportListResponseData.observe(this@HelpAndSupportActivity) {
                    toggleLoader(false)
                    if (it.status == Constant.success) {
                        if (!it.lstAssociateIssueDetails.isNullOrEmpty()) {
                            showNoDataLayout(false)
                            it.lstAssociateIssueDetails?.let { it1 ->
                                setHelpAndSupportResponseData(
                                    it1
                                )
                            }
                        } else {
                            showNoDataLayout(true)
                            binding.layoutNoData.apply {
                                tvNoData.text = it.Message
                            }
                        }
                    } else {
                        showNoDataLayout(true)
                        showToast(it.Message.toString())
                    }
                }

                issueDetailsResponseData.observe(
                    this@HelpAndSupportActivity
                ) { t -> setUpIssueDetailsData(t?.lstAssociateIssueUpdates) }

                associateIssueResponseData.observe(this@HelpAndSupportActivity) {
                    if (it.status == Constant.SUCCESS) {
                        showToast(it.Message.toString())
                        bottomSheetNewLeave.dismiss()
                        clearData()
                        callHelpAndSupportListApi()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                issueCategoryResponseData.observe(this@HelpAndSupportActivity) {
                    if (it.size != 0) {
                        issueCategoryList.clear()
                        issueCategoryList.addAll(it)
                        val issueList: ArrayList<String> = ArrayList()
                        issueList.clear()
                        for (i in it) {
                            issueList.add(i.IssueCategoryName.toString())
                        }
                        val adapter = ArrayAdapter(
                            this@HelpAndSupportActivity,
                            android.R.layout.simple_list_item_1, issueList
                        )
                        bottomSheetHelpAndSupportBinding.etIssueCategory.setAdapter(adapter)
                    } else {
                        showToast(getString(R.string.something_went_wrong))
                    }
                }

                issueSubCategoryResponseData.observe(this@HelpAndSupportActivity) {
                    if (it.size != 0) {
                        issueSubCategoryList.clear()
                        issueSubCategoryList.addAll(it)
                        val issueList: ArrayList<String> = ArrayList()
                        issueList.clear()
                        for (i in it) {
                            issueList.add(i.IssueSubCategoryName.toString())
                        }
                        val adapter = ArrayAdapter(
                            this@HelpAndSupportActivity,
                            android.R.layout.simple_list_item_1, issueList
                        )
                        bottomSheetHelpAndSupportBinding.etSubIssueCategory.setAdapter(adapter)
                    } else {
                        showToast(getString(R.string.something_went_wrong))
                    }
                }

                issueSubCategoryMessageData.observe(this@HelpAndSupportActivity) {
                    showToast(it)
                }

                issueCategoryMessageData.observe(this@HelpAndSupportActivity) {
                    showToast(it)
                }

                associateIssueMessageData.observe(this@HelpAndSupportActivity) {
                    showToast(it)
                }

                helpAndSupportMessageData.observe(this@HelpAndSupportActivity) {
                    toggleLoader(false)
                    showToast(it)
                }
            }
        }
    }

    private fun clearData() {
        bottomSheetHelpAndSupportBinding.apply {
            etIssueCategory.setText(getString(R.string.Select))
            etSubIssueCategory.setText(getString(R.string.Select))
        }
    }

    private fun getPreferenceData() {
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
    }

    private fun getHelpAndSupportListRequestApi(): HelpAndSupportListRequestModel {
        val request = HelpAndSupportListRequestModel()
        request.InnovID = innovId
        return request
    }

    private fun callHelpAndSupportListApi() {
        with(binding) {
            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                toggleLoader(true)
                helpAndSupportViewModel.callHelpAndSupportApi(getHelpAndSupportListRequestApi())
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerHelpAndSupport.visibility = GONE
            }
            layoutNoData.root.visibility = GONE
        }
    }

    private fun setHelpAndSupportResponseData(data: ArrayList<ListAssociateIssueDetails>) {
        helpSupportList.clear()
        for (i in data) {
            //TODO update api according to UI
            helpSupportList.add(
                HelpAndSupportModel(
                    helpSupportItemType = HelpSupportItemType.MAIN_ITEM,
                    helpSupportDetails = HelpSupportDetails(
                        issueTitle = i.IssueSubCategoryName,
                        issueType = i.IssueCategoryName,
                        issueDate = i.CreatedOn,
                        associateQueryId = i.AssociateQueryId,
                        resolvedDate = i.LastUpdatedOn,
                        issueDetails = i.IssueDetails,
                        helpSupportStatus = if (i.CurrentIssueStatus == Constant.Closed) HelpSupportStatus.CLOSE else HelpSupportStatus.OPEN
                    )
                )
            )
        }
        helpAndSupportAdapter.notifyDataSetChanged()
    }

    private fun setUpHelpSupportAdapter() {
        helpAndSupportAdapter = HelpAndSupportAdapter(this, helpSupportList, this)
        binding.recyclerHelpAndSupport.adapter = helpAndSupportAdapter
    }

    private fun setUpListener() {
        binding.apply {
            fabHelp.setOnClickListener {
                openNewLeaveRequestBottomSheet()
            }
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callHelpAndSupportListApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                callHelpAndSupportListApi()
            }
        }
    }

    private fun openIssueDetailsBottomSheet() {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_issue_details, null)
        bottomSheetIssueDetailsBinding = BottomSheetIssueDetailsBinding.bind(view)
        bottomSheetIssueDetailsDialog = BottomSheetDialog(this)
        bottomSheetIssueDetailsBinding.apply {
            btnClose.setOnClickListener {
                bottomSheetIssueDetailsDialog.dismiss()
            }
            issueDetailsAdapter = IssueDetailsAdapter(this@HelpAndSupportActivity, issueDetailsList)
            recyclerIssueDetails.adapter = issueDetailsAdapter
            tvIssueDetails.text = getString(R.string.details_of_view_service_request)
        }
        callIssueDetailsApi()
        bottomSheetIssueDetailsDialog.apply {
            setContentView(view)
            setCancelable(false)
            show()
        }
    }

    private fun getIssueDetailsRequestModel(): IssueDetailsRequestModel {
        val request = IssueDetailsRequestModel()
        request.AssociateQueryId =
            helpSupportList[selectedIssueItemPosition].helpSupportDetails?.associateQueryId.toString()
        return request
    }

    private fun callIssueDetailsApi() {
        if (isNetworkAvailable()) {
            helpAndSupportViewModel.callIssueDetailsApi(getIssueDetailsRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun setUpIssueDetailsData(it: ArrayList<ListAssociateIssueUpdatesModel>?) {
        val data = helpSupportList[selectedIssueItemPosition].helpSupportDetails

        issueDetailsList.clear()
        issueDetailsList.add(
            IssueDetailsModel(
                issueDetailsType = IssueDetailsType.SINGLE_LINE,
                issueStatus = HelpSupportStatus.OPEN,
                title = getString(R.string.ticket_raised_on),
                subTitle = data?.issueDate
            )
        )
        issueDetailsList.add(
            IssueDetailsModel(
                issueDetailsType = IssueDetailsType.TWO_LINE,
                title = getString(R.string.issue_category),
                subTitle = data?.issueType
            )
        )
        issueDetailsList.add(
            IssueDetailsModel(
                issueDetailsType = IssueDetailsType.TWO_LINE,
                title = getString(R.string.issue_sub_category),
                subTitle = data?.issueTitle
            )
        )
        issueDetailsList.add(
            IssueDetailsModel(
                issueDetailsType = IssueDetailsType.TWO_LINE,
                title = getString(R.string.reason),
                subTitle = data?.issueDetails
            )
        )
        issueDetailsList.add(
            IssueDetailsModel(
                issueDetailsType = IssueDetailsType.DIVIDER
            )
        )
        for (i in it ?: arrayListOf()) {
            issueDetailsList.add(
                IssueDetailsModel(
                    issueDetailsType = IssueDetailsType.MULTI_LINE,
                    title = getString(R.string.from),
                    subTitle = i.LastUpdatedBy,
                    title2 = getString(R.string.remark),
                    subTitle2 = i.Remark
                )
            )
            issueDetailsList.add(
                IssueDetailsModel(
                    issueDetailsType = IssueDetailsType.SINGLE_LINE,
                    title = getString(R.string.last_updated_on),
                    subTitle = i.LastUpdatedOn,
                    issueStatus = if (i.QueryStatus == Constant.Closed) HelpSupportStatus.CLOSE else HelpSupportStatus.OPEN
                )
            )
            issueDetailsList.add(
                IssueDetailsModel(
                    issueDetailsType = IssueDetailsType.DIVIDER
                )
            )
        }

        issueDetailsAdapter.notifyDataSetChanged()
    }

    private fun openNewLeaveRequestBottomSheet() {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_help_and_support, null)
        bottomSheetNewLeave = BottomSheetDialog(this)
        bottomSheetHelpAndSupportBinding = BottomSheetHelpAndSupportBinding.bind(view)
        bottomSheetNewLeave.setContentView(view)
        bottomSheetNewLeave.setCancelable(true)
        callIssueCategoryApi()
        callIssueSubCategoryApi()
        bottomSheetHelpAndSupportBinding.apply {
            tvNewLeaveRequest.text = getString(R.string.raise_service_request)
            etIssueCategory.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    selectedIssueCategoryId = issueCategoryList[position].IssueCategoryID ?: 0
                    etSubIssueCategory.setText(getString(R.string.Select))
                    selectedIssueSubCategoryId = 0
                    callIssueSubCategoryApi()
                }
            etSubIssueCategory.setOnItemClickListener { parent, view, position, id ->
                selectedIssueSubCategoryId =
                    issueSubCategoryList[position].IssueSubCategoryId ?: 0
            }
            btnClose.setOnClickListener {
                bottomSheetNewLeave.dismiss()
            }
            btnSubmit.setOnClickListener {
                validateAssociateIssueData()
            }
        }
        bottomSheetNewLeave.show()

    }

    private fun validateAssociateIssueData() {
        clearError()
        bottomSheetHelpAndSupportBinding.apply {
            if (etIssueCategory.text.toString() == getString(R.string.Select)) {
                layoutIssueCategory.error = getString(R.string.please_select_issue_category)
            } else if (etSubIssueCategory.text.toString() == getString(R.string.Select)) {
                layoutSubIssueCategory.error = getString(R.string.please_select_issue_sub_category)
            } else if (TextUtils.isEmpty(etRequestTitle.text.toString().trim())) {
                layoutRequestTitle.error = getString(R.string.please_enter_request_title)
            } else if (TextUtils.isEmpty(etReason.text.toString().trim())) {
                layoutReason.error = getString(R.string.please_enter_reason)
            } else {
                callAssociateIssueApi()
            }
        }
    }

    private fun clearError() {
        bottomSheetHelpAndSupportBinding.apply {
            layoutIssueCategory.error = ""
            layoutSubIssueCategory.error = ""
            layoutRequestTitle.error = ""
            layoutReason.error = ""
        }
    }

    private fun callAssociateIssueApi() {
        if (isNetworkAvailable()) {
            helpAndSupportViewModel.callAssociateIssueApi(getAssociateIssueRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun callIssueCategoryApi() {
        if (isNetworkAvailable()) {
            helpAndSupportViewModel.callIssueCategoryApi(getIssueCategoryRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun callIssueSubCategoryApi() {
        if (isNetworkAvailable()) {
            helpAndSupportViewModel.callIssueSubCategoryApi(getIssueSubCategoryRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun getIssueCategoryRequestModel(): IssueCategoryRequestModel {
        val request = IssueCategoryRequestModel()
        request.InnovID = innovId
        return request
    }

    private fun getIssueSubCategoryRequestModel(): IssueSubCategoryRequestModel {
        val request = IssueSubCategoryRequestModel()
        request.IssueCategoryID = selectedIssueCategoryId
        return request
    }

    private fun getAssociateIssueRequestModel(): AssociateIssueRequestModel {
        val request = AssociateIssueRequestModel()
        request.InnovID = innovId
        request.IssueCategoryId = selectedIssueCategoryId
        request.IssueSubCategoryId = selectedIssueSubCategoryId
        request.IssueHeader = bottomSheetHelpAndSupportBinding.etRequestTitle.text.toString().trim()
        request.IssueDetails = bottomSheetHelpAndSupportBinding.etReason.text.toString().trim()
        request.MappingID = ""
        return request
    }

    private fun setUpToolbar() {
        binding.apply {
            toolbar.btnBack.setOnClickListener {
                finish()
            }
            toolbar.tvTitle.text = getString(R.string.help_and_support)
            toolbar.divider.visibility = VISIBLE
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
                noDataLayout = layoutNoData.root, recyclerView = recyclerHelpAndSupport, show = show
            )
        }
    }

    override fun onItemClick(position: Int) {
        selectedIssueItemPosition = position
        openIssueDetailsBottomSheet()
    }

}