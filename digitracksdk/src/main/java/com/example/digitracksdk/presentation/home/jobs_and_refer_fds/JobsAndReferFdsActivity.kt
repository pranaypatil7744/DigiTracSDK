package com.example.digitracksdk.presentation.home.jobs_and_refer_fds

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.innov.digitrac.R
import com.innov.digitrac.databinding.ActivityJobsAndReferFdsBinding
import com.example.digitracksdk.presentation.attendance.mileage_tracking.adapter.TabAdapter
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.JobsFragment
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.referrals.ReferralsFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.example.digitracksdk.utils.AppUtils

class JobsAndReferFdsActivity : AppCompatActivity() {
    lateinit var binding:ActivityJobsAndReferFdsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityJobsAndReferFdsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        setUpToolbar()
        setUpTabView()
    }

    private fun setUpTabView() {
        val fragmentList: ArrayList<Fragment> = ArrayList()
        val titleList:ArrayList<String> = ArrayList()

        titleList.clear()
        titleList.add(getString(R.string.jobs))
        titleList.add(getString(R.string.referrals))
        val jobsFragment = JobsFragment.newInstance()
        val referralsFragment = ReferralsFragment.newInstance()
        fragmentList.add(jobsFragment)
        fragmentList.add(referralsFragment)
        binding.apply {
            val adapter = TabAdapter(this@JobsAndReferFdsActivity,fragmentList)
            pagerJobs.adapter = adapter
            TabLayoutMediator(binding.tabLayout,pagerJobs){tab,position->
                tab.text = titleList[position]
            }.attach()
            pagerJobs.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }
    }

    fun showSearchBar(){
        binding.toolbar.apply {
            btnBack.visibility = GONE
            tvTitle.visibility = GONE
            btnOne.visibility = GONE
            layoutSearch.visibility = VISIBLE
        }
    }

    fun hideSearchBar(){
        binding.toolbar.apply {
            btnBack.visibility = VISIBLE
            tvTitle.visibility = VISIBLE
            btnOne.visibility = VISIBLE
            layoutSearch.visibility = GONE
            etSearch.setText("")
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            btnBack.setOnClickListener {
                finish()
            }
            tvTitle.text = getString(R.string.jobs)
            btnOne.setImageResource(R.drawable.ic_search)
            btnOne.visibility = VISIBLE
        }

    }
}