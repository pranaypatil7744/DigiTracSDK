package com.example.digitracksdk.presentation.onboarding.esic_details

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityEsicdetailsBinding
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewEsicResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertESICDetailsModel
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.onboarding.epf_details.adapter.DetailAdapter
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailItemType
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailModel
import com.example.digitracksdk.presentation.onboarding.epf_details.model.SpinnerType
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

enum class EsicItems(val value:Int){
    DO_YOU_HAVE_OLD_ESIC(0),
    ESIC_NUMBER(1),
    INSURANCE_NUMBER(2),
    EMP_CODE(3),
    BRANCH_NAME(4),
    DISPENSARY_NAME(5)
}

class ESICDetailsActivity : BaseActivity(), ValidationListener, DetailAdapter.DetailListener {

    lateinit var binding: ActivityEsicdetailsBinding
    private val yesNoList: ArrayList<String> = ArrayList()
    lateinit var yesNoAdapter: ArrayAdapter<String>
    lateinit var detailAdapter: DetailAdapter
    lateinit var preferenceUtils: PreferenceUtils
    var viewType: Constant.ViewType = Constant.ViewType.EDIT
    private val paperlessViewEsicViewModel: PaperlessViewEsicViewModel by viewModel()
    var list: ArrayList<DetailModel> = ArrayList()
    val request = POBInsertESICDetailsModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEsicdetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        paperlessViewEsicViewModel.validationListener = this
        setUpToolbar()
        setUpObserver()
        setUpListener()
        callViewEsicApi()
    }

    private fun setUpObserver() {
        binding.apply {
            with(paperlessViewEsicViewModel) {
                viewEsicResponseData.observe(this@ESICDetailsActivity
                ) {
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        setUpRecycler(it)
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                insertEsicResponseData.observe(this@ESICDetailsActivity
                ) {
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        showToast(it.Message.toString())
                        finish()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(this@ESICDetailsActivity) {
                    toggleLoader(false)
                    showToast(it)
                }
            }
        }
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callViewEsicApi()
            }
        }
    }


    private fun callViewEsicApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                toolbar.tvSave.visibility = VISIBLE
                toggleLoader(true)
                paperlessViewEsicViewModel.callViewEsicApi(
                    request = InnovIDRequestModel(
                        InnovID = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
                    )
                )
            } else {
                toolbar.tvSave.visibility = GONE
                layoutNoInternet.root.visibility = VISIBLE
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


    private fun setUpAdapter() {
        detailAdapter = DetailAdapter(this, list,yesNoAdapter = yesNoAdapter,listener = this)
        binding.recyclerEsic.adapter = detailAdapter
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
    private fun setUpRecycler(data: PaperlessViewEsicResponseModel) {
        setUpData()
        list.clear()
        list.add(
            DetailModel(
                title = getString(R.string.do_you_have_old_esic),
                value = if (data.DoYouHaveAnOldESICNo == "Y") getString(R.string.yes) else if (data.DoYouHaveAnOldESICNo == "N") getString(R.string.no) else "",
                itemType = DetailItemType.SPINNER,
                spinnerType = SpinnerType.YESNO
            )

        )
        list.add(
            DetailModel(title = getString(R.string.esic_number),
            value = data.ExistESICNo,maxLength = 10,
            itemType = DetailItemType.EDIT_TEXT_NUMBER)
        )
        list.add(
            DetailModel(
                title = getString(R.string.insurance_number),
                value = data.InsuranceNumber,
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.employee_code_number),
                value = data.PreviousEmployeeCodeNumber,
                itemType = DetailItemType.EDIT_TEXT_NUMBER
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.branch_office_name),
                value = data.BranchOfficeName
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.dispensary_name),
                value = data.DispensaryName
            )
        )
        setUpAdapter()
    }


    private fun setUpToolbar() {
        binding.toolbar.apply {
            divider.visibility = VISIBLE
            tvTitle.text = getString(R.string.esic_details)
            btnBack.setOnClickListener {
                finish()
            }
            tvSave.apply {
                visibility = VISIBLE
                text = getString(R.string.edit)
                setOnClickListener {
                    if (viewType == Constant.ViewType.EDIT) {
                        text = getString(R.string.save)
                        viewType = Constant.ViewType.ADD
                        if (list[0].value == getString(R.string.yes)){
                            enableFields()
                        }else{
                            list[0].isEnabled = true
                            detailAdapter.notifyDataSetChanged()
                        }
                    } else {
                        clearError()
                        if (list[0].value == getString(R.string.no)){
                            showToast(context.getString(R.string.no_esic_details_available))
                        }else{
                            paperlessViewEsicViewModel.validateESICRequestModel(getPOBInsertEsicModel())
                        }
                    }
                }
            }
        }
    }

    private fun getPOBInsertEsicModel(): POBInsertESICDetailsModel {
        //Refer list index position
        request.DoYouHaveAnOldESICNo = if (list[EsicItems.DO_YOU_HAVE_OLD_ESIC.value].value.toString() == getString(R.string.yes)) Constant.Yes else if (list[EsicItems.DO_YOU_HAVE_OLD_ESIC.value].value.toString() == getString(R.string.no)) Constant.No else ""
        request.ESICNo = list[EsicItems.ESIC_NUMBER.value].value.toString()
        request.InsuranceNo = list[EsicItems.ESIC_NUMBER.value].value.toString()
        request.EmpCode = list[EsicItems.EMP_CODE.value].value.toString()
        request.BranchOfficeName = list[EsicItems.BRANCH_NAME.value].value.toString()
        request.DispensaryName = list[EsicItems.DISPENSARY_NAME.value].value.toString()
        request.InnovID = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
        return request
    }

    private fun enableFields() {
        for (data in list) {
            data.isEnabled = true
        }
        list[EsicItems.INSURANCE_NUMBER.value].isEnabled = false
        list[EsicItems.INSURANCE_NUMBER.value].isFocus = false
        detailAdapter.refresh(list)
    }

    override fun onValidationSuccess(type: String, msg: Int) {
        callInsertEsicDataApi()
    }

    private fun callInsertEsicDataApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            paperlessViewEsicViewModel.callInsertEsicApi(
                request
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun onValidationFailure(type: String, msg: Int) {
        //please refer list position
        when(type){
            Constant.ListenerConstants.ESIC_CHECK_ERROR -> {
                list[EsicItems.DO_YOU_HAVE_OLD_ESIC.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
            Constant.ListenerConstants.ESIC_NO_ERROR -> {
                list[EsicItems.ESIC_NUMBER.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
//            Constant.ListenerConstants.INSURANCE_NO_ERROR -> {
//                list[EsicItems.INSURANCE_NUMBER.value].apply {
//                    error = getString(msg)
//                    isFocus = true
//                }
//            }
            Constant.ListenerConstants.EMP_CODE_ERROR -> {
                list[EsicItems.EMP_CODE.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
            Constant.ListenerConstants.BRANCH_OFFICE_ERROR -> {
                list[EsicItems.BRANCH_NAME.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
            Constant.ListenerConstants.DISPENSARY_NAME_ERROR -> {
                list[EsicItems.DISPENSARY_NAME.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
        }
        detailAdapter.notifyDataSetChanged()
    }

    private fun clearError(){
        for (i in list){
            i.error = ""
            i.isFocus = false
        }
        list[EsicItems.INSURANCE_NUMBER.value].isFocus = false
        detailAdapter.notifyDataSetChanged()
    }

    override fun onTextChanged(position: Int, text: String) {
        //TODO solve text change issue
        if (position == EsicItems.ESIC_NUMBER.value){
            if (text.isNullOrEmpty()){
                list[EsicItems.INSURANCE_NUMBER.value].value = ""
                list[EsicItems.ESIC_NUMBER.value].isFocus = true
            }else{
                list[EsicItems.INSURANCE_NUMBER.value].value = text
                list[EsicItems.ESIC_NUMBER.value].isFocus = true
            }
//            detailAdapter.notifyDataSetChanged()
            detailAdapter.notifyItemChanged(EsicItems.INSURANCE_NUMBER.value)
        }
    }

    override fun onSpinnerDataSelected(selectedPosition: Int, data: String,itemPosition:Int) {
        if (viewType == Constant.ViewType.ADD){
            if (itemPosition == 0){
                if (data == getString(R.string.no)){
                    for (i in list){
                        i.isEnabled = false
                    }
                    list[itemPosition].isEnabled = true
                }else{
                    for (i in list){
                        i.isEnabled = true
                    }
                    list[EsicItems.INSURANCE_NUMBER.value].isEnabled = false
                    list[EsicItems.INSURANCE_NUMBER.value].isFocus = false
                }
                setUpAdapter()
            }
        }
    }

}