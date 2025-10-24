package com.example.digitracksdk.presentation.home.jobs_and_refer_fds.referrals

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.doOnTextChanged
import com.example.digitracksdk.Constant
import com.example.digitracksdk.base.BaseFragment
import com.example.digitracksdk.databinding.FragmentReferralsBinding
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.JobsAndReferFdsActivity
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.JobsAndReferFriendViewModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.LstreferredDetailsItem
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.ReferredUsersRequestModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.referral_profile_details.ReferralProfileDetailsActivity
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.referrals.adapter.ReferralsAdapter
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class ReferralsFragment : BaseFragment(), ReferralsAdapter.ReferralClickManager {
    companion object {
        fun newInstance(): ReferralsFragment {
            return ReferralsFragment()
        }
    }

    lateinit var binding: FragmentReferralsBinding
    private lateinit var referralsAdapter: ReferralsAdapter
    private var referralsList: ArrayList<LstreferredDetailsItem> = ArrayList()
    private var fixReferralsList: ArrayList<LstreferredDetailsItem> = ArrayList()
    private val jobsAndReferFriendViewModel: JobsAndReferFriendViewModel by viewModel()
    private lateinit var preferenceUtils: PreferenceUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        preferenceUtils = PreferenceUtils(requireContext())
        binding = FragmentReferralsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpReferralsAdapter()
        setUpObserver()
        setUpListener()
    }

    override fun onResume() {
        super.onResume()
        callReferredUsersList()
    }

    private fun setUpObserver() {
        binding.apply {
            with(jobsAndReferFriendViewModel) {
                referredUserResponseData.observe(requireActivity()) {
                    toggleLoader(false)
                    if (!it.lstreferredDetails.isNullOrEmpty()) {
                        showNoDataLayout(false)
                        referralsList.clear()
                        referralsList.addAll(it.lstreferredDetails)
                        fixReferralsList.clear()
                        fixReferralsList.addAll(referralsList)
                        referralsAdapter.notifyDataSetChanged()
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
                callReferredUsersList()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing=false
                callReferredUsersList()
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
                            val searchList = referralSearch(text.toString(), fixReferralsList)
                            referralsList.clear()
                            referralsList.addAll(searchList)
                            if (searchList.isEmpty()) {
                                if (context?.isNetworkAvailable() == true){
                                    binding.layoutNoData.root.visibility = VISIBLE
                                }
                            } else {
                                binding.layoutNoData.root.visibility = GONE
                            }
                            referralsAdapter.notifyDataSetChanged()
                        } else {
                            referralsList.clear()
                            referralsList.addAll(fixReferralsList)
                            binding.layoutNoData.root.visibility = GONE
                            referralsAdapter.notifyDataSetChanged()
                        }
                    }
                }

            }
        }
    }

    private fun referralSearch(
        searchText: String,
        referralList: ArrayList<LstreferredDetailsItem>
    ): ArrayList<LstreferredDetailsItem> {
        val list = referralList.filter {
            it.firstName.lowercase(Locale.ROOT).contains(
                searchText, true
            ).or(
                it.lastName.lowercase(Locale.ROOT).contains(searchText, true)
            ).or(
                it.location.lowercase(Locale.ROOT).contains(searchText)
            ).or(
                it.skill.lowercase(Locale.ROOT).contains(searchText)
            )
        }
        return list as ArrayList<LstreferredDetailsItem>
    }

    private fun callReferredUsersList() {

        with(binding){

            if (context?.isNetworkAvailable() == true) {
                layoutNoInternet.root.visibility = GONE
                toggleLoader(true)
                jobsAndReferFriendViewModel.callReferredUsersList(
                    ReferredUsersRequestModel(preferenceUtils.getValue(com.example.digitracksdk.Constant.PreferenceKeys.INNOV_ID))
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerReferrals.visibility= GONE
            }
            layoutNoData.root.visibility= GONE
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
                recyclerView = recyclerReferrals,
                show = show
            )
        }
    }

    private fun setUpReferralsAdapter() {
        referralsAdapter = ReferralsAdapter(this.requireContext(), referralsList, this)
        binding.recyclerReferrals.adapter = referralsAdapter
    }

    override fun onReferralClick(position: Int) {
        val data = referralsList[position]
        val b = Bundle()
        b.putSerializable(Constant.REFERRAL_MODEL, data)
        val i = (Intent(this.requireContext(), ReferralProfileDetailsActivity::class.java))
        i.putExtras(b)
        startActivity(i)
    }

}