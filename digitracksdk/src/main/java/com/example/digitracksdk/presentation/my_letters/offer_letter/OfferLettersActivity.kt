package com.example.digitracksdk.presentation.my_letters.offer_letter

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.domain.model.my_letters.DownloadCandidateOfferLetterRequestModel
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityOfferLettersBinding
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.my_letters.OfferLetterAcceptRejectRequestModel
import com.example.digitracksdk.domain.model.my_letters.*
import com.example.digitracksdk.presentation.my_letters.candidate_loi.model.CandidateLoiStatus
import com.example.digitracksdk.presentation.my_letters.offer_letter.adapter.OfferLetterAdapter
import com.example.digitracksdk.presentation.my_letters.offer_letter.model.OfferLetterModel
import com.example.digitracksdk.presentation.onboarding.signature_screen.AddSignatureActivity
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.ImageUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class OfferLettersActivity : BaseActivity(), OfferLetterAdapter.OfferLetterManager {
    lateinit var binding: ActivityOfferLettersBinding
    private lateinit var offerLetterAdapter: OfferLetterAdapter
    private var offerLetterList: ArrayList<OfferLetterModel> = ArrayList()
    var innovId: String = ""
    var gnetAssociateId: String = ""
    var candidateType: String = ""
    var offerId: String = ""
    var offerPatternId: String = ""
    private val offerLetterViewModel: OfferLetterViewModel by viewModel()
    lateinit var preferenceUtils: PreferenceUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOfferLettersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        getPreferenceData()
        setUpToolbar()
        setUpObserver()
        setUpOfferLetterAdapter()
        setUpListener()
        callOfferListApi()
    }

    private fun setUpObserver() {
        binding.apply {
            with(offerLetterViewModel) {
                candidateOfferLetterResponseData.observe(
                    this@OfferLettersActivity
                ) { it ->
                    if (it.Status == Constant.SUCCESS) {
                        if (it.Offers?.size != 0) {
                            offerLetterList.clear()
                            showNoDataLayout(false)
                            for (i in it.Offers ?: arrayListOf()) {
                                offerLetterList.add(
                                    OfferLetterModel(
                                        title = i.ClientName,
                                        offerLetterStatus = AppUtils.INSTANCE?.getMyLettersStatus(i.OfferStatus.toString())
                                            ?: CandidateLoiStatus.AWAITING,
                                        designation = i.Designation,
                                        location = i.WorkLocation,
                                        fileUrl = "Offer_Letter.pdf",
                                        joiningDate = AppUtils.INSTANCE?.convertDateFormat(
                                            "dd-MMM-yyyy",
                                            i.JoiningDate.toString(),
                                            "dd MMM yyyy"
                                        ),
                                        OfferId = i.OfferID,
                                        OfferPatternId = i.OfferPatternId,
                                        CandidateType = i.CandidateType
                                    )
                                )
                            }
                            offerLetterAdapter.notifyDataSetChanged()
                        } else {
                            showNoDataLayout(true)
                        }
                    } else {
                        showNoDataLayout(true)
                        showToast(it.Message.toString())
                    }
                }

                offerLetterAcceptRejectResponseData.observe(this@OfferLettersActivity) {
                    if (it.status == Constant.success) {
                        showToast(it.Message.toString())
                        callOfferListApi()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                downloadCandidateOfferLetterResponseData.observe(
                    this@OfferLettersActivity
                ) { it ->
                    if (it.Status == Constant.SUCCESS) {
                        if (it.OfferLetterImage == "No File") {
                            showToast(it.OfferLetterImage.toString())
                        } else {
                            val file = it.OfferLetterImage?.let { it1 ->
                                ImageUtils.INSTANCE?.writePDFToFile(it1, "Offer letter")
                            }
                            ImageUtils.INSTANCE?.openPdfFile(
                                this@OfferLettersActivity,
                                file?.absolutePath.toString()
                            )
                        }

                    } else {
                        showToast(it.Message.toString())
                    }
                }

                showProgress.observe(this@OfferLettersActivity) {
                    toggleLoader(it)
                }

                messageData.observe(this@OfferLettersActivity) {
                    showToast(it.toString())
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        callOfferListApi()
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callOfferListApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                callOfferListApi()
            }
        }
    }

    private fun getPreferenceData() {
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
        gnetAssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
    }

    private fun callOfferListApi() {
        if (isNetworkAvailable()) {
            binding.layoutNoInternet.root.visibility = GONE
            toggleLoader(true)
            offerLetterViewModel.callCandidateOfferLetterListApi(
                request = CommonRequestModel(
                    InnovId = innovId
                )
            )
        } else {
            binding.recyclerOfferLetter.visibility = GONE
            binding.layoutNoInternet.root.visibility = VISIBLE
        }
        binding.layoutNoData.root.visibility = GONE
    }

    private fun setUpOfferLetterAdapter() {
        offerLetterAdapter = OfferLetterAdapter(this, offerLetterList, this)
        binding.recyclerOfferLetter.adapter = offerLetterAdapter
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.offer_letter)
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
        }
    }

    private fun showNoDataLayout(show: Boolean) {
        binding.apply {
            noDataLayout(
                noDataLayout = layoutNoData.root,
                recyclerView = recyclerOfferLetter,
                show = show
            )
        }
    }

    override fun onItemClick(position: Int) {
        val data = offerLetterList[position]
        offerId = data.OfferId.toString()
        offerPatternId = data.OfferPatternId.toString()
        candidateType = data.CandidateType.toString()
        callDownloadOfferLetterApi()
    }

    override fun clickOnAccept(position: Int) {
        val i = Intent(this, AddSignatureActivity::class.java)
        val b = Bundle()
        b.putBoolean(Constant.IS_FROM_OFFER_LETTER, true)
        b.putSerializable(Constant.DATA, offerLetterList[position])
        i.putExtras(b)
        startActivity(i)
    }

    override fun clickOnReject(position: Int) {
        val data = offerLetterList[position]
        if (isNetworkAvailable()) {
            offerLetterViewModel.callOfferLetterAcceptRejectApi(
                request = OfferLetterAcceptRejectRequestModel(
                    AcceptStatus = "false",
                    GNETAssociateID = gnetAssociateId,
                    ImageDocArray = null,
                    OfferID = data.OfferId?.toInt() ?: 0,
                    OfferPatternID = data.OfferPatternId?.toInt() ?: 0,
                    OfferType = data.CandidateType.toString()
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun callDownloadOfferLetterApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            offerLetterViewModel.callDownloadCandidateOfferLetterApi(
                request = DownloadCandidateOfferLetterRequestModel(
                    CandidateType = candidateType,
                    OfferPatternId = offerPatternId,
                    OfferId = offerId,
                    InnovID = innovId
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
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