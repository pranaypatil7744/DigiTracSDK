package com.example.digitracksdk.presentation.onboarding.pf_uan

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityPfuandetailsBinding
import com.example.digitracksdk.domain.model.onboarding.pf_uan.GetPFESICRequestModel
import com.example.digitracksdk.domain.model.onboarding.pf_uan.GetPFESICResponseModel
import com.example.digitracksdk.domain.model.onboarding.pf_uan.InsertPFESICRequestModel
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.onboarding.epf_details.adapter.DetailAdapter
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailItemType
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailModel
import com.example.digitracksdk.presentation.onboarding.epf_details.model.SpinnerType
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by Mo Khurseed Ansari on 17-10-2023.
 */
enum class PFUANItems(val value: Int) {
    DO_YOU_HAVE_EXISTING_PF_UAN(0),
    PF_UAN_No(1),
    Do_YOU_HAVE_EXISTING_ESIC_No(2),
    ESIC_No(3)

}

class PFUanDetailsActivity : BaseActivity(), DetailAdapter.DetailListener, ValidationListener {
    lateinit var binding: ActivityPfuandetailsBinding
    lateinit var preferenceUtils: PreferenceUtils
    private val paperlessPFUANViewModel: PaperlessPFUANViewModel by viewModel()
    var viewType: Constant.ViewType = Constant.ViewType.EDIT
    var list: ArrayList<DetailModel> = ArrayList()
    private val yesNoList: ArrayList<String> = ArrayList()
    lateinit var yesNoAdapter: ArrayAdapter<String>
    lateinit var detailAdapter: DetailAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPfuandetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        preferenceUtils = PreferenceUtils(this)
        paperlessPFUANViewModel.validationListener = this
        setUpToolbar()
        setUpObserver()
        setUpListener()
        callViewPFUanApi()
    }

    private fun setUpObserver() {
        binding.apply {

            with(paperlessPFUANViewModel)
            {
                getPFESICResponseData.observe(this@PFUanDetailsActivity)
                {
                    toggleLoader(false)

                    if (it.status?.lowercase() == Constant.success) {
                        setUpRecycler(it)
                    } else {
                        showToast(it.Message.toString())
                    }

                }
                insertPFESICResponseData.observe(this@PFUanDetailsActivity)
                {
                    toggleLoader(false)
                    if (it.status.toString().lowercase() == Constant.success) {
                        showToast(it.Message.toString())
                        finish()
                    } else {
                        showToast(it.Message.toString())
                    }
                }


            }


        }


    }

    private fun setUpRecycler(data: GetPFESICResponseModel?) {

        setUpData()

        list.addAll(
            ArrayList<DetailModel>().apply {
                clear()
                add(
                    DetailModel(
                        title = getString(R.string.do_you_have_existing_pf_uan_number),
                        value = if (data?.IsPFUAN == "Y") getString(R.string.yes) else if (data?.IsPFUAN == "N") getString(
                            R.string.no
                        ) else "",
                        itemType = DetailItemType.SPINNER,
                        spinnerType = SpinnerType.YESNO

                    )
                )

                add(
                    DetailModel(
                        title = getString(R.string.pf_uan),
                        value = data?.PFUANNumber?:"",
                        maxLength = 12,
                        itemType = DetailItemType.EDIT_TEXT_NUMBER

                    )

                )

                add(
                    DetailModel(
                        title = getString(R.string.do_you_have_existing_esic_ip_number),
                        value = if (data?.IsESICIp == "Y") getString(R.string.yes) else if (data?.IsESICIp == "N") getString(
                            R.string.no
                        ) else "",
                        itemType = DetailItemType.SPINNER,
                        spinnerType = SpinnerType.YESNO
                    )
                )
                add(
                    DetailModel(
                        title = getString(R.string.esic_number),
                        value = data?.IsESICIpNumber?:"",
                        maxLength = 17,
                        itemType = DetailItemType.EDIT_TEXT_NUMBER


                    )
                )

            }
        )
        setUpAdapter()
    }

    private fun setUpAdapter() {
        detailAdapter = DetailAdapter(this, list, yesNoAdapter = yesNoAdapter, listener = this)
        binding.recylerPFUAN.adapter = detailAdapter
    }

    private fun setUpData() {
        yesNoList.clear()
        yesNoList.add(getString(R.string.yes))
        yesNoList.add(getString(R.string.no))

        yesNoAdapter = ArrayAdapter<String>(
            this,
            R.layout.dropdown_menu_popup_item,
            R.id.text_information,
            yesNoList
        )
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callViewPFUanApi()
            }
        }

    }

    private fun callViewPFUanApi() {
        binding.apply {

            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = View.GONE
                toolbar.tvSave.visibility = View.VISIBLE
                toggleLoader(true)

                paperlessPFUANViewModel.callGetPFESICApi(
                    request = GetPFESICRequestModel(
                        InnovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
                    )

                )

            } else {
                toolbar.tvSave.visibility = View.GONE
                layoutNoInternet.root.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpToolbar() {

        binding.toolbar.apply {
            divider.visibility = View.VISIBLE
            tvTitle.text = getString(R.string.pf_uan)
            btnBack.setOnClickListener {
                finish()
            }

            tvSave.apply {
                visibility = View.VISIBLE
                text = getString(R.string.edit)
                setOnClickListener {
                    if (viewType == Constant.ViewType.EDIT) {
                        text = getString(R.string.save)
                        viewType = Constant.ViewType.ADD
                        enableFields()
                    } else {
                        clearError()
                        paperlessPFUANViewModel.validatePFESICRequestModel(
                            this@PFUanDetailsActivity,
                            getPFUANInsertModel()
                        )

                    }
                }

            }
        }
    }

    private fun getPFUANInsertModel(): InsertPFESICRequestModel {
        val request = InsertPFESICRequestModel()
        request.InnovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
        request.IsPFUAN =
            if (list[PFUANItems.DO_YOU_HAVE_EXISTING_PF_UAN.value].value.toString() == getString(R.string.yes)) Constant.Yes else if (list[PFUANItems.DO_YOU_HAVE_EXISTING_PF_UAN.value].value.toString() == getString(
                    R.string.no
                )
            ) Constant.No else ""
        request.PFUANNumber = list[PFUANItems.PF_UAN_No.value].value?.toString() ?: ""
        request.IsESICIp =
            if (list[PFUANItems.Do_YOU_HAVE_EXISTING_ESIC_No.value].value.toString() == getString(R.string.yes)) Constant.Yes else if (list[PFUANItems.Do_YOU_HAVE_EXISTING_ESIC_No.value].value.toString() == getString(
                    R.string.no
                )
            ) Constant.No else ""
        request.IsESICIpNumber = list[PFUANItems.ESIC_No.value].value?.toString() ?: ""
        return request
    }

    private fun clearError() {
        for (i in list) {
            i.error = ""
            i.isFocus = false
        }
        detailAdapter.notifyDataSetChanged()
    }

    private fun enableFields() {
        for (data in list) {
            data.isEnabled = true
        }
        detailAdapter.refresh(list)
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
        callInsertPFUANDataApi()
    }


    private fun callInsertPFUANDataApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            paperlessPFUANViewModel.callInsertPFESICApi(
                getPFUANInsertModel()
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun onValidationFailure(type: String, msg: Int) {

        when (type) {

            Constant.ListenerConstants.PF_UAN_NO_ERROR -> {
                list[PFUANItems.PF_UAN_No.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }

            Constant.ListenerConstants.ESIC_NO_ERROR -> {
                list[PFUANItems.ESIC_No.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }

            Constant.ListenerConstants.ESIC_CHECK_ERROR -> {
                list[PFUANItems.ESIC_No.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }

        }


    }

    override fun onSpinnerDataSelected(selectedPosition: Int, data: String, itemPosition: Int) {
        if (viewType == Constant.ViewType.ADD) {

            if (list[PFUANItems.DO_YOU_HAVE_EXISTING_PF_UAN.value].value.toString() == getString(R.string.no)) {
                list[PFUANItems.PF_UAN_No.value].value = ""
                list[PFUANItems.PF_UAN_No.value].isEnabled = false
                list[PFUANItems.PF_UAN_No.value].isFocus = false
            } else {
                list[PFUANItems.PF_UAN_No.value].isEnabled = true
                list[PFUANItems.PF_UAN_No.value].isFocus = true
            }


            if (list[PFUANItems.Do_YOU_HAVE_EXISTING_ESIC_No.value].value.toString() == getString(R.string.no)) {
                list[PFUANItems.ESIC_No.value].value = ""
                list[PFUANItems.ESIC_No.value].isEnabled = false
                list[PFUANItems.ESIC_No.value].isFocus = false
            } else {
                list[PFUANItems.ESIC_No.value].isEnabled = true
                list[PFUANItems.ESIC_No.value].isFocus = true

            }

//            setUpAdapter()
            detailAdapter.notifyDataSetChanged()
        }
    }

}