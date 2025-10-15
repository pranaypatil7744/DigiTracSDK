package com.example.digitracksdk.presentation.home.notification

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityNotificationBinding
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.notification.ListNotificationsModel
import com.example.digitracksdk.domain.model.notification.NotificationDetailsRequestModel
import com.example.digitracksdk.presentation.home.notification.adapter.NotificationAdapter
import com.example.digitracksdk.presentation.image_view.ImageViewActivity
import com.example.digitracksdk.presentation.onboarding.document.DocumentDetailsActivity.Companion.viewImage
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class NotificationActivity : BaseActivity(), NotificationAdapter.NotificationClickManager {
    lateinit var binding: ActivityNotificationBinding
    lateinit var notificationAdapter: NotificationAdapter
    private val notificationViewModel: NotificationViewModel by viewModel()
    lateinit var preferenceUtils: PreferenceUtils
    private val notificationList: ArrayList<ListNotificationsModel> = ArrayList()
    var innovId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        getPreferenceData()
        setUpNotificationAdapter()
        setObserver()
        callNotificationListApi()
        setUpToolbar()
        setUpListener()
    }

    private fun setObserver() {
        binding.apply {
            with(notificationViewModel) {

                notificationListResponseData.observe(this@NotificationActivity
                ) { it ->
                    if (it.status == Constant.success) {
                        if (!it.lstMobileNotifications.isNullOrEmpty()) {
                            showNoDataLayout(false)
                            notificationList.clear()
                            notificationList.addAll(it.lstMobileNotifications ?: arrayListOf())
                            notificationAdapter.notifyDataSetChanged()
                        } else {
                            showNoDataLayout(true)
                            layoutNoData.apply {
                                tvNoData.text = it.Message
                            }
                        }
                    } else {
                        showNoDataLayout(true)
                        showToast(it.Message.toString())
                    }
                }

                notificationDetailsResponseData.observe(this@NotificationActivity
                ) { it ->
                    if (!it.NotificationImageArr.isNullOrEmpty()) {
                        val i = Intent(this@NotificationActivity, ImageViewActivity::class.java)
                        val b = Bundle()
                        viewImage = it.NotificationImageArr.toString()
                        b.putString(Constant.SCREEN_NAME, getString(R.string.notification))
//                            b.putString(Constant.IMAGE_PATH,it.NotificationImageArr)
                        b.putBoolean(Constant.IS_IMAGE, true)
                        i.putExtras(b)
                        startActivity(i)
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                messageData.observe(this@NotificationActivity) {
                    showToast(it)
                }

                showProgress.observe(this@NotificationActivity) {
                    toggleLoader(it)
                }
            }
        }
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callNotificationListApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing=false
                callNotificationListApi()
            }
        }
    }

    private fun getPreferenceData() {
        innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
    }

    private fun callNotificationListApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                notificationViewModel.callNotificationListApi(request = CommonRequestModel(InnovId = innovId))
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerNotification.visibility= GONE
            }
            layoutNoData.root.visibility= GONE
        }
    }

    private fun setUpNotificationAdapter() {
        notificationAdapter = NotificationAdapter(this, listener = this,notificationList = notificationList)
        binding.recyclerNotification.adapter = notificationAdapter
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            btnBack.setOnClickListener {
                finish()
            }
            tvTitle.text = getString(R.string.notification)
            divider.visibility = VISIBLE
        }
    }

    override fun clickOnItem(position: Int) {
        callViewNotificationApi(notificationList[position].NotificationId?:0)
    }

    private fun callViewNotificationApi(notificationId:Int) {
        if (isNetworkAvailable()){
            notificationViewModel.callNotificationDetailsApi(NotificationDetailsRequestModel(NotificationId = notificationId))
        }else{
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
    private fun showNoDataLayout(show: Boolean) {
        binding.apply {
            noDataLayout(
                noDataLayout = layoutNoData.root, recyclerView = recyclerNotification, show = show
            )
        }
    }
}