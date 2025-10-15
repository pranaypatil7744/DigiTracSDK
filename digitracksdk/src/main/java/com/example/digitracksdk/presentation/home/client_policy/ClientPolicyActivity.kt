package com.example.digitracksdk.presentation.home.client_policy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityClientPolicyBinding
import com.example.digitracksdk.domain.model.client_policies.AcknowledgeRequestModel
import com.example.digitracksdk.domain.model.client_policies.ClientPoliciesRequestModel
import com.example.digitracksdk.domain.model.client_policies.ViewAckPolicyRequestModel
import com.example.digitracksdk.domain.model.client_policies.ViewPolicyRequestModel
import com.example.digitracksdk.presentation.home.client_policy.adapter.ClientPolicyAdapter
import com.example.digitracksdk.presentation.home.client_policy.model.ClientPolicyModel
import com.example.digitracksdk.presentation.home.client_policy.model.PolicyStatusType
import com.example.digitracksdk.presentation.web_view.WebViewActivity
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.DialogUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class ClientPolicyActivity : BaseActivity(), ClientPolicyAdapter.PolicyClickManager,
    DialogUtils.DialogManager {
    lateinit var binding: ActivityClientPolicyBinding
    lateinit var clientPolicyAdapter: ClientPolicyAdapter
    lateinit var preferenceUtils: PreferenceUtils
    private val clientPoliciesViewModel: ClientPoliciesViewModel by viewModel()
    private var policyList: ArrayList<ClientPolicyModel> = ArrayList()
    private var policyId: Int? = 0
    private var policyStatus: Int? = 0
    val policyChangeResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){
                callClientPoliciesApi()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityClientPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setUpObserver()
        setUpListener()
        setUpPolicyAdapter()
        callClientPoliciesApi()
    }


    private fun setUpObserver() {
        binding.apply {
            with(clientPoliciesViewModel) {
                clientPoliciesResponseData.observe(
                    this@ClientPolicyActivity
                ) {

                    if (it.status == Constant.success) {
                        if (!it.lstClientPolicyDetails.isNullOrEmpty()) {
                            showNoDataLayout(false)
                            policyList.clear()
                            for (i in it.lstClientPolicyDetails) {
                                policyList.add(
                                    ClientPolicyModel(
                                        policyName = i.ClientPolicyName,
                                        policyType = i.ClientName,
                                        date = i.AcknowledgementOn,
                                        policyUrl = i.ClientPolicyFilePath,
                                        policyId = i.ClientPolicyID,
                                        policyStatusType = if (i.AcknowledgementStatus == Constant.Yes) PolicyStatusType.ACKNOWLEDGED else PolicyStatusType.PENDING
                                    )
                                )
                            }
                            clientPolicyAdapter.notifyDataSetChanged()
                        } else {
                            binding.layoutNoData.tvNoData.text = it.Message
                            showNoDataLayout(true)
                        }
                    } else {
                        showNoDataLayout(true)
                        showToast(it.Message.toString())
                    }
                }
                insertAcknowledgeResponseData.observe(this@ClientPolicyActivity) {
                    if (it.status == Constant.SUCCESS) {
                        DialogUtils.closeAcknowledgeDialog()
                        showToast(it.Message.toString())
                        callClientPoliciesApi()
                    } else {
                        DialogUtils.closeAcknowledgeDialog()
                        showToast(it.Message.toString())
                    }
                }

                viewPolicyResponseData.observe(this@ClientPolicyActivity) {
                    if (it.status?.lowercase() == Constant.success) {
                        /*val file = it.ClientPolicyImageArr?.let { it1 ->
                            ImageUtils.INSTANCE?.writePDFToFile(it1, pdfName = "Client policy")
                        }
                        ImageUtils.INSTANCE?.openPdfFile(
                            this@ClientPolicyActivity,
                            file?.absolutePath.toString()
                        )*/
                        val i = Intent(this@ClientPolicyActivity, WebViewActivity::class.java)
                        val b = Bundle()
                        b.putString(
                            Constant.WEB_URL,
                            it.ClientPolicyURL
                        )
                        b.putBoolean(Constant.IS_PDF, true)
                        b.putString(Constant.SCREEN_NAME, getString(R.string.view_policy))
                        b.putString(Constant.POLICY_ID, policyId.toString())
                        b.putString(Constant.POLICY_STATUS, policyStatus.toString())
                        i.putExtras(b)
                        policyChangeResult.launch(i)
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                viewAckPolicyResponseData.observe(this@ClientPolicyActivity)
                {
                    if (it.Status?.lowercase() == Constant.success) {

                        val i = Intent(this@ClientPolicyActivity, WebViewActivity::class.java)
                        val b = Bundle()
                        b.putString(
                            Constant.WEB_URL,
                            it.FilePath
                        )
                        b.putBoolean(Constant.IS_PDF, true)
                        b.putString(Constant.SCREEN_NAME, getString(R.string.view_acknowledged))
                        i.putExtras(b)
                        startActivity(i)
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(this@ClientPolicyActivity) {
                    showToast(it)
                }
                showProgressBar.observe(this@ClientPolicyActivity) {
                    toggleLoader(it)
                }
            }
        }
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callClientPoliciesApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                callClientPoliciesApi()
            }

        }
    }

    private fun callClientPoliciesApi() {
        with(binding) {

            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                clientPoliciesViewModel.callClientPoliciesApi(
                    request = ClientPoliciesRequestModel(
                        InnovID = preferenceUtils.getValue(com.example.digitracksdk.Constant.PreferenceKeys.INNOV_ID)
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerPolicy.visibility = GONE
            }
            layoutNoData.root.visibility = GONE
        }

    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
            tvTitle.text = getString(R.string.policy)
        }
    }

    private fun setUpPolicyAdapter() {
        clientPolicyAdapter = ClientPolicyAdapter(this, policyList, this)
        binding.recyclerPolicy.adapter = clientPolicyAdapter
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
                noDataLayout = layoutNoData.root, recyclerView = recyclerPolicy, show = show
            )
        }
    }

    override fun clickOnViewBtn(position: Int) {
        val data = policyList[position]
        policyId = data.policyId
        policyStatus = data.policyStatusType.value
        callViewPolicyApi(policyList[position].policyId ?: 0)
    }

    private fun callViewPolicyApi(policyId: Int) {
        if (isNetworkAvailable()) {
            clientPoliciesViewModel.callViewPolicyApi(
                request = ViewPolicyRequestModel(
                    ClientPolicyID = policyId
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun clickOnAckBtn(position: Int) {
        val data = policyList[position]
        policyId = data.policyId
        callViewAcknowledgedPolicyApi(policyId)
    }

    private fun callViewAcknowledgedPolicyApi(policyId: Int? = 0) {
        if (isNetworkAvailable()) {
            clientPoliciesViewModel.callViewAckPolicyApi(
                ViewAckPolicyRequestModel(
                    GnetassociateId = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID),
                    ClientId = preferenceUtils.getValue(Constant.PreferenceKeys.ClientID),
                    PolicyId = policyId.toString()
                )

            )

        } else {
            showToast(getString(R.string.no_internet_connection))
        }

    }

    override fun onConfirmClick(isCheck: Boolean) {
        super.onConfirmClick(isCheck)
        if (isCheck) {
            callInsertAcknowledgeApi()
        } else {
            showToast(getString(R.string.please_select_term_and_condition))
        }
    }

    private fun callInsertAcknowledgeApi() {
        if (isNetworkAvailable()) {
            clientPoliciesViewModel.callInsertAcknowledgeApi(getInsertAcknowledgeRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun getInsertAcknowledgeRequestModel(): AcknowledgeRequestModel {
        val request = AcknowledgeRequestModel()
        request.ClientId = preferenceUtils.getValue(Constant.PreferenceKeys.ClientID)
        request.GNETAssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
        request.PolicyId = policyId ?: 0
        return request
    }
}