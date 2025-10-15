package com.example.digitracksdk.presentation.home.training

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityTrainingBinding
import com.example.digitracksdk.domain.model.training_model.TrainingRequestModel
import com.example.digitracksdk.presentation.attendance.mileage_tracking.adapter.TabAdapter
import com.example.digitracksdk.presentation.home.training.document_fragment.TrainingDocumentFragment
import com.example.digitracksdk.presentation.home.training.document_fragment.model.TrainingDocumentModel
import com.example.digitracksdk.presentation.home.training.video_fragment.TrainingVideoFragment
import com.example.digitracksdk.presentation.home.training.video_fragment.model.TrainingVideoModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class TrainingActivity : BaseActivity() {
    lateinit var binding: ActivityTrainingBinding
    lateinit var preferenceUtils: PreferenceUtils
    private val trainingViewModel: TrainingViewModel by viewModel()
    private var trainingDocumentFragment: TrainingDocumentFragment =
        TrainingDocumentFragment.newInstance()
    private var trainingVideoFragment: TrainingVideoFragment = TrainingVideoFragment.newInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        preferenceUtils = PreferenceUtils(this)
        setUpToolbar()
        setObserver()
        setUpTabView()
        setUpListener()
        callTrainingApi()
    }

    private fun setObserver() {
        binding.apply {
            with(trainingViewModel) {
                trainingResponseData.observe(
                    this@TrainingActivity
                ) { it ->
                    if (it.status == Constant.success) {
                        if (!it.lstClientTrainingDetails.isNullOrEmpty()) {
                            showNoDataLayout(false)
                            trainingVideoFragment.trainingVideoList.clear()
                            trainingDocumentFragment.trainingDocList.clear()

                            for (i in it.lstClientTrainingDetails ?: arrayListOf()) {
                                trainingVideoFragment.trainingVideoList.add(
                                    TrainingVideoModel(
                                        videoLink = i.VideoLink,
                                        videoImg = i.TrainingTypeImageArr
                                    )
                                )
                                trainingDocumentFragment.trainingDocList.add(
                                    TrainingDocumentModel(
                                        docType = i.ClientTrainingType,
                                        docDetails = i.ClientName,
                                        docName = i.ClientTrainingName,
                                        docIcon = i.TrainingTypeImageArr,
                                        docLink = i.TrainingTypeFilePath,
                                        clientTrainingID = i.ClientTrainingID
                                    )
                                )
                            }
                            trainingDocumentFragment.setUpTrainingAdapter()
                            trainingVideoFragment.setUpTrainingAdapter()

                        } else {
                            showNoDataLayout(true)
                        }
                    } else {
                        showToast(it.Message.toString())
                    }
                }

                trainingMessage.observe(this@TrainingActivity) {
                    if (it != Constant.UNKNOWN_ERROR_MESSAGE) {
                        showToast(it)
                    }
                }

                showProgressBar.observe(this@TrainingActivity) {
                    toggleLoader(it)
                }
            }
        }
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callTrainingApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                callTrainingApi()
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        callTrainingApi()
    }

    private fun callTrainingApi() {
        with(binding) {
            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                pagerTraining.visibility = VISIBLE
                trainingViewModel.callTrainingApi(
                    request = TrainingRequestModel(
                        InnovId = preferenceUtils.getValue(com.example.digitracksdk.Constant.PreferenceKeys.INNOV_ID)
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                pagerTraining.visibility = GONE
            }
            layoutNoData.root.visibility = GONE
        }
    }

    private fun setUpTabView() {
        val fragmentList: ArrayList<Fragment> = ArrayList()
        val titleList: ArrayList<String> = ArrayList()

        titleList.clear()
        titleList.add(getString(R.string.document))
        titleList.add(getString(R.string.videos))
        fragmentList.add(trainingDocumentFragment)
        fragmentList.add(trainingVideoFragment)
        binding.pagerTraining.adapter = TabAdapter(this, fragmentList)
        TabLayoutMediator(binding.tabLayout, binding.pagerTraining) { tab, position ->
            tab.text = titleList[position]
        }.attach()
        binding.pagerTraining.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            btnBack.setOnClickListener {
                finish()
            }
            tvTitle.text = getString(R.string.training)
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
                noDataLayout = layoutNoData.root, viewPager2 = pagerTraining, show = show
            )
        }
    }
}