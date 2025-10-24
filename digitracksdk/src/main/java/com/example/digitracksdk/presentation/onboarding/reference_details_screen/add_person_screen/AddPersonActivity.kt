package com.example.digitracksdk.presentation.onboarding.reference_details_screen.add_person_screen

import android.os.Build
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityAddPersonBinding
import com.example.digitracksdk.domain.model.onboarding.LstReferenceDetail
import com.example.digitracksdk.domain.model.onboarding.insert.GetReferenceCategoryModelItem
import com.example.digitracksdk.domain.model.onboarding.insert.InsertCandidateReferenceDetailsModel
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.onboarding.epf_details.adapter.DetailAdapter
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailItemType
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailModel
import com.example.digitracksdk.presentation.onboarding.epf_details.model.SpinnerType
import com.example.digitracksdk.presentation.onboarding.reference_details_screen.PaperlessViewCandidateReferenceDetailsViewModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

enum class ReferenceDetailsItem(val value:Int){
    REF_CATEGORY(0),
    NAME(1),
    CONTACT_NUM(2),
    EMAIL(3),
    ADDRESS(4)
}

class AddPersonActivity : BaseActivity(), ValidationListener {

    private lateinit var referenceCategoryAdapter: ArrayAdapter<String>
    private var referenceCategoryList: ArrayList<String> = ArrayList()
    lateinit var preferenceUtils: PreferenceUtils
    var referenceCategoryListId: ArrayList<GetReferenceCategoryModelItem> = ArrayList()
    lateinit var binding: ActivityAddPersonBinding
    var list: ArrayList<DetailModel> = ArrayList()
    var referenceModel: LstReferenceDetail? = null
    lateinit var adapter: DetailAdapter
    var viewType = Constant.ViewType.ADD
    private var isFromAdd: Boolean = false
    private val paperlessViewCandidateReferenceDetailsViewModel: PaperlessViewCandidateReferenceDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        preferenceUtils = PreferenceUtils(this)
        paperlessViewCandidateReferenceDetailsViewModel.validationListener = this
        getIntentData()
        setObserver()
    }

    private fun setObserver() {
        binding.apply {
            with(paperlessViewCandidateReferenceDetailsViewModel) {
                insertCandidateReferenceDetailsResponseData.observe(this@AddPersonActivity
                ) {
                    toggleLoader(false)
                    if (it.status == Constant.SUCCESS) {
                        showToast(it.Message.toString())
                        finish()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                getReferenceCategoryResponseData.observe(this@AddPersonActivity) {
                    toggleLoader(false)
                    if (!it.isEmpty()) {
                        referenceCategoryList.add(getString(R.string.Select))
                        for (i in it) {
                            referenceCategoryList.add(i.ReferenceCategoryName)
                            referenceCategoryListId = it
                        }
                    } else {
                        showToast(getString(R.string.something_went_wrong))
                    }
                }

                messageData.observe(this@AddPersonActivity) {
                    toggleLoader(false)
                    showToast(it)
                }
            }
        }
    }

    private fun getIntentData() {
        intent.extras?.run {
            isFromAdd = getBoolean(Constant.IS_FROM_ADD, false)
            if (!isFromAdd) {
                referenceModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getSerializable(
                        Constant.DATA,
                        LstReferenceDetail::class.java) as LstReferenceDetail
                }else
                    getSerializable(Constant.DATA) as LstReferenceDetail

            } else {
                callGetReferenceCategoryApi()
            }
        }
        setUpToolbar()
        setUpAdapter()
        setUpRecycler()
    }

    private fun callGetReferenceCategoryApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            paperlessViewCandidateReferenceDetailsViewModel.callGetReferenceCategorysApi()
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

    private fun setUpRecycler() {
        list.add(
            DetailModel(
                title = getString(R.string.reference_category),
                itemType = DetailItemType.SPINNER,
                isEnabled = isFromAdd,
                spinnerType = SpinnerType.REFERENCE,
                value = if (!isFromAdd) referenceModel?.ReferenceCategoryName else getString(R.string.Select)
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.name),
                itemType = DetailItemType.EDIT_TEXT,
                isEnabled = isFromAdd,
                value = if (!isFromAdd) referenceModel?.Name else ""
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.contact_number),
                itemType = DetailItemType.EDIT_TEXT_NUMBER,
                isEnabled = isFromAdd,
                value = if (!isFromAdd) referenceModel?.ContactNo else "",
                maxLength = 10
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.email_address),
                itemType = DetailItemType.EDIT_TEXT_EMAIL,
                isEnabled = isFromAdd,
                value = if (!isFromAdd) referenceModel?.EmailId else ""
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.address),
                itemType = DetailItemType.EDIT_TEXT,
                isEnabled = isFromAdd,
                value = if (!isFromAdd) referenceModel?.Address else ""
            )
        )
        adapter.notifyDataSetChanged()
    }

    private fun setUpAdapter() {
        referenceCategoryAdapter = ArrayAdapter<String>(
            this,
            R.layout.dropdown_menu_popup_item,
            R.id.text_information,
            referenceCategoryList
        )
        adapter = DetailAdapter(this, list, referenceAdapter = referenceCategoryAdapter)
        binding.recyclerAddPerson.adapter = adapter
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text =
                if (isFromAdd) getString(R.string.add_a_person) else getString(R.string.reference_details)
            tvLeftTitle1.apply {
                text = getString(R.string.save)
                visibility = if (isFromAdd) VISIBLE else GONE
                setTextColor(ContextCompat.getColor(this@AddPersonActivity, R.color.dodger_blue))
                setOnClickListener {
                    paperlessViewCandidateReferenceDetailsViewModel.validateInsertCandidateReferenceDetailsModel(
                        getInsertCandidateReferenceModel(),this@AddPersonActivity
                    )
                }
            }
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
        }
    }

    private fun getInsertCandidateReferenceModel(): InsertCandidateReferenceDetailsModel {
        val listReferId = referenceCategoryListId.find { it.ReferenceCategoryName == list[ReferenceDetailsItem.REF_CATEGORY.value].value.toString() }
        val request = InsertCandidateReferenceDetailsModel()
        request.Name = list[ReferenceDetailsItem.NAME.value].value.toString()
        request.ContactNo = list[ReferenceDetailsItem.CONTACT_NUM.value].value.toString()
        request.EmailId = list[ReferenceDetailsItem.EMAIL.value].value.toString()
        request.ReferenceCategory = list[ReferenceDetailsItem.REF_CATEGORY.value].value.toString()
        request.Address = list[ReferenceDetailsItem.ADDRESS.value].value.toString()
        request.InnovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
        request.ReferenceCategoryID = listReferId?.ReferenceCategoryID?:0
        request.Source = "DT"
        return request
    }

    override fun onValidationSuccess(type: String, msg: Int) {
        clearError()
        callInsertReferenceApi()
    }

    private fun callInsertReferenceApi() {
        if (isNetworkAvailable()) {
            toggleLoader(true)
            paperlessViewCandidateReferenceDetailsViewModel.callInsertCandidateReferenceDetailsApi(
                getInsertCandidateReferenceModel()
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun onValidationFailure(type: String, msg: Int) {
        clearError()
        when(type){
            Constant.ListenerConstants.NAME_ERROR ->{
                list[ReferenceDetailsItem.NAME.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
            Constant.ListenerConstants.MOBILE_ERROR ->{
                list[ReferenceDetailsItem.CONTACT_NUM.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
            Constant.ListenerConstants.CANDIDATE_EMAIL_ERROR ->{
                list[ReferenceDetailsItem.EMAIL.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
            Constant.ListenerConstants.ADDRESS_1_ERROR ->{
                list[ReferenceDetailsItem.ADDRESS.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
            Constant.ListenerConstants.REFERENCE_TYPE_ERROR ->{
                list[ReferenceDetailsItem.REF_CATEGORY.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
        }
    }

    private fun clearError() {
        for (i in list){
            i.error = ""
            i.isFocus = false
        }
        adapter.notifyDataSetChanged()
    }
}