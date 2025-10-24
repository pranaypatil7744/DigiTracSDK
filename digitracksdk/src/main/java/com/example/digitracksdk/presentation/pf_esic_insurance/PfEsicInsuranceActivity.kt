package com.example.digitracksdk.presentation.pf_esic_insurance

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityPfEsicInsuranceBinding
import com.example.digitracksdk.domain.model.home_model.request.InnovIDCardRequestModel
import com.example.digitracksdk.presentation.home.innov_id_card.InnovIDCardViewModel
import com.example.digitracksdk.presentation.my_letters.adapter.MyLettersAdapter
import com.example.digitracksdk.presentation.my_letters.model.MyLettersModel
import com.example.digitracksdk.presentation.pf_esic_insurance.model.PfEsicRequestModel
import com.example.digitracksdk.presentation.pf_esic_insurance.pf_esic_insurance_view_screen.PfEsicInsuranceViewActivity
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.DialogUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel


class PfEsicInsuranceActivity : BaseActivity(), MyLettersAdapter.MyLettersClickManager,
    DialogUtils.DialogManager {
    lateinit var binding: ActivityPfEsicInsuranceBinding
    private lateinit var myLettersAdapter: MyLettersAdapter
    private var selectedItemName = ""
    private var pfEsicInsuranceList: ArrayList<MyLettersModel> = ArrayList()
    private val pfEsicInsuranceViewModel: PfEsicInsuranceViewModel by viewModel()
    private val innovIdCardViewModel: InnovIDCardViewModel by viewModel()
    lateinit var preferenceUtils: PreferenceUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPfEsicInsuranceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setUpObserver()
        setUpPfInsuranceData()
    }

    private fun setUpObserver(){
        binding.apply {
            with(innovIdCardViewModel) {
                innovIDCardResponseData.observe(this@PfEsicInsuranceActivity
                ) { it ->
                    if (it?.status == Constant.SUCCESS) {
                        val fName = preferenceUtils.getValue(Constant.PreferenceKeys.FIRST_NAME)
                        val mName = preferenceUtils.getValue(Constant.PreferenceKeys.MiddleName)
                        val lName = preferenceUtils.getValue(Constant.PreferenceKeys.LAST_NAME)
                        DialogUtils.showPfUanDialog(
                            this@PfEsicInsuranceActivity,
                            "$fName $mName $lName",
                            it.newUANNo.toString()
                        )
                    } else {
                        showToast(it?.message.toString())
                    }
                }
                messageData.observe(this@PfEsicInsuranceActivity
                ) { it ->
                    if (it != null) {
                        showToast(it)
                    }
                }
                showProgressbar.observe(this@PfEsicInsuranceActivity) {
                    toggleLoader(it)
                }
            }

            with(pfEsicInsuranceViewModel) {
                responseData.observe(this@PfEsicInsuranceActivity
                ) { it ->
                    toggleLoader(false)
                    if (it.ImageArr.isNullOrEmpty()) {
                        showToast(it.Message.toString())
                    } else {
                        val intent = Intent(
                            this@PfEsicInsuranceActivity,
                            PfEsicInsuranceViewActivity::class.java
                        )
                        val bundle = Bundle()
                        bundle.putString(Constant.ITEM_NAME, selectedItemName)
                        bundle.putString(Constant.SHARE_IMAGE, it.ImageArr)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                }
                messageData.observe(this@PfEsicInsuranceActivity
                ) { it ->
                    toggleLoader(false)
                    showToast(it)
                }
            }
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.pf_esic_insurance)
            divider.visibility = View.VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun setUpPfInsuranceData() {
        pfEsicInsuranceList.clear()
        pfEsicInsuranceList.add(
            MyLettersModel(
                itemName = getString(R.string.pf_uan),
                itemIcon = R.drawable.ic_pf
            )
        )
        pfEsicInsuranceList.add(
            MyLettersModel(
                itemName = getString(R.string.esic_card),
                itemIcon = R.drawable.ic_esic_card
            )
        )
        pfEsicInsuranceList.add(
            MyLettersModel(
                itemName = getString(R.string.esic_medical_card),
                itemIcon = R.drawable.ic_esic_medical_card
            )
        )
        pfEsicInsuranceList.add(
            MyLettersModel(
                itemName = getString(R.string.medical_card),
                itemIcon = R.drawable.ic_medical_card
            )
        )
        setUpPfInsuranceAdapter()
    }

    private fun setUpPfInsuranceAdapter() {
        myLettersAdapter = MyLettersAdapter(this, pfEsicInsuranceList, this)
        binding.recyclerPfInsurance.adapter = myLettersAdapter
    }

    override fun onClickItems(position: Int) {
        when (val itemName = pfEsicInsuranceList[position].itemName.toString()) {
            getString(R.string.pf_uan) -> {
                callInnovIdCardApi()
            }
            else -> {
                callEsicCardApi(itemName)
            }
        }
    }

    private fun callInnovIdCardApi() {
        if (isNetworkAvailable()) {
            innovIdCardViewModel.callInnovIDCardApi(
                InnovIDCardRequestModel(
                    InnovID = preferenceUtils.getValue(
                        Constant.PreferenceKeys.INNOV_ID
                    )
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun getPfEsicInsuranceRequestModel(): PfEsicRequestModel {
        val request = PfEsicRequestModel()
        request.GNETAssociateID = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
        return request
    }

    private fun callEsicCardApi(itemName: String) {
        selectedItemName = itemName
        if (isNetworkAvailable()) {
            toggleLoader(true)
            when (itemName) {
                getString(R.string.esic_card) -> {
                    pfEsicInsuranceViewModel.callEsicCardApi(getPfEsicInsuranceRequestModel())
                }
                getString(R.string.esic_medical_card) -> {
                    pfEsicInsuranceViewModel.callEsicMedicalCardApi(getPfEsicInsuranceRequestModel())
                }
                getString(R.string.medical_card) -> {
                    pfEsicInsuranceViewModel.callMedicalCardApi(getPfEsicInsuranceRequestModel())
                }
            }
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