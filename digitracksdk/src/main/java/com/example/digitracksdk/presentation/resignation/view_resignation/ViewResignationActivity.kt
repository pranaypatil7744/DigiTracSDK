package com.example.digitracksdk.presentation.resignation.view_resignation

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityViewResignationBinding
import com.innov.digitrac.databinding.BottomSheetRevokeResignationBinding
import com.example.digitracksdk.domain.model.resignation.ResignationListRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationsListModel
import com.example.digitracksdk.domain.model.resignation.RevokeResignationRequestModel
import com.example.digitracksdk.presentation.resignation.add_resignation.AddResignationViewModel
import com.example.digitracksdk.presentation.resignation.view_resignation.adapter.ViewResignationAdapter
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class ViewResignationActivity : BaseActivity(), ViewResignationAdapter.RevokeClickManager {
    lateinit var binding: ActivityViewResignationBinding
    lateinit var preferenceUtils: PreferenceUtils
    private val addResignationViewModel: AddResignationViewModel by viewModel()
    lateinit var viewResignationAdapter: ViewResignationAdapter
    private val resignationViewList: ArrayList<ResignationsListModel> = ArrayList()
    var gnetAssociteId: String = ""
    lateinit var bottomSheetRevokeResignationDialog: BottomSheetDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityViewResignationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        setObserver()
        setUpToolbar()
        setUpAdapter()
        setUpListener()
        getPreferenceData()
        callResignationListApi()

    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callResignationListApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                callResignationListApi()
            }
        }
    }

    private fun setUpAdapter() {
        viewResignationAdapter =
            ViewResignationAdapter(this@ViewResignationActivity, resignationViewList, this)
        binding.recyclerResignation.adapter = viewResignationAdapter
    }

    private fun getPreferenceData() {
        binding.apply {
            gnetAssociteId = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
        }
    }

    private fun setObserver() {
        binding.apply {
            with(addResignationViewModel) {
                resignationListResponseData.observe(this@ViewResignationActivity) {
                    toggleLoader(false)
                    if (it.Status == com.example.digitracksdk.Constant.success) {
                        if (!it.lstResignationDetails.isNullOrEmpty() && it.lstResignationDetails?.size != 0 ) {
                            layoutNoData.root.visibility = GONE
                            resignationViewList.clear()
                            resignationViewList.addAll(it.lstResignationDetails ?: arrayListOf())
                            viewResignationAdapter.notifyDataSetChanged()
                        } else {
                            layoutNoData.root.visibility = VISIBLE
                        }
                    } else {
                        showToast(it.Message.toString())
                    }
                }
                revokeResignationResponseData.observe(this@ViewResignationActivity) {
                    toggleLoader(false)
                    showToast(it.Message.toString())
                    resignationViewList.clear()
                    viewResignationAdapter.notifyDataSetChanged()
                    callResignationListApi()
                }

                showProgressbar.observe(this@ViewResignationActivity) {
                    toggleLoader(it)
                }
                messageData.observe(this@ViewResignationActivity) {
                    showToast(it)
                }
            }
        }
    }

    private fun callResignationListApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                layoutNoInternet.root.visibility = GONE
                recyclerResignation.visibility = VISIBLE
                addResignationViewModel.callResignationListApi(
                    request = ResignationListRequestModel(
                        gnetAssociteId
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerResignation.visibility = GONE
            }
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.view_resignation)
            divider.visibility = VISIBLE
            btnBack.setOnClickListener {
                finish()
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

    private fun showNoDataLayout(show: Boolean) {
        binding.apply {
            noDataLayout(
                noDataLayout = layoutNoData.root, recyclerView = recyclerResignation, show = show
            )
        }
    }

    override fun onClickRevokeBtn(position: Int) {
        openRevokeResignationReasonBottomSheet(position)
    }

    private fun openRevokeResignationReasonBottomSheet(position: Int) {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_revoke_resignation, null)
        val binding = BottomSheetRevokeResignationBinding.bind(view)
        bottomSheetRevokeResignationDialog = BottomSheetDialog(this)
        binding.apply {
            btnClose.setOnClickListener {
                bottomSheetRevokeResignationDialog.dismiss()
            }
            btnSubmit.setOnClickListener {
                val reason = etReason.text ?: ""
                if (reason.isEmpty()) {
                    layoutReason.error = getString(R.string.enter_revoke_reason_to_continue)
                } else {
                    layoutReason.error = ""
                    val dateOfResignation = resignationViewList[position].DateOfResignation ?: ""
                    callRevokeResignationApi(reason.toString(), dateOfResignation)

                }
            }
        }
        bottomSheetRevokeResignationDialog.apply {
            setContentView(view)
            setCancelable(false)
            show()
        }

    }

    private fun callRevokeResignationApi(reason: String, dateOfResignation: String) {

        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                layoutNoInternet.root.visibility = GONE
                var dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
                val date = dateFormatter.parse(dateOfResignation)
                dateFormatter = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
                date?.let {
                    addResignationViewModel.callRevokeResignationApi(
                        request = RevokeResignationRequestModel(
                            GNETAssociateId = gnetAssociteId,
                            DateOfResignation = dateFormatter.format(it).toString(),
                            Reason = reason
                        )
                    )
                }
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerResignation.visibility = GONE
            }
        }
        bottomSheetRevokeResignationDialog.dismiss()

    }
}