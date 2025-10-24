package com.example.digitracksdk.presentation.home.new_reimbursement

import android.os.Bundle
import android.view.View.GONE
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityReimbursement1Binding
import com.example.digitracksdk.presentation.home.new_reimbursement.fragments.NewReimbursementApprovedFragment
import com.example.digitracksdk.presentation.home.new_reimbursement.fragments.NewReimbursementPendingFragment
import com.example.digitracksdk.presentation.home.new_reimbursement.fragments.NewReimbursementRejectedFragment
import com.example.digitracksdk.presentation.home.new_reimbursement.fragments.PendingVouchersListFragment
import com.example.digitracksdk.presentation.home.reimbursements.adapter.ReimbursementPagerAdapter
import com.example.digitracksdk.utils.AppUtils


class Reimbursement1Activity : BaseActivity(){
    lateinit var binding: ActivityReimbursement1Binding
    var tabsList: ArrayList<String> = ArrayList()
    var fragmentList: ArrayList<Fragment> = ArrayList()
    var pendingVouchersListFragment = PendingVouchersListFragment.newInstance()
    var awaitingFragment = NewReimbursementPendingFragment.newInstance()
    var approvedFragment = NewReimbursementApprovedFragment.newInstance()
    var rejectedFragment = NewReimbursementRejectedFragment.newInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReimbursement1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        setUpToolbar()
        setUpView()
    }
   /* override fun onTouchEvent(event: MotionEvent?): Boolean {
        val parent: ViewParent = binding.fragmentPager
        // or get a reference to the ViewPager and cast it to ViewParent
        parent.requestDisallowInterceptTouchEvent(true)

        // let this view deal with the event or
        return super.onTouchEvent(event)
    }*/
    private fun setUpView() {
        fragmentList.add(pendingVouchersListFragment)
        fragmentList.add(awaitingFragment)
        fragmentList.add(approvedFragment)
        fragmentList.add(rejectedFragment)
        binding.fragmentPager.adapter = ReimbursementPagerAdapter(this, fragmentList)
        binding.fragmentPager.isUserInputEnabled = true
        binding.fragmentPager.offscreenPageLimit = 4
        tabsList.add(getString(R.string.pending_vouchers))
        tabsList.add(getString(R.string.awaiting))
        tabsList.add(getString(R.string.approved))
        tabsList.add(getString(R.string.rejected))

        TabLayoutMediator(binding.tabLayout, binding.fragmentPager) { tab, position ->
            tab.text = tabsList[position]
//            if (position == 0){
//                tab.orCreateBadge.backgroundColor = ContextCompat.getColor(this,R.color.blue_ribbon)
//                tab.orCreateBadge.badgeTextColor = ContextCompat.getColor(this,R.color.white)
//                tab.orCreateBadge.number = awaitingCount
//                tab.orCreateBadge.badgeGravity = BadgeDrawable.TOP_END
//            }
        }.attach()

    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.reimbursement1)
            divider.visibility = GONE
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

}