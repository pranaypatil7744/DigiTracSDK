package com.example.digitracksdk.presentation.home.rewards

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityRewardsBinding
import com.example.digitracksdk.domain.model.rewards.AssociateRewardDetail
import com.example.digitracksdk.domain.model.rewards.RewardRequestModel
import com.example.digitracksdk.presentation.home.rewards.adapter.RewardsAdapter
import com.example.digitracksdk.presentation.image_view.ImageViewActivity
import com.example.digitracksdk.presentation.web_view.WebViewActivity
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class RewardsActivity : BaseActivity(), RewardsAdapter.RewardClickManager {
    lateinit var binding: ActivityRewardsBinding
    lateinit var preferenceUtils: PreferenceUtils
    lateinit var adapter: RewardsAdapter
    private val rewardsList: ArrayList<AssociateRewardDetail> = ArrayList()
    private val rewardsViewModel: RewardsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
         binding = ActivityRewardsBinding.inflate(layoutInflater)
          setContentView(binding.root)
          AppUtils.INSTANCE?.systemBarPadding(binding.root)
          preferenceUtils = PreferenceUtils(this)
          setUpToolbar()
          setObserver()
          setUpAdapter()
          callRewardsApi()
          setUpListener()

       /* setContent {
            RewardsScreen()
        }*/
    }



    private fun setUpListener() {

        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callRewardsApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                callRewardsApi()
            }
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
            tvTitle.text = getString(R.string.rewards)

        }
    }

    private fun setObserver() {
        with(rewardsViewModel) {
            rewardsResponseData.observe(this@RewardsActivity) {
                toggleLoader(false)

                if (it.Status?.lowercase() == Constant.success) {
                    if (!it.associateRewardDetail.isNullOrEmpty()) {
                        showNoDataLayout(false)
                        rewardsList.clear()
                        rewardsList.addAll(it.associateRewardDetail)
                        adapter.notifyDataSetChanged()
                    } else {
                        showNoDataLayout(true)
                        binding.layoutNoData.tvNoData.text = it.Message
                    }
                } else {
                    showNoDataLayout(true)
                    showToast(it.Message.toString())
                }

            }

            messageData.observe(this@RewardsActivity) {
                toggleLoader(false)
                showToast(it)
            }

        }

    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            loader = binding.contentLoading.root,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    private fun showNoDataLayout(show: Boolean) {
        binding.apply {
            noDataLayout(
                noDataLayout = layoutNoData.root, recyclerView = recycleReward, show = show
            )
        }
    }

    private fun setUpAdapter() {
        if (::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
        } else {
            adapter = RewardsAdapter(this, rewardsList, this@RewardsActivity)
            binding.recycleReward.adapter = adapter
        }

    }

    private fun callRewardsApi() {
        with(binding)
        {
            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                toggleLoader(true)
                rewardsViewModel.callRewardsApi(
                    request = RewardRequestModel(
                        GNETAssociateID = preferenceUtils.getValue(com.example.digitracksdk.Constant.PreferenceKeys.GnetAssociateID)
                    )
                )

            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recycleReward.visibility = GONE
            }
        }


    }

    override fun clickOnViewBtn(position: Int) {

        val url = rewardsList[position].Url
        if (!url.isNullOrBlank()) {

            if (url.toString().lowercase().contains(".png") || url.toString().lowercase()
                    .contains(".jpg") || url.toString().lowercase().contains(".jpeg")
            ) {
                val b = Bundle()
                val i = Intent(this, ImageViewActivity::class.java)
                b.putBoolean(Constant.IS_IMAGE, true)
                b.putString(Constant.SCREEN_NAME, getString(R.string.rewards))
                b.putString(Constant.IMAGE_PATH, url.toString())
                i.putExtras(b)
                startActivity(i)

            } else {
                val i = Intent(this@RewardsActivity, WebViewActivity::class.java)
                val b = Bundle()
                b.putString(Constant.WEB_URL, url)
                b.putString(Constant.SCREEN_NAME, getString(R.string.rewards))
                i.putExtras(b)
                startActivity(i)
            }
        } else {
            showToast(getString(R.string.something_went_wrong))
        }
    }


}