package com.example.digitracksdk.presentation.my_letters.candidate_loi

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityCandidateLoiBinding
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.my_letters.ViewCandidateLoiRequestModel
import com.example.digitracksdk.presentation.my_letters.candidate_loi.adapter.CandidateLoiAdapter
import com.example.digitracksdk.presentation.my_letters.candidate_loi.model.CandidateLoiModel
import com.example.digitracksdk.presentation.my_letters.candidate_loi.model.CandidateLoiStatus
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.ImageUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class CandidateLoiActivity : BaseActivity(), CandidateLoiAdapter.CandidateLoiManager {
    lateinit var binding: ActivityCandidateLoiBinding
    lateinit var candidateLoiAdapter: CandidateLoiAdapter
    private val candidateLoiViewModel: CandidateLoiViewModel by viewModel()
    lateinit var preferenceUtils: PreferenceUtils
    var innovId: String = ""
    var loiTrackingId: Int = 0
    var candidateLoiList: ArrayList<CandidateLoiModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCandidateLoiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        getIntentData()
        setUpToolbar()
        setUpListener()
        setUpObserver()
        setUpCandidateLoiAdapter()
        callCandidateLioListApi()
    }

    private fun setUpObserver() {
        binding.apply {
            with(candidateLoiViewModel) {
                candidateLoiListResponseData.observe(
                    this@CandidateLoiActivity
                ) { it ->
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        if (it.LstLoi?.size != 0) {
                            candidateLoiList.clear()
                            showNoDataLayout(false)
                            for (i in it.LstLoi ?: arrayListOf()) {
                                candidateLoiList.add(
                                    CandidateLoiModel(
                                        name = it.CandidateName,
                                        companyName = i.ClientName,
                                        joiningDate = AppUtils.INSTANCE?.convertDateFormat(
                                            "dd-MMM-yyyy",
                                            i.DateofJoining.toString(),
                                            "dd MMM yyyy"
                                        ),
                                        no = "",
                                        fileUrl = "Candidate_LOI.pdf",
                                        loiTrackingId = i.LOITrackingID,
                                        candidateLoiStatus = AppUtils.INSTANCE?.getMyLettersStatus(i.LOIStatus.toString())
                                            ?: CandidateLoiStatus.AWAITING
                                    )
                                )
                            }
                            candidateLoiAdapter.notifyDataSetChanged()
                        } else {
                            showNoDataLayout(true)
                        }
                    } else {
                        showToast(it.component1().toString())
                        showNoDataLayout(true)
                    }
                }

                viewCandidateLoiResponseData.observe(
                    this@CandidateLoiActivity
                ) { it ->
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        val file = it.LOIImage?.let { it1 ->
                            ImageUtils.INSTANCE?.writePDFToFile(
                                it1,
                                "Letter of Intent"
                            )
                        }
                        ImageUtils.INSTANCE?.openPdfFile(
                            this@CandidateLoiActivity,
                            file?.absolutePath.toString()
                        )

                    } else {
                        showToast(getString(R.string.something_went_wrong))
                    }
                }

                messageData.observe(
                    this@CandidateLoiActivity
                ) { t ->
                    toggleLoader(false)
                    showToast(t.toString())
                }
            }
        }
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callCandidateLioListApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                callCandidateLioListApi()
            }
        }
    }

    private fun callCandidateLioListApi() {
        if (isNetworkAvailable()) {
            binding.layoutNoInternet.root.visibility = GONE
            toggleLoader(true)
            candidateLoiViewModel.callCandidateLoiListApi(request = CommonRequestModel(InnovId = innovId))
        } else {
            binding.recyclerCandidateLoi.visibility = GONE
            binding.layoutNoInternet.root.visibility = VISIBLE
        }
        binding.layoutNoData.root.visibility= GONE

    }

    private fun callViewCandidateLoiApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            candidateLoiViewModel.callViewCandidateLoiApi(
                request = ViewCandidateLoiRequestModel(
                    LOITrackingId = loiTrackingId, InnovId = innovId
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun getIntentData() {
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.candidate_loi)
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
        }
    }

    private fun setUpCandidateLoiAdapter() {
        candidateLoiAdapter = CandidateLoiAdapter(this, candidateLoiList, this)
        binding.recyclerCandidateLoi.adapter = candidateLoiAdapter
    }

    override fun onItemClick(position: Int) {
        loiTrackingId = candidateLoiList[position].loiTrackingId ?: 0
        callViewCandidateLoiApi()
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
                noDataLayout = layoutNoData.root,
                recyclerView = recyclerCandidateLoi,
                show = show
            )
        }
    }
}