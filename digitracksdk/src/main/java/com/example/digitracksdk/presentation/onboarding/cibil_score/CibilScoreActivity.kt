package com.example.digitracksdk.presentation.onboarding.cibil_score

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.example.digitracksdk.Constant
import com.innov.digitrac.base.BaseActivity
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import com.innov.digitrac.R
import com.innov.digitrac.databinding.ActivityCibilScoreBinding
import com.example.digitracksdk.domain.model.onboarding.cibil_score.GetCibilScoreRequestModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.DialogUtils
import org.koin.android.viewmodel.ext.android.viewModel

class CibilScoreActivity : BaseActivity(), ValidationListener,
    DialogUtils.DialogManager {
    lateinit var binding: ActivityCibilScoreBinding
    var isView: Boolean = false

    lateinit var preferenceUtils: PreferenceUtils
    private var innovId = ""
    var mobile: String? = ""

    private val cibilScoreViewModel: CibilScoreViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCibilScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        preferenceUtils = PreferenceUtils(this)
        cibilScoreViewModel.listener = this
        getPreferenceData()
        getIntentData()
        setUpToolbar()
        setUpObserverData()
        setUpListener()
    }

    private fun getIntentData() {
        binding.apply {
            intent?.extras?.run {
                isView = getBoolean(Constant.IS_VIEW, false)
            }
        }
    }


    private fun getPreferenceData() {
        mobile = preferenceUtils.getValue(Constant.PreferenceKeys.MobileNo)
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
    }

    private fun setUpObserverData() {
        binding.apply {
            with(cibilScoreViewModel) {

                cibilScoreResponseModel.observe(
                    this@CibilScoreActivity
                ) {
                    if (it.Status?.lowercase() == Constant.success && !it.CibilScore.isNullOrEmpty()) {
                        openDialogBox(it.Message.toString())

                    } else {
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(this@CibilScoreActivity) {
                    showToast(it.toString())
                }

                showProgressBar.observe(this@CibilScoreActivity) {
                    toggleLoader(it)
                }
            }

        }
    }

    private fun openDialogBox(score: String) {
        DialogUtils.showLogOutDialog(
            this@CibilScoreActivity,
            listener = this@CibilScoreActivity,
            msg = score,
            title = getString(R.string.cibil_score),
            confirm = (getString(R.string.dismiss)).replace("d","D"),
            isCloseHide = true
        )
    }


    private fun getCibilScoreRequestModel(): GetCibilScoreRequestModel {
        return GetCibilScoreRequestModel(
            PanCard = binding.etPanNumber.text.toString().trim().uppercase(),
            InnovId = innovId
        )

    }


    private fun setUpListener() {
        binding.apply {
            toolbar.tvLeftTitle1.setOnClickListener {
                clearError()
                hideKeyboard(this@CibilScoreActivity)
                cibilScoreViewModel.verifyCibilDetails(
                    getCibilScoreRequestModel()
                )
            }
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.cibil_score)
            divider.visibility = View.VISIBLE
            tvLeftTitle1.text = getString(R.string.save)
            tvLeftTitle1.visibility = View.VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
            tvLeftTitle1.setTextColor(
                ContextCompat
                    .getColor(this@CibilScoreActivity, R.color.blue_ribbon)
            )
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
        if (binding.checkAck.isChecked)
            callGetCibilScoreApi()
        else
            showToast(getString(R.string.please_select_term_condition))
    }

    private fun callGetCibilScoreApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                cibilScoreViewModel.callGetCibilScoreApi(
                    getCibilScoreRequestModel()
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

    override fun onValidationFailure(type: String, msg: Int) {
        binding.apply {
            when (type) {
                Constant.ListenerConstants.PAN_CARD_ERROR -> {
                    layoutPanCardNumber.error = getString(msg)
                }
            }
        }
    }

    private fun clearError() {
        binding.apply {
            layoutPanCardNumber.error = ""
        }
    }


}