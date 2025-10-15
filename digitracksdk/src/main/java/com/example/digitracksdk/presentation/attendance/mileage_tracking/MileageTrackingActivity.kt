package com.example.digitracksdk.presentation.attendance.mileage_tracking

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.innov.digitrac.R
import com.innov.digitrac.databinding.ActivityMileageTrackingBinding
import com.example.digitracksdk.presentation.attendance.mileage_tracking.adapter.TabAdapter
import com.example.digitracksdk.presentation.attendance.mileage_tracking.regularization_fragment.MileageTrackingRegularizationFragment
import com.example.digitracksdk.presentation.attendance.mileage_tracking.status_fragment.MileageTrackingStatusFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.example.digitracksdk.utils.AppUtils

class MileageTrackingActivity : AppCompatActivity() {
    lateinit var binding:ActivityMileageTrackingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMileageTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        setUpToolbar()
        setUpTabView()
    }

    private fun setUpTabView() {
        val fragmentList: ArrayList<Fragment> = ArrayList()
        val titleList:ArrayList<String> = ArrayList()

        titleList.clear()
        titleList.add(getString(R.string.status))
        titleList.add(getString(R.string.regularization))
        val statusFragment = MileageTrackingStatusFragment.newInstance()
        val regularizationFragment = MileageTrackingRegularizationFragment.newInstance()
        fragmentList.add(statusFragment)
        fragmentList.add(regularizationFragment)
        binding.viewPagerMileageTracking.adapter = TabAdapter(this, fragmentList)
        TabLayoutMediator(binding.tabLayout,binding.viewPagerMileageTracking){tab,position->
            tab.text = titleList[position]
        }.attach()
        binding.viewPagerMileageTracking.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
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
            tvTitle.text = getString(R.string.mileage_tracking)
        }
    }
}