package com.example.digitracksdk.presentation.home.jobs_and_refer_fds.refer_a_friend

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.domain.model.refer_a_friend.ReferralFriendRequestModel
import com.example.digitracksdk.domain.model.refer_a_friend.SkillListModel
import com.example.digitracksdk.Constant
import com.example.digitracksdk.domain.model.refer_a_friend.BranchDetailsRequestModel
import com.example.digitracksdk.domain.model.refer_a_friend.BranchListModel
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityReferAfriendBinding
import com.innov.digitrac.domain.model.refer_a_friend.*
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class ReferAFriendActivity : BaseActivity(), ValidationListener {
    lateinit var binding: ActivityReferAfriendBinding
    lateinit var preferenceUtils: PreferenceUtils
    private val referFriendViewModel: ReferFriendViewModel by viewModel()
    private val branchList: ArrayList<BranchListModel> = ArrayList()
    private val skillList: ArrayList<SkillListModel> = ArrayList()
    var skill: String = ""
    var skillId: Int = 0
    var branchId: Int = 0
    var clientRequirementID: String = ""
    var gender:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReferAfriendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        preferenceUtils = PreferenceUtils(this)
        referFriendViewModel.validationListener = this
        setUpToolbar()
        getIntentData()
        setObserver()
        setUpDropDownData()
        setUpListener()
    }

    private fun setObserver() {
        binding.apply {
            with(referFriendViewModel) {
                branchListResponseData.observe(this@ReferAFriendActivity) {
                    if (it.LstBranchDetails?.size != 0) {
                        branchList.clear()
                        it.LstBranchDetails?.let { it1 -> branchList.addAll(it1) }
                        val list: ArrayList<String> = ArrayList()
                        list.clear()
                        for (i in branchList) {
                            list.add(i.FacilityName.toString())
                        }
                        val adapter = ArrayAdapter(
                            this@ReferAFriendActivity,
                            android.R.layout.simple_list_item_1, list
                        )
                        binding.etBranchName.setAdapter(adapter)
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                skillListResponseData.observe(this@ReferAFriendActivity) {
                    if (it.lstSkillset?.size != 0) {
                        skillList.clear()
                        it.lstSkillset?.let { it1 -> skillList.addAll(it1) }
                        val list: ArrayList<String> = ArrayList()
                        list.clear()
                        for (i in skillList) {
                            list.add(i.SkillName.toString())
                        }
                        val adapter = ArrayAdapter(
                            this@ReferAFriendActivity,
                            android.R.layout.simple_list_item_1, list
                        )
                        binding.etSkill.setAdapter(adapter)
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                referralFriendResponseData.observe(this@ReferAFriendActivity) {
                    if (it.status == Constant.SUCCESS) {
                        showToast(it.Message.toString())
                        finish()
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(this@ReferAFriendActivity) {
                    showToast(it)
                }
                showProgress.observe(this@ReferAFriendActivity) {
                    toggleLoader(it)
                }
            }
        }
    }

    private fun getIntentData() {
        intent.extras?.run {
            clientRequirementID = getString(Constant.CLIENT_REQUIREMENT_ID).toString()
        }
    }

    private fun getReferralFriendRequestModel(): ReferralFriendRequestModel {
        val request = ReferralFriendRequestModel()
        request.FirstName = binding.etCandidate.text.toString().trim()
        request.Skill = skill
        request.SkillId = skillId
        request.AdharCard = binding.etAadharCard.text.toString().trim()
        request.ClientrequirementID = clientRequirementID
        request.EmailID = binding.etCandidateEmail.text.toString().trim()
        request.BranchId = branchId
        request.Gender = gender
        request.Location = binding.etCandidateLocation.text.toString().trim()
        request.RefferedByInnovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
        request.MobileNo = binding.etCandidatePhone.text.toString().trim()
        return request
    }

    private fun setUpListener() {
        binding.apply {
            btnSubmit.setOnClickListener {
                referFriendViewModel.validateReferralFriend(getReferralFriendRequestModel())
            }
            etBranchName.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    branchId = branchList[position].FacilityID ?: 0
                }
            etSkill.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    skill = skillList[position].SkillName.toString()
                    skillId = skillList[position].SkillSetID ?: 0
                }
            radioMale.setOnClickListener {
                gender = Constant.Male
            }
            radioFemale.setOnClickListener {
                gender = Constant.Female
            }
        }
    }

    private fun setUpDropDownData() {
        if (isNetworkAvailable()) {
            callBranchDetailsListApi()
            callSkillListApi()
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    private fun callBranchDetailsListApi() {
        referFriendViewModel.callBranchDetailsListApi(
            request = BranchDetailsRequestModel(
                ""
            )
        )
    }

    private fun callSkillListApi() {
        referFriendViewModel.callSkillListApi()
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.refer_a_friend)
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = View.VISIBLE
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

    override fun onValidationSuccess(type: String, msg: Int) {
        clearError()
        callReferralFriendApi()
    }

    private fun callReferralFriendApi() {
        if (isNetworkAvailable()) {
            referFriendViewModel.callReferralFriendApi(getReferralFriendRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun onValidationFailure(type: String, msg: Int) {
        clearError()
        when (type) {
            Constant.ListenerConstants.NAME_ERROR -> {
                binding.layoutCandidate.error = getString(msg)
            }
            Constant.ListenerConstants.GENDER_ERROR ->{
                showToast(getString(msg))
            }
            Constant.ListenerConstants.SKILL_ERROR -> {
                binding.layoutSkill.error = getString(msg)
            }
            Constant.ListenerConstants.LOCATION_ERROR -> {
                binding.layoutCandidateLocation.error = getString(msg)
            }
            Constant.ListenerConstants.PHONE_ERROR -> {
                binding.layoutCandidatePhone.error = getString(msg)
            }
            Constant.ListenerConstants.CANDIDATE_EMAIL_ERROR -> {
                binding.layoutCandidateEmail.error = getString(msg)
            }
            Constant.ListenerConstants.BRANCH_ERROR -> {
                binding.layoutBranchName.error = getString(msg)
            }
            Constant.ListenerConstants.AADHAR_NUMBER_ERROR -> {
                binding.layoutAadharCard.error = getString(msg)
            }
        }
    }

    private fun clearError() {
        binding.apply {
            layoutCandidate.error = ""
            layoutSkill.error = ""
            layoutAadharCard.error = ""
            layoutBranchName.error = ""
            layoutCandidateEmail.error = ""
            layoutCandidateLocation.error = ""
            layoutCandidatePhone.error = ""
        }
    }
}