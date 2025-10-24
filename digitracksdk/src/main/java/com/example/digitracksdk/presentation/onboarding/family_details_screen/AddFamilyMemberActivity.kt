package com.example.digitracksdk.presentation.onboarding.family_details_screen

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityAddFamilyMemberBinding
import com.example.digitracksdk.domain.model.onboarding.ListFamily
import com.example.digitracksdk.domain.model.onboarding.insert.PaperlessFamilyDetailsModel
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.onboarding.epf_details.adapter.DetailAdapter
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailItemType
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailModel
import com.example.digitracksdk.presentation.onboarding.epf_details.model.SpinnerType
import com.example.digitracksdk.presentation.onboarding.family_details_screen.model.MultiEditTextModel
import com.example.digitracksdk.presentation.onboarding.family_details_screen.model.TxtInputType
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

enum class FamilyDetailsItems(val value:Int){
    RELATION(0),
    NAME(1),
    DOB(2),
    OCCUPATION(3),
    IS_RESIDING_WITH_YOU(4),
    ADDRESS(5),
    IS_NOMINEE(6),
    PF_ESIC(7),
    INSURANCE_GRATUITY(8)
}
class AddFamilyMemberActivity : BaseActivity(), ValidationListener, DetailAdapter.DetailListener {
    lateinit var binding: ActivityAddFamilyMemberBinding
    private var familyDetailModel: ListFamily? = null
    var relationList: ArrayList<String> = ArrayList()
    var nomineeList: ArrayList<String> = ArrayList()
    lateinit var relationAdapter: ArrayAdapter<String>
    lateinit var nomineeAdapter: ArrayAdapter<String>
    lateinit var residingWithYouAdapter: ArrayAdapter<String>
    lateinit var preferenceUtils: PreferenceUtils

    var isFromAdd: Boolean = false
    lateinit var detailAdapter: DetailAdapter

    var list: ArrayList<DetailModel> = ArrayList()
    private val paperlessViewFamilyDetailsViewModel: PaperlessViewFamilyDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddFamilyMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        paperlessViewFamilyDetailsViewModel.validationListener = this
        getIntentData()
        setObserver()
        setUpAdapter()
    }

    private fun setObserver() {
        binding.apply {
            with(paperlessViewFamilyDetailsViewModel){
                insertFamilyDetailsResponseData.observe(this@AddFamilyMemberActivity) {
                    if (it.status == Constant.SUCCESS) {
                        showToast(it.Message.toString())
                        finish()
                    } else {
                        showToast(it.Message.toString())
                    }
                }
                messageData.observe(this@AddFamilyMemberActivity) {
                    showToast(it)
                }
                showProgressBar.observe(this@AddFamilyMemberActivity) {
                    toggleLoader(it)
                }
            }
        }
    }

    private fun getIntentData() {

        intent.extras?.run {
            isFromAdd = getBoolean(Constant.IS_FROM_ADD, false)
            if (!isFromAdd) {
                familyDetailModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getSerializable(Constant.DATA, ListFamily::class.java)
                } else
                    getSerializable(Constant.DATA) as? ListFamily

            }
        }
        setUpToolbar()
        setUpRecycler()
    }

    private fun setUpRecycler() {
        relationList.add(getString(R.string.select_family_member))
        relationList.add(getString(R.string.father))
        relationList.add(getString(R.string.mother))
        relationList.add(getString(R.string.son))
        relationList.add(getString(R.string.daughter))
        relationList.add(getString(R.string.husband))
        relationList.add(getString(R.string.wife))
        relationList.add(getString(R.string.guardian))
        nomineeList.add(getString(R.string.yes))
        nomineeList.add(getString(R.string.no))

        relationAdapter = ArrayAdapter<String>(
            this,
            R.layout.dropdown_menu_popup_item,
            R.id.text_information,
            relationList
        )
        nomineeAdapter = ArrayAdapter<String>(
            this,
            R.layout.dropdown_menu_popup_item,
            R.id.text_information,
            nomineeList
        )
        residingWithYouAdapter = ArrayAdapter<String>(
            this,
            R.layout.dropdown_menu_popup_item,
            R.id.text_information,
            nomineeList
        )

        list.add(
            DetailModel(
                title = getString(R.string.relation),
                value = if (!isFromAdd) familyDetailModel?.Relation else getString(R.string.select_family_member),
                itemType = DetailItemType.SPINNER,
                spinnerType = SpinnerType.RELATION,
                isEnabled = isFromAdd
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.name),
                value = if (!isFromAdd) familyDetailModel?.FamilyMemberName else "",
                itemType = DetailItemType.EDIT_TEXT,
                isEnabled = isFromAdd
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.date_of_birth),
                value = if (!isFromAdd) familyDetailModel?.DateOfBirth else "",
                itemType = DetailItemType.EDIT_TEXT_DATE,
                icon = R.drawable.ic_calendar,
                isEnabled = isFromAdd
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.occupation),
                value = if (!isFromAdd) familyDetailModel?.Occupation else "",
                itemType = DetailItemType.EDIT_TEXT,
                isEnabled = isFromAdd
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.is_residing_with_you),
                value = if (!isFromAdd) familyDetailModel?.IsResidingWithYou else "",
                itemType = DetailItemType.SPINNER,
                spinnerType = SpinnerType.RESIDING_WITH_YOU,
                isEnabled = isFromAdd
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.address),
                value = if (!isFromAdd) familyDetailModel?.CurrentAddress else "",
                itemType = DetailItemType.EDIT_TEXT,
                isEnabled = isFromAdd
            )
        )
        list.add(
            DetailModel(
                title = getString(R.string.is_nominee),
                value = if (!isFromAdd) familyDetailModel?.IsNominee else "",
                itemType = DetailItemType.SPINNER,
                spinnerType = SpinnerType.NOMINEE,
                isEnabled = isFromAdd
            )
        )
        list.add(
            DetailModel(
                value = MultiEditTextModel(
                    hint1 = getString(R.string.pf_),
                    hint2 = getString(R.string.esic_card_),
                    value1 = if (!isFromAdd) familyDetailModel?.PF else "",
                    value2 = if (!isFromAdd) familyDetailModel?.ESIC else "",
                    inputType = TxtInputType.NUMBER,
                    inputType2 = TxtInputType.NUMBER,
                    maxLength1 = 3,
                    maxLength2 = 3
                ),
                isEnabled = isFromAdd,
                itemType = DetailItemType.MULTI_EDITTEXT_WITH_NUMBER
            )
        )
        list.add(
            DetailModel(
                value = MultiEditTextModel(
                    hint1 = getString(R.string.insurance_),
                    hint2 = getString(R.string.gratuity_),
                    value1 = if (!isFromAdd) familyDetailModel?.Insurance else "",
                    value2 = if (!isFromAdd) familyDetailModel?.Gratuity else "",
                    inputType = TxtInputType.NUMBER,
                    inputType2 = TxtInputType.NUMBER,
                    maxLength1 = 3,
                    maxLength2 = 3
                ),
                isEnabled = isFromAdd,
                itemType = DetailItemType.MULTI_EDITTEXT_WITH_NUMBER
            )
        )
        setUpAdapter()

    }

    private fun setUpAdapter() {
        if (::detailAdapter.isInitialized) {
            detailAdapter.notifyDataSetChanged()
        } else {
            detailAdapter = DetailAdapter(
                this,
                list = list,
                relationAdapter = relationAdapter,
                residingWithYouAdapter = residingWithYouAdapter,
                nomineeAdapter = nomineeAdapter,
                listener = this
            )
            binding.recyclerAddFamilyMember.adapter = detailAdapter
        }
    }


    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = if (isFromAdd) {
                getString(R.string.add_a_member)
            } else {
                getString(R.string.view_member)
            }
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
            tvSave.apply {
                if (isFromAdd) {
                    tvSave.visibility = VISIBLE
                    tvSave.text = getString(R.string.save)
                    setOnClickListener {
                        paperlessViewFamilyDetailsViewModel.validateFamilyRequestModel(
                            getPOBInsertFamilyModel(),this@AddFamilyMemberActivity,
                            associateDOB = preferenceUtils.getValue(Constant.PreferenceKeys.DOB)
                        )
                    }
                } else {
                    tvSave.visibility = GONE
                }
            }
        }
    }

    private fun getPOBInsertFamilyModel(): PaperlessFamilyDetailsModel {
        val request = PaperlessFamilyDetailsModel()
        val relationshipId: String
        when (list[0].value.toString()) {
            getString(R.string.father) -> {
                relationshipId = "1"
            }
            getString(R.string.mother) -> {
                relationshipId = "2"
            }
            getString(R.string.son) -> {
                relationshipId = "3"
            }
            getString(R.string.daughter) -> {
                relationshipId = "4"
            }
            getString(R.string.husband) -> {
                relationshipId = "7"
            }
            getString(R.string.wife) -> {
                relationshipId = "8"
            }
            getString(R.string.guardian) -> {
                relationshipId = "15"
            }
            else ->{
                relationshipId = "0"
            }
        }
        request.RelationshipId = relationshipId
        request.FamilyMemberName = list[FamilyDetailsItems.NAME.value].value.toString()
        request.DateOfBirth = list[FamilyDetailsItems.DOB.value].value.toString()
        request.Occupation = list[FamilyDetailsItems.OCCUPATION.value].value.toString()
        request.IsResidingWithYou = if (list[FamilyDetailsItems.IS_RESIDING_WITH_YOU.value].value.toString() == getString(R.string.yes)) Constant.Yes else if (list[FamilyDetailsItems.IS_RESIDING_WITH_YOU.value].value.toString() == getString(R.string.no))  Constant.No else ""
        request.CurrentAddress = list[FamilyDetailsItems.ADDRESS.value].value.toString()
        request.IsNominee = if (list[FamilyDetailsItems.IS_NOMINEE.value].value.toString() == getString(R.string.yes)) Constant.Yes else if (list[FamilyDetailsItems.IS_NOMINEE.value].value.toString() == getString(R.string.no)) Constant.No else ""
        request.PF = (list[FamilyDetailsItems.PF_ESIC.value].value as? MultiEditTextModel)?.value1.toString()
        request.ESIC = (list[FamilyDetailsItems.PF_ESIC.value].value as? MultiEditTextModel)?.value2.toString()
        request.Insurance = (list[FamilyDetailsItems.INSURANCE_GRATUITY.value].value as? MultiEditTextModel)?.value1.toString()
        request.Gratuity =(list[FamilyDetailsItems.INSURANCE_GRATUITY.value].value as? MultiEditTextModel)?.value2.toString()
        request.IsActive = "Y"
        request.InnovID = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
        return request
    }

    override fun onValidationSuccess(type: String, msg: Int) {
        clearError()
        callAddMemberApi()
    }

    private fun callAddMemberApi() {
        if (isNetworkAvailable()){
            paperlessViewFamilyDetailsViewModel.callInsertFamilyDetailsApi(getPOBInsertFamilyModel())
        }else{
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun onValidationFailure(type: String, msg: Int) {
        clearError()
        when(type){
            Constant.ListenerConstants.RELATIONSHIP_ERROR -> {
                list[FamilyDetailsItems.RELATION.value].apply {
                    error = getString(msg)
                }
            }
            Constant.ListenerConstants.FAMILY_NAME_ERROR -> {
                list[FamilyDetailsItems.NAME.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
            Constant.ListenerConstants.FAMILY_DOB_ERROR -> {
                list[FamilyDetailsItems.DOB.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
            Constant.ListenerConstants.FAMILY_OCCUPATION_ERROR -> {
                list[FamilyDetailsItems.OCCUPATION.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
            Constant.ListenerConstants.IS_RESIDING_WITH_YOU_ERROR -> {
                list[FamilyDetailsItems.IS_RESIDING_WITH_YOU.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
            Constant.ListenerConstants.ADDRESS_1_ERROR -> {
                list[FamilyDetailsItems.ADDRESS.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
            Constant.ListenerConstants.NOMINEE_ERROR -> {
                list[FamilyDetailsItems.IS_NOMINEE.value].apply {
                    error = getString(msg)
                    isFocus = true
                }
            }
            Constant.ListenerConstants.FAMILY_PF_ERROR -> {
                (list[FamilyDetailsItems.PF_ESIC.value].value as? MultiEditTextModel)?.apply {
                    error1 = getString(msg)
                    isFocus1 = true
                }
            }
            Constant.ListenerConstants.FAMILY_ESIC_ERROR -> {
                (list[FamilyDetailsItems.PF_ESIC.value].value as? MultiEditTextModel)?.apply {
                    error2 = getString(msg)
                    isFocus2 = true
                }
            }
            Constant.ListenerConstants.FAMILY_INSURANCE_ERROR -> {
                (list[FamilyDetailsItems.INSURANCE_GRATUITY.value].value as? MultiEditTextModel)?.apply {
                    error1 = getString(msg)
                    isFocus1 = true
                }
            }
            Constant.ListenerConstants.FAMILY_GRATUITY_ERROR -> {
                (list[FamilyDetailsItems.INSURANCE_GRATUITY.value].value as? MultiEditTextModel)?.apply {
                    error2 = getString(msg)
                    isFocus2 = true
                }
            }
            else ->{
                showToast(getString(msg))
            }
        }
        detailAdapter.notifyDataSetChanged()
    }

    private fun clearError() {
        for (i in list){
            i.error = ""
            i.isFocus = false
            (i.value as? MultiEditTextModel)?.error1 =""
            (i.value as? MultiEditTextModel)?.error2 =""
        }
        detailAdapter.notifyDataSetChanged()
    }

    override fun openDatePicker(position: Int) {
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH)
        val d = cal.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->

                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(Calendar.YEAR, year)
                selectedCalendar.set(Calendar.MONTH, monthOfYear)
                selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val dateToUpdate =
                    AppUtils.INSTANCE?.convertDateToString(selectedCalendar.time, "dd MMM yyyy")
                list[position].value = dateToUpdate
                detailAdapter.notifyItemChanged(position)
            }, y, m, d
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.root,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    override fun onSpinnerDataSelected(selectedPosition: Int, data: String, itemPosition: Int) {
        //refer list position
        if (itemPosition == FamilyDetailsItems.IS_RESIDING_WITH_YOU.value){
            if (data == getString(R.string.yes)){
                // disable address field here
                list[FamilyDetailsItems.ADDRESS.value].apply {
                    isEnabled = false
                    value = ""
                    detailAdapter.notifyDataSetChanged()
                }
            }else{
                list[FamilyDetailsItems.ADDRESS.value].apply {
                    isEnabled = true
                    detailAdapter.notifyDataSetChanged()
                }
            }
        }else if (itemPosition == FamilyDetailsItems.IS_NOMINEE.value){
            if (data == getString(R.string.yes)){
                list[FamilyDetailsItems.PF_ESIC.value].apply {
                    isEnabled = true
                }
                list[FamilyDetailsItems.INSURANCE_GRATUITY.value].apply {
                    isEnabled = true
                }
                detailAdapter.notifyDataSetChanged()
            }else{
                list[FamilyDetailsItems.PF_ESIC.value].apply {
                    value = MultiEditTextModel(
                        hint1 = getString(R.string.pf_),
                        hint2 = getString(R.string.esic_card_),
                        value1 = "",
                        value2 = "",
                        inputType = TxtInputType.NUMBER,
                        inputType2 = TxtInputType.NUMBER,
                        maxLength1 = 3,
                        maxLength2 = 3
                    )
                    isEnabled = false
                    itemType = DetailItemType.MULTI_EDITTEXT_WITH_NUMBER
                }
                list[FamilyDetailsItems.INSURANCE_GRATUITY.value].apply {
                    value = MultiEditTextModel(
                        hint1 = getString(R.string.insurance_),
                        hint2 = getString(R.string.gratuity_),
                        value1 = "",
                        value2 = "",
                        inputType = TxtInputType.NUMBER,
                        inputType2 = TxtInputType.NUMBER,
                        maxLength1 = 3,
                        maxLength2 = 3
                    )
                    isEnabled = false
                    itemType = DetailItemType.MULTI_EDITTEXT_WITH_NUMBER
                }
                detailAdapter.notifyDataSetChanged()
            }
        }
    }
}