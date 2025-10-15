package com.example.digitracksdk.presentation.my_profile.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View.*
import com.example.digitracksdk.domain.model.onboarding.pan_verification.Result
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.EmpProfileDetailsItemBinding
import com.innov.digitrac.databinding.ItemProfileDetailsBinding
import com.example.digitracksdk.domain.model.home_model.response.InnovIDCardResponseModel
import com.example.digitracksdk.domain.model.onboarding.bank_account_verification.Details
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification_digilocker.Data
import com.example.digitracksdk.presentation.my_profile.model.ProfileDetailsModel
import com.example.digitracksdk.presentation.my_profile.model.ProfileModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.ImageUtils

class MyProfileAdapter(
    val context: Context,
    var list: ArrayList<ProfileModel>,
    private val profileDetailsList: ArrayList<InnovIDCardResponseModel>,
    private val listener: ProfileClickManager,
    private var isFromVerifyAadhaar: Boolean = false,
    private var aadhaarDetails: ArrayList<Data> = ArrayList(),
//    private var isFromVerifyAadhaarDigiLocker: Boolean = false,
//    private var aadhaarDigiLockerDetails: ArrayList<Data> = ArrayList(),
    private var isFromVerifyPanCard: Boolean = false,
    private var panCardDetails: ArrayList<Result> = ArrayList(),
    private var isFromVerifyBank: Boolean = false,
    private var bankDetails: ArrayList<Details> = ArrayList(),

    ) : RecyclerView.Adapter<MyProfileAdapter.ViewHolder>() {


    enum class SummaryDetailsType(val value: Int) {
        EMP_PROFILE_DETAILS(1),
        EMP_PROFILE_DETAILS_ITEM(2)
    }

    class ViewHolder : RecyclerView.ViewHolder {
        var itemProfileDetailsBinding: ItemProfileDetailsBinding? = null

        constructor(binding: ItemProfileDetailsBinding) : super(binding.root) {
            itemProfileDetailsBinding = binding
        }

        var empProfileDetailsItemBinding: EmpProfileDetailsItemBinding? = null

        constructor(binding: EmpProfileDetailsItemBinding) : super(binding.root) {
            empProfileDetailsItemBinding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            SummaryDetailsType.EMP_PROFILE_DETAILS.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_profile_details, parent, false)
                val binding = ItemProfileDetailsBinding.bind(view)
                ViewHolder(binding)
            }
            SummaryDetailsType.EMP_PROFILE_DETAILS_ITEM.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.emp_profile_details_item, parent, false)
                val binding = EmpProfileDetailsItemBinding.bind(view)
                ViewHolder(binding)

            }
            else -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.bottom_sheet_payslip_download, parent, false)
                val binding = ItemProfileDetailsBinding.bind(view)
                ViewHolder(binding)
            }
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when (holder.itemViewType) {
            SummaryDetailsType.EMP_PROFILE_DETAILS.value -> {
                holder.itemProfileDetailsBinding?.apply {
                    if (isFromVerifyAadhaar) {
                        val aadhaarData = aadhaarDetails[0]
                        btnEditProfile.visibility = INVISIBLE
                        textName.text = aadhaarData.Name
                        tvDesignation.visibility = GONE
                        val pic = ImageUtils.INSTANCE?.stringToBitMap(aadhaarData.Profile)
                        ImageUtils.INSTANCE?.loadBitMap(icProfile, pic)
                    } else if(isFromVerifyPanCard){
                        val panCardDetails = panCardDetails[0]
                        btnEditProfile.visibility = INVISIBLE
                        textName.text = panCardDetails.user_full_name
                        tvDesignation.visibility = GONE
                        icProfile.visibility= GONE
                    }else if(isFromVerifyBank){
                        val bankDetails = bankDetails[0]
                        btnEditProfile.visibility = INVISIBLE
                        textName.text = bankDetails.BeneName
                        tvDesignation.visibility = GONE
                        icProfile.visibility= GONE
                    }
                    else {
                        if (profileDetailsList.size != 0) {
                            val data = profileDetailsList[0]
                            textName.text = data.associateFirstName + " " + data.associateLastName
                            tvDesignation.text = data.designation
                            val pic = ImageUtils.INSTANCE?.stringToBitMap(data.picture)
                            ImageUtils.INSTANCE?.loadBitMap(icProfile, pic)
                        }
                        btnEditProfile.visibility = VISIBLE
                    }
                    btnEditProfile.setOnClickListener {
                        listener.onEditBtnClick(position)
                    }
                }
            }
            SummaryDetailsType.EMP_PROFILE_DETAILS_ITEM.value -> {
                holder.empProfileDetailsItemBinding?.apply {
                    val profileDetailList: ArrayList<ProfileDetailsModel> = ArrayList()
                    profileDetailList.clear()
                    if (isFromVerifyAadhaar) {
                        val aadhaarData = aadhaarDetails[0]
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.aadhaar_no),
                                value = aadhaarData.AadhaarNo.toString()
                            )
                        )

                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.father_husband_name),
                                value = if (!aadhaarData.Father_Husband_Name.isNullOrEmpty()) aadhaarData.Father_Husband_Name.toString() else ""
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.date_of_birth),
                                value = aadhaarData.DOB.toString()
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.gender),
                                value = if (aadhaarData.Gender.toString() == "M") context.getString(
                                    R.string.male
                                ) else if (aadhaarData.Gender.toString() == "F") context.getString(
                                    R.string.female
                                ) else ""
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.address),
                                value = if (!aadhaarData.Address.isNullOrEmpty()) aadhaarData.Address.toString() else ""
                            )
                        )

                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.sub_dist),
                                value = if (!aadhaarData.SubDistrict.isNullOrEmpty()) aadhaarData.SubDistrict.toString() else ""
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.post_office),
                                value = if (!aadhaarData.PostOffice.isNullOrEmpty()) aadhaarData.PostOffice.toString() else ""
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.district),
                                value = if (!aadhaarData.District.isNullOrEmpty()) aadhaarData.District.toString() else ""
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.city),
                                value = if (!aadhaarData.City.isNullOrEmpty()) aadhaarData.City.toString() else ""
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.state),
                                value = if (!aadhaarData.State.isNullOrEmpty()) aadhaarData.State.toString() else ""
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.country),
                                value = if (!aadhaarData.Country.isNullOrEmpty()) aadhaarData.Country.toString() else ""
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.pin_code),
                                value = if (!aadhaarData.Pin.isNullOrEmpty()) aadhaarData.Pin.toString() else ""
                            )
                        )
                    }
                    else if(isFromVerifyPanCard){
                        val panCardDetails = panCardDetails[0]
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.pan_card_number_caps),
                                value = panCardDetails.pan_number.toString()
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.aadhaar_card_number),
                                value = panCardDetails.masked_aadhaar.toString()
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.aadhaar_linked),
                                value = panCardDetails.aadhaar_linked_status.toString()
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.date_of_birth),
                                value = panCardDetails.user_dob.toString()
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.gender),
                                value = panCardDetails.user_gender.toString()
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.pantype),
                                value = panCardDetails.pan_type.toString()
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.mobile_no),
                                value = panCardDetails.user_phone_number.toString()
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.email),
                                value = panCardDetails.user_email.toString()
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.address1),
                                value = panCardDetails.user_address?.line_1.toString()
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.address2),
                                value = panCardDetails.user_address?.street_name.toString()
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.address3),
                                value = panCardDetails.user_address?.line_2.toString()
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.city),
                                value = panCardDetails.user_address?.city.toString()
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.state),
                                value = panCardDetails.user_address?.state.toString()
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.country),
                                value = panCardDetails.user_address?.country.toString()
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.pin),
                                value = panCardDetails.user_address?.zip.toString()
                            )
                        )
                    }
                    else if(isFromVerifyBank) {
                        val bankDetails = bankDetails[0]
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.beneficiary_name),
                                value = bankDetails.BeneName.toString()
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.bank_reference_no),
                                value = bankDetails.BankRef.toString()
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.remark),
                                value = bankDetails.Remark.toString()
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.status),
                                value = bankDetails.Status.toString()
                            )
                        )
                        profileDetailList.add(
                            ProfileDetailsModel(
                                title = context.getString(R.string.transaction_time),
                                value = bankDetails.TransactionTime.toString()
                            )
                        )

                }
                    else {
                        if (profileDetailsList.size != 0) {
                            val data = profileDetailsList[0]
                            profileDetailList.add(
                                ProfileDetailsModel(
                                    title = context.getString(R.string.client),
                                    value = data.clientName.toString()
                                )
                            )
                            profileDetailList.add(
                                ProfileDetailsModel(
                                    title = context.getString(R.string.innov_id),
                                    value = data.innovID.toString()
                                )
                            )
                            profileDetailList.add(
                                ProfileDetailsModel(
                                    title = context.getString(R.string.employee_id),
                                    value = data.gNETAssociateID.toString()
                                )
                            )
                            profileDetailList.add(
                                ProfileDetailsModel(
                                    title = context.getString(R.string.aadhaar_no),
                                    value = data.adharCard.toString()
                                )
                            )
                            profileDetailList.add(
                                ProfileDetailsModel(
                                    title = context.getString(R.string.date_of_birth),
                                    value = AppUtils.INSTANCE?.convertDateFormat(
                                        "dd-MMM-yyyy",
                                        data.dob.toString(),
                                        "dd MMM yyyy"
                                    ).toString()
                                )
                            )
                            profileDetailList.add(
                                ProfileDetailsModel(
                                    title = context.getString(R.string.mobile_no),
                                    value = data.mobile.toString()
                                )
                            )
                            profileDetailList.add(
                                ProfileDetailsModel(
                                    title = context.getString(R.string.email_id),
                                    value = data.email.toString()
                                )
                            )
                            profileDetailList.add(
                                ProfileDetailsModel(
                                    title = context.getString(R.string.gender),
                                    value = if (data.gender.toString()
                                            .lowercase() == "f"
                                    ) context.getString(R.string.female) else if (data.gender.toString()
                                            .lowercase() == "m"
                                    ) context.getString(R.string.male) else context.getString(R.string.other)
                                )
                            )
                            profileDetailList.add(
                                ProfileDetailsModel(
                                    title = context.getString(R.string.marital_status),
                                    value = data.maritalStatus.toString()
                                )
                            )
                            profileDetailList.add(
                                ProfileDetailsModel(
                                    title = context.getString(R.string.blood_group),
                                    value = data.bloodGroup.toString()
                                )
                            )
                            profileDetailList.add(
                                ProfileDetailsModel(
                                    title = context.getString(R.string.address),
                                    value = data.permanentAddress1.toString() + "," + data.permanentAddress2 + ",\n" + data.permanentAddress3
                                )
                            )
                            profileDetailList.add(
                                ProfileDetailsModel(
                                    title = context.getString(R.string.city),
                                    value = data.PermanentCityName.toString()
                                )
                            )
                            profileDetailList.add(
                                ProfileDetailsModel(
                                    title = context.getString(R.string.state),
                                    value = data.PermanentStateName.toString()
                                )
                            )
                            profileDetailList.add(
                                ProfileDetailsModel(
                                    title = context.getString(R.string.pin_code),
                                    value = data.PermanentAddressPIN.toString()
                                )
                            )
                            profileDetailList.add(
                                ProfileDetailsModel(
                                    title = context.getString(R.string.uan_number),
                                    value = data.newUANNo.toString()
                                )
                            )
                            profileDetailList.add(
                                ProfileDetailsModel(
                                    title = context.getString(R.string.esic_number),
                                    value = data.newESICNo.toString()
                                )
                            )
                            profileDetailList.add(
                                ProfileDetailsModel(
                                    title = context.getString(R.string.pf_no),
                                    value = data.newPFNo.toString()
                                )
                            )
                        }
                    }
                    val adapter = MyProfileDetailsAdapter(context, profileDetailList)
                    recyclerProfileDetails.adapter = adapter
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].summaryDetailsType.value
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ProfileClickManager {
        fun onEditBtnClick(position: Int)
    }

}