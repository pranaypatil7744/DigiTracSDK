package com.example.digitracksdk.presentation.home.exit_questionnaire

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityExitQuestionnaireBinding
import com.example.digitracksdk.domain.model.exit_questionnaire_model.ExitQuestionnaireRequestModel
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.onboarding.epf_details.adapter.DetailAdapter
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailItemType
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class ExitQuestionnaireActivity : BaseActivity(), DetailAdapter.DetailListener, ValidationListener {
    lateinit var binding: ActivityExitQuestionnaireBinding
    private val exitQuestionnaireViewModel: ExitQuestionnaireViewModel by viewModel()
    private lateinit var preferences: PreferenceUtils
    var list: ArrayList<DetailModel> = ArrayList()
    private var questionnaireList: ArrayList<String> = ArrayList()
    lateinit var adapter: DetailAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExitQuestionnaireBinding.inflate(layoutInflater)
        exitQuestionnaireViewModel.validationListener = this
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        preferences = PreferenceUtils(this)
        setUpToolbar()
        setUpRecyclerList()
        setUpAdapter()
        setUpObserver()
        setUpListener()
    }

    private fun setUpRecyclerList() {

        setUpQuestionnaireList()
        list.add(
            DetailModel(
                title = getString(R.string.exit_questionnaire1),
                itemType = DetailItemType.RADIO_GROUP,
                isEnabled = true
            )

        )
        list.add(
            DetailModel(
                title = getString(R.string.exit_questionnaire2),
                itemType = DetailItemType.RADIO_GROUP,
                isEnabled = true
            )

        )
        list.add(
            DetailModel(
                title = getString(R.string.exit_questionnaire3),
                itemType = DetailItemType.RADIO_GROUP,
                isEnabled = true
            )

        )
        list.add(
            DetailModel(
                title = getString(R.string.exit_questionnaire4),
                itemType = DetailItemType.RADIO_GROUP,
                isEnabled = true
            )

        )
        list.add(
            DetailModel(
                title = getString(R.string.exit_questionnaire5),
                itemType = DetailItemType.RADIO_GROUP,
                isEnabled = true
            )

        )

        list.add(
            DetailModel(
                title = getString(R.string.exit_questionnaire6),
                itemType = DetailItemType.EDIT_TEXT_REMARK,
                isEnabled = true,
            )

        )


    }

    private fun setUpQuestionnaireList() {
        questionnaireList.clear()
        questionnaireList.add(getString(R.string.yes))
        questionnaireList.add(getString(R.string.no))
        questionnaireList.add(getString(R.string.cannot_say))
    }

    private fun setUpAdapter() {
        if (::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
        } else {
            adapter = DetailAdapter(
                context = this,
                list = list,
                genderRadioList = questionnaireList,
                listener = this
            )
            binding.recyclerExitQuestionnaire.adapter = adapter
        }
    }

    private fun setUpObserver() {
        with(exitQuestionnaireViewModel)
        {
            exitQuestionnaireResponseData.observe(this@ExitQuestionnaireActivity)
            {
                if (it.status?.lowercase() == Constant.success) {
                    finish()
                } else {
                    showToast(it.Message.toString())
                }
            }
            messageData.observe(this@ExitQuestionnaireActivity)
            {
                showToast(it)
            }
            showProgressBar.observe(this@ExitQuestionnaireActivity)
            {
                toggleLoader(it)
            }
        }

    }

    private fun setUpListener() {
        binding.apply {

            btnSubmit.setOnClickListener {
                exitQuestionnaireViewModel.validateExitQuestionnaire(
                    getExitQuestionnaireRequestModel()
                )
            }

        }

    }

    private fun getExitQuestionnaireRequestModel(): ExitQuestionnaireRequestModel {
        val request = ExitQuestionnaireRequestModel()
        request.ExitQuestionnaire1 = list[0].value.toString()
        request.ExitQuestionnaire2 = list[1].value.toString()
        request.ExitQuestionnaire3 = list[2].value.toString()
        request.ExitQuestionnaire4 = list[3].value.toString()
        request.ExitQuestionnaire5 = list[4].value.toString()
        request.ExitRemark = list[5].value.toString()
        request.GnetAssosiateId = preferences.getValue(Constant.PreferenceKeys.GnetAssociateID)
        return request
    }

    private fun callExitQuestionnaireApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                exitQuestionnaireViewModel.callExitQuestionnaireApi(getExitQuestionnaireRequestModel())
            } else {
                showToast(getString(R.string.no_internet_connection))
            }

        }

    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = View.VISIBLE
            tvTitle.text = getString(R.string.exit_questionnaire)
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
        callExitQuestionnaireApi()
    }

    override fun onValidationFailure(type: String, msg: Int) {
        showToast(getString(msg))
    }


}
