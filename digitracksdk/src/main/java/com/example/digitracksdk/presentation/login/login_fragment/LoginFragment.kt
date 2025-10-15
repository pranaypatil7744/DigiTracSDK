package com.example.digitracksdk.presentation.login.login_fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.databinding.FragmentLoginBinding
import com.example.digitracksdk.domain.model.login_model.LoginRequestModel
import com.example.digitracksdk.presentation.login.LoginActivity
import com.example.digitracksdk.presentation.signup.SignUpActivity
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.InnovSingleton
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModel()

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpListener()
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            btnBack.apply {
                setOnClickListener {
                    (requireActivity() as LoginActivity).finish()
                }
            }
        }
    }

    private fun setUpListener() {
        binding.btnLogin.setOnClickListener {
            if (isValidateMobileNO()) {
                clearLabel()
                callLoginApi()

            } else {
                binding.layoutPhoneEmail.error =
                    getString(R.string.please_enter_valid_phone)
            }
        }
        binding.tvDontHaveAnAccount.setOnClickListener {
            startSignUpScreen()

        }
        binding.tvRegister.setOnClickListener {
            startSignUpScreen()
        }
    }

    private fun startSignUpScreen() {
        startActivity(Intent(requireActivity(), SignUpActivity::class.java))
    }

    private fun clearLabel() {
        binding.layoutPhoneEmail.error = ""
    }

//    private fun startVerifyOtpScreen(){
//        val verifyOtpFragment = (context as LoginActivity).verifyOtpFragment
//        verifyOtpFragment.phoneOrEmail = binding.etEmailMob.text.toString().trim()
//        (context as LoginActivity).setFragment(verifyOtpFragment)
//    }

    private fun callLoginApi() {


        if (this.context?.isNetworkAvailable() == true) {
            loginViewModel.callLoginApi(getLoginMobileRequestModel())
        } else {
            showToast(getString(R.string.no_internet_connection))
        }

        with(loginViewModel) {
            loginResponseData.observe(viewLifecycleOwner, Observer {
                if (it.CandidateStatus.equals(Constant.Valid)) {
                    clearLabel()
//                    startVerifyOtpScreen()

                } else {
                    binding.layoutPhoneEmail.error = getString(R.string.not_a_register_number)
                }
            })

            messageData.observe(viewLifecycleOwner, Observer {
                showToast(it)
            })
            showProgressbar.observe(requireActivity()) {
                toggleLoader(it)
            }
        }
    }

    private fun toggleLoader(showLoader: Boolean) {
        (context as LoginActivity).toggleLoader(showLoader)
    }

    private fun getLoginMobileRequestModel(): LoginRequestModel {
        val request = LoginRequestModel()
        request.APKVersion = InnovSingleton.singleton.getVersionInfo(this.requireActivity())
        request.AndroidVersion = InnovSingleton.singleton.androidVersion
        request.BuildNo = InnovSingleton.singleton.osBuildNumber
        request.EmployeeCode = ""
        request.Mobile = binding.etEmailMob.text.toString().trim()
        request.ModelNo = InnovSingleton.singleton.deviceName
        request.SignupSource = "D"
        return request
    }

    private fun isValidateMobileNO(): Boolean {
        val mobileNo = binding.etEmailMob.text.toString().trim()
        return !(mobileNo.isEmpty() || mobileNo.length < 10)
    }

    fun showToast(msg: String) {
        AppUtils.INSTANCE?.showLongToast(this.requireActivity(), msg)
    }

}