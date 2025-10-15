package com.example.digitracksdk.presentation.walkthrough

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivitySplashScreenBinding
import com.example.digitracksdk.presentation.login.login_fragment.LoginViewModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.ImageUtils
import com.example.digitracksdk.utils.PermissionUtils.Companion.getNotificationPermission
import com.example.digitracksdk.utils.PermissionUtils.Companion.requestNotificationPermissions
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : BaseActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    private val loginViewModel: LoginViewModel by viewModel()
    private lateinit var appUpdateManager: AppUpdateManager
    private val appUpdateRequestCode = 1
    private var isLogin: Boolean = false
    lateinit var preferenceUtils: PreferenceUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        preferenceUtils = PreferenceUtils(this)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        appUpdateManager = AppUpdateManagerFactory.create(this)
        isLogin = preferenceUtils.getValue(Constant.PreferenceKeys.IS_LOGIN, false)
        setUpView()
        setUpObserver()
    }

    private fun setUpView() {
        binding.apply {
            ImageUtils.INSTANCE?.loadLocalGIFImage(imgInnovLogo, R.drawable.digitrac_logo_gif)
        }
    }


    private fun setUpObserver() {
        binding.apply {
            with(loginViewModel) {
                checkAppVersionResponseData.observe(this@SplashScreenActivity) {
                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        val manager = this@SplashScreenActivity.packageManager
                        val info = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            manager.getPackageInfo(
                                this@SplashScreenActivity.packageName,
                                PackageManager.PackageInfoFlags.of(0)
                            )
                        } else {
                            manager.getPackageInfo(
                                this@SplashScreenActivity.packageName,
                                PackageManager.GET_ACTIVITIES
                            )
                        }
                        val appVersion = info.versionName
                        Handler(mainLooper).postDelayed({
                            if ((appVersion?.toDoubleOrNull()
                                    ?: 0.0) < (it.DigitracVersion?.toDoubleOrNull() ?: 0.0)
                            ) {
                                checkForUpdate()
                            } else {
                                if (isLogin) {
                                    startActivity(
                                        Intent(
                                            this@SplashScreenActivity,
                                            com.example.digitracksdk.presentation.home.HomeActivity::class.java
                                        )
                                    )
                                } else {
                                    startActivity(
                                        Intent(
                                            this@SplashScreenActivity,
                                            com.example.digitracksdk.presentation.walkthrough.WalkThroughActivity::class.java
                                        )
                                    )
                                }
                                finish()
                            }
                        }, 5000)
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(this@SplashScreenActivity) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }
        }
    }

    private fun checkForUpdate() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            // Checks that the platform will allow the specified type of update.
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo, AppUpdateType.IMMEDIATE, this, appUpdateRequestCode
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (getNotificationPermission(this@SplashScreenActivity)) {
            callCheckAppVersionApi()
        } else {
            requestNotificationPermissions(this@SplashScreenActivity)
        }

    }

    private fun callCheckAppVersionApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                loginViewModel.callCheckAppVersionApi()
            } else {
                showToast(getString(R.string.no_internet_connection))
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
}