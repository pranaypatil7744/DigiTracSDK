package com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.base.BaseFragment
import com.example.digitracksdk.databinding.FragmentJobsBinding
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.JobsAndReferFdsActivity
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.JobsAndReferFriendViewModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.job_details_screen.JobDetailsActivity
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.adapter.JobsAdapter
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.JobReferralRequestModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.LstOpenDemandForCandidatesItem
import com.example.digitracksdk.utils.LocationUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList


class JobsFragment : BaseFragment(), JobsAdapter.JobsClickManager {

    lateinit var binding: FragmentJobsBinding
    private lateinit var jobsAdapter: JobsAdapter
    var latitude: String? = ""
    var longitude: String? = ""
    private var jobsList: ArrayList<LstOpenDemandForCandidatesItem> = ArrayList()
    private var fixJobsList: ArrayList<LstOpenDemandForCandidatesItem> = ArrayList()
    private val jobsAndReferFriendViewModel: JobsAndReferFriendViewModel by viewModel()
    private lateinit var preferenceUtils: PreferenceUtils

    companion object {
        fun newInstance(): JobsFragment {
            return JobsFragment()
        }
    }

    private val locationUtil =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                latitude = result.data?.getStringExtra(Constant.IntentExtras.EXTRA_LATITUDE)
                longitude =
                    result.data?.getStringExtra(Constant.IntentExtras.EXTRA_LONGITUDE)
                callJobsListApi(latitude.toString(), longitude.toString())
            }
        }

    private fun callJobsListApi(latitude: String, longitude: String) {
        with(binding){
            if (requireContext().isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                toggleLoader(true)
                jobsAndReferFriendViewModel.callJobReferralListApi(
                    JobReferralRequestModel(
                        preferenceUtils.getValue(com.example.digitracksdk.Constant.PreferenceKeys.INNOV_ID),
                        latitude, longitude
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerJobs.visibility= GONE
            }
            layoutNoData.root.visibility= GONE
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        preferenceUtils = PreferenceUtils(requireContext())
        binding = FragmentJobsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpJobsAdapter()
        setUpListener()
        setObserver()
        locationUtil.launch(Intent(context, LocationUtils::class.java))
    }

    private fun setObserver() {
        binding.apply {
            with(jobsAndReferFriendViewModel) {
                jobReferralResponseData.observe(requireActivity()) {
                    toggleLoader(false)
                    if (!it.lstOpenDemandForCandidates.isNullOrEmpty()) {
                        showNoDataLayout(false)
                        jobsList.clear()
                        fixJobsList.clear()
                        jobsList.addAll(it.lstOpenDemandForCandidates)
                        fixJobsList.addAll(jobsList)
                        jobsAdapter.notifyDataSetChanged()
                    } else {
                        showNoDataLayout(true)
                    }
                }

                messageData.observe(requireActivity()) {
                    toggleLoader(false)
                    showToast(it)
                }
            }
        }
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                if (latitude.isNullOrEmpty() && longitude.isNullOrEmpty()) {
                    locationUtil.launch(Intent(context, LocationUtils::class.java))
                } else {
                    callJobsListApi(latitude.toString(), longitude.toString())
                }
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing=false
                if (latitude.isNullOrEmpty() && longitude.isNullOrEmpty()) {
                    locationUtil.launch(Intent(context, LocationUtils::class.java))
                } else {
                    callJobsListApi(latitude.toString(), longitude.toString())
                }
            }
        }

        (context as JobsAndReferFdsActivity).binding.apply {
            toolbar.apply {
                btnOne.setOnClickListener {
                    (context as JobsAndReferFdsActivity).showSearchBar()
                }
                btnClose.setOnClickListener {
                    (context as JobsAndReferFdsActivity).hideSearchBar()
                }
                etSearch.doOnTextChanged { text, start, before, count ->
                    if (count >= 1) {
                        val searchList = jobSearch(text.toString(), fixJobsList)
                        jobsList.clear()
                        jobsList.addAll(searchList)
                        if (searchList.isEmpty()) {
                            if (context?.isNetworkAvailable() == true){
                                binding.layoutNoData.root.visibility = VISIBLE
                            }
                        } else {
                            binding.layoutNoData.root.visibility = GONE
                        }
                        jobsAdapter.notifyDataSetChanged()
                    } else {
                        jobsList.clear()
                        jobsList.addAll(fixJobsList)
                        binding.layoutNoData.root.visibility = GONE
                        jobsAdapter.notifyDataSetChanged()
                    }
                }
            }

        }
    }

    private fun jobSearch(
        searchText: String,
        jobList: ArrayList<LstOpenDemandForCandidatesItem>
    ): ArrayList<LstOpenDemandForCandidatesItem> {
        val list = jobList.filter {
            it.designation?.lowercase(Locale.ROOT)?.contains(
                searchText, true
            )?.or(
                it.locationName?.lowercase(Locale.ROOT)?.contains(searchText, true) == true
            )?.or(
                it.salaryRange?.lowercase(Locale.ROOT)?.contains(searchText) == true
            ) == true
        }
        return list as ArrayList<LstOpenDemandForCandidatesItem>
    }

    private fun setUpJobsAdapter() {
        jobsAdapter = JobsAdapter(this.requireContext(), jobsList, this)
        binding.recyclerJobs.adapter = jobsAdapter
    }

    override fun onClickReferFriendBtn(position: Int) {
        val data = jobsList[position]
        val i = Intent(this.requireContext(), JobDetailsActivity::class.java)
        val b = Bundle()
        b.putSerializable(Constant.JOB_MODEL, data)
        i.putExtras(b)
        startActivity(i)
    }

    override fun onClickViewPolicy(position: Int) {
        val data = jobsList[position]
//        val i = Intent(this.requireContext(), WebViewActivity::class.java)
//        val b = Bundle()
//        b.putString(Constant.SCREEN_NAME, getString(R.string.view_policy))
//        b.putBoolean(Constant.IS_PDF, true)
//        b.putString(Constant.WEB_URL, data.policyPathUrl)
//        i.putExtras(b)
//        startActivity(i)
        if(!data.policyPathUrl.isNullOrEmpty()) {
            val i = Intent(Intent.ACTION_VIEW)
            i.setDataAndType(Uri.parse(data.policyPathUrl), "application/pdf");
//        i.setDataAndType(Uri.parse("https://drive.google.com/viewerng/viewer?embedded=true&url=http://digionefs.innov.in/IncentivePolicy/2021/8/2/Terms and Conditions for Associate Referral Scheme -Wal-Mart India Pvt Ltd_2.pdf"), "application/pdf")
            startActivity(i)
        }else{
            showToast(getString(R.string.no_policy_found))
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
                noDataLayout = layoutNoData.root,
                recyclerView = recyclerJobs,
                show = show
            )
        }
    }
}