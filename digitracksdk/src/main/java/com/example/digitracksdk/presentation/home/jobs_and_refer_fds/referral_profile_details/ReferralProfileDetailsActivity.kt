package com.example.digitracksdk.presentation.home.jobs_and_refer_fds.referral_profile_details

import android.os.Build
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityReferralProfileDetailsBinding
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.LstreferredDetailsItem
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.referral_profile_details.adapter.ReferralProfileDetailsAdapter
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.referral_profile_details.model.ProfileDetailsType
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.referral_profile_details.model.ReferralProfileDetailsModel
import com.example.digitracksdk.utils.AppUtils

class ReferralProfileDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityReferralProfileDetailsBinding
    lateinit var referralProfileDetailsAdapter: ReferralProfileDetailsAdapter
    private var profileDetailsList: ArrayList<ReferralProfileDetailsModel> = ArrayList()
    lateinit var referralData: LstreferredDetailsItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReferralProfileDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        getIntentData()
        setUpToolbar()
        setUpProfileData()
    }

    private fun getIntentData() {
        intent.extras?.run {
            referralData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getSerializable(
                    Constant.REFERRAL_MODEL,
                    LstreferredDetailsItem::class.java
                ) as LstreferredDetailsItem
            } else
                getSerializable(Constant.REFERRAL_MODEL) as LstreferredDetailsItem
        }
    }

    private fun setUpProfileData() {
        profileDetailsList.clear()
        val name = referralData.firstName + referralData.lastName
        //TODO confirm data according to keys
        profileDetailsList.add(
            ReferralProfileDetailsModel(
                name = name,
                designation = referralData.roleProfileApplyingFor,
                department = referralData.skill,
                profilePic = R.drawable.profile_placeholder,
                refDetailsType = ProfileDetailsType.PROFILE
            )
        )
        profileDetailsList.add(
            ReferralProfileDetailsModel(
                refDetailsType = ProfileDetailsType.PROFILE_DETAILS,
                icon1 = R.drawable.ic_email,
                icon2 = R.drawable.ic_call,
                title = getString(R.string.email),
                title2 = getString(R.string.contact_us),
                subTitle = referralData.emailID,
                subTitle2 = referralData.mobileNo
            )
        )
        profileDetailsList.add(
            ReferralProfileDetailsModel(
                refDetailsType = ProfileDetailsType.PROFILE_DETAILS,
                icon1 = R.drawable.ic_location,
                icon2 = R.drawable.ic_applicable_amount,
                title = getString(R.string.location),
                title2 = getString(R.string.applicable_amount),
                subTitle = referralData.location,
                subTitle2 = referralData.applicableAmount
            )
        )
        profileDetailsList.add(
            ReferralProfileDetailsModel(
                refDetailsType = ProfileDetailsType.PROFILE_DETAILS,
                icon1 = R.drawable.ic_aadhar_card,
                icon2 = R.drawable.ic_gendar,
                title2 = getString(R.string.gender),
                title = getString(R.string.aadhaar_card),
                subTitle2 = referralData.gender,
                subTitle = referralData.adharCard
            )
        )
        profileDetailsList.add(
            ReferralProfileDetailsModel(
                refDetailsType = ProfileDetailsType.PROFILE_DETAILS,
                icon1 = R.drawable.ic_status,
                title = getString(R.string.status),
                subTitle = referralData.status
            )
        )

        setUpProfileDetailsAdapter()
    }

    private fun setUpProfileDetailsAdapter() {
        referralProfileDetailsAdapter = ReferralProfileDetailsAdapter(this, profileDetailsList)
        binding.recyclerProfile.adapter = referralProfileDetailsAdapter
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.profile_details)
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
        }
    }
}