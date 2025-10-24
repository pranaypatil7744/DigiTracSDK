package com.example.digitracksdk.presentation.my_profile.view_profile

import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityMyProfileBinding
import com.example.digitracksdk.domain.model.home_model.request.InnovIDCardRequestModel
import com.example.digitracksdk.domain.model.home_model.response.InnovIDCardResponseModel
import com.example.digitracksdk.presentation.my_profile.create_edit_profile.CreateEditProfileActivity
import com.example.digitracksdk.presentation.home.innov_id_card.InnovIDCardViewModel
import com.example.digitracksdk.presentation.login.LoginActivity
import com.example.digitracksdk.presentation.my_profile.adapter.MyProfileAdapter
import com.example.digitracksdk.presentation.my_profile.model.ProfileModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.DialogUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class MyProfileActivity : BaseActivity(), MyProfileAdapter.ProfileClickManager,
    DialogUtils.DialogManager {

    lateinit var binding: ActivityMyProfileBinding
    private var summaryList: ArrayList<ProfileModel> = ArrayList()
    private var profileDetailsList: ArrayList<InnovIDCardResponseModel> = ArrayList()
    private lateinit var myProfileAdapter: MyProfileAdapter
    private val innovIDCardViewModel: InnovIDCardViewModel by viewModel()
    lateinit var preferenceUtils: PreferenceUtils
    var profilePic:String? = ""
    var mobile:String?= ""
    var innovId :String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        AppUtils.INSTANCE?.setLang(this)
        setUpToolbar()
        setListener()
        setUpSummaryAdapter()
        setUpObserver()
        setUpViewData()
    }

    private fun setUpObserver() {
        binding.apply {
            with(innovIDCardViewModel) {
                innovIDCardResponseData.observe(this@MyProfileActivity) {
                    toggleLoader(false)
                    profileDetailsList.clear()
                    profileDetailsList.add(it)
                    profileDetailsList[0].picture = profilePic
                    profileDetailsList[0].mobile = mobile
                    profileDetailsList[0].innovID = innovId
                    myProfileAdapter.notifyDataSetChanged()
                }

                messageData.observe(this@MyProfileActivity) {
                    toggleLoader(false)
                    showToast(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getPreferenceData()
        callInnovIDCardApi()
    }

    private fun getPreferenceData() {
        profilePic = preferenceUtils.getValue(Constant.PreferenceKeys.PROFILE_PIC)
        mobile = preferenceUtils.getValue(Constant.PreferenceKeys.MobileNo)
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
    }

    private fun callInnovIDCardApi() {
        if (isNetworkAvailable()){
            toggleLoader(true)
            innovIDCardViewModel.callInnovIDCardApi(
                InnovIDCardRequestModel(PreferenceUtils(this).getValue(
                    Constant.PreferenceKeys.INNOV_ID))
            )
        }else{
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun setUpViewData() {

        summaryList.clear()
        summaryList.add(
            ProfileModel(
                summaryDetailsType = MyProfileAdapter.SummaryDetailsType.EMP_PROFILE_DETAILS
            )
        )

        summaryList.add(
            ProfileModel(
                summaryDetailsType = MyProfileAdapter.SummaryDetailsType.EMP_PROFILE_DETAILS_ITEM
            )
        )
    }

    private fun setUpSummaryAdapter() {
        myProfileAdapter =
            MyProfileAdapter(this, summaryList,profileDetailsList,this)
        binding.recyclerMyProfile.adapter = myProfileAdapter
    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(binding.root, loader = binding.contentLoading.root, binding.contentLoading.imageLoading, showLoader)
    }

    private fun setListener() {
        binding.apply {
            toolbar.btnOne.setOnClickListener {
                DialogUtils.showLogOutDialog(this@MyProfileActivity,this@MyProfileActivity,getString(R.string.are_you_sure_you_want_to_logout))
            }
        }
    }

    override fun onOkClick() {
        val intent = Intent(this@MyProfileActivity, LoginActivity::class.java)
        val firebaseToken = preferenceUtils.getValue(Constant.PreferenceKeys.FIREBASE_TOKEN)
        val selectedLang = preferenceUtils.getValue(Constant.SELECTED_LANGUAGE)
        preferenceUtils.clear()
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        preferenceUtils.setValue(Constant.PreferenceKeys.FIREBASE_TOKEN,firebaseToken)
        preferenceUtils.setValue(Constant.SELECTED_LANGUAGE,selectedLang)
        startActivity(intent)
        finish()
    }

    private fun setUpToolbar() {

        binding.toolbar.apply {
            tvTitle.text = getString(R.string.my_profile)
            divider.visibility = VISIBLE
            btnOne.visibility= VISIBLE
            btnOne.setImageResource(R.drawable.ic_logout)

            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    override fun onEditBtnClick(position: Int) {
        // todo remove this is temporary solution
        profileDetailsList[0].picture = ""

        val intent = Intent(this, CreateEditProfileActivity::class.java)
        val b = Bundle()
        b.putBoolean(Constant.IS_FOR_EDIT,true)
        b.putSerializable(Constant.PROFILE_MODEL,profileDetailsList)
        intent.putExtras(b)
        startActivity(intent)
    }
}