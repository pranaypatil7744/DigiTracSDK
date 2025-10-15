package com.example.digitracksdk.presentation.home.nav_drawer

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.innov.digitrac.R
import com.innov.digitrac.databinding.FragmentNavDrawerBinding
import com.example.digitracksdk.presentation.home.client_policy.ClientPolicyActivity
import com.example.digitracksdk.presentation.home.help_and_support.HelpAndSupportActivity
import com.example.digitracksdk.presentation.home.innov_id_card.InnovIdCardActivity
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.JobsAndReferFdsActivity
import com.example.digitracksdk.presentation.home.nav_drawer.adapter.NavDrawerAdapter
import com.example.digitracksdk.presentation.home.nav_drawer.model.NavDrawerModel
import com.example.digitracksdk.presentation.home.nav_drawer.model.NavDrawerType
import com.example.digitracksdk.presentation.home.training.TrainingActivity
import com.example.digitracksdk.presentation.login.LoginActivity
import com.example.digitracksdk.utils.PreferenceUtils

class NavDrawerFragment : Fragment(), NavDrawerAdapter.NavDrawerClickManager {

    lateinit var binding: FragmentNavDrawerBinding
    private var navItemsList: ArrayList<NavDrawerModel> = ArrayList()
    lateinit var navDrawerAdapter: NavDrawerAdapter
    lateinit var preferenceUtils: PreferenceUtils
    companion object {
        fun newInstance(): NavDrawerFragment {
            return NavDrawerFragment()
        }
    }
    var name:String = ""
    var email:String = ""
    var profilePic :Bitmap?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNavDrawerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceUtils = PreferenceUtils(this.requireContext())

        setUpNavMenuData()
    }

    private fun setUpNavMenuData() {
        navItemsList.clear()
        navItemsList.add(
            NavDrawerModel(
                profilePic = profilePic,
                backgroundPic = R.drawable.dummy_bg,
                profileName = name,
                email = email,
                navDrawerType = NavDrawerType.NAV_PROFILE
            )
        )
//        navItemsList.add(
//            NavDrawerModel(
//                navDrawerType = NavDrawerType.NAV_ITEMS,
//                itemName = getString(R.string.client_policy), itemIcon = R.drawable.ic_receipt
//            )
//        )
//        navItemsList.add(
//            NavDrawerModel(
//                navDrawerType = NavDrawerType.NAV_ITEMS,
//                itemName = getString(R.string.training), itemIcon = R.drawable.ic_training
//            )
//        )
//        navItemsList.add(
//            NavDrawerModel(
//                navDrawerType = NavDrawerType.NAV_ITEMS,
//                itemName = getString(R.string.jobs_and_refer_a_friend), itemIcon = R.drawable.ic_jobs
//            )
//        )
//        navItemsList.add(
//            NavDrawerModel(
//                navDrawerType = NavDrawerType.NAV_ITEMS,
//                itemName = getString(R.string.innov_id_card), itemIcon = R.drawable.ic_card
//            )
//        )
//        navItemsList.add(
//            NavDrawerModel(
//                navDrawerType = NavDrawerType.NAV_ITEMS,
//                itemName = getString(R.string.customer_id_card), itemIcon = R.drawable.ic_card
//            )
//        )
        navItemsList.add(NavDrawerModel(navDrawerType = NavDrawerType.DIVIDER))
        navItemsList.add(
            NavDrawerModel(
                navDrawerType = NavDrawerType.NAV_ITEMS,
                itemName = getString(R.string.help_and_support), itemIcon = R.drawable.ic_help
            )
        )
        navItemsList.add(
            NavDrawerModel(
                navDrawerType = NavDrawerType.NAV_ITEMS,
                itemName = getString(R.string.about_us), itemIcon = R.drawable.ic_profile
            )
        )
        navItemsList.add(
            NavDrawerModel(
                navDrawerType = NavDrawerType.NAV_ITEMS,
                itemName = getString(R.string.logout), itemIcon = R.drawable.ic_logout
            )
        )
        setUpNavAdapter()

    }

    private fun setUpNavAdapter() {
        navDrawerAdapter = NavDrawerAdapter(this.requireContext(), navItemsList, this)
        binding.recyclerNav.adapter = navDrawerAdapter
    }

    override fun clickOnItems(position: Int) {
        when(navItemsList[position].itemName){
            getString(R.string.client_policy)->{
                startActivity(Intent(this.requireContext(), ClientPolicyActivity::class.java))
            }
            getString(R.string.training)->{
                startActivity(Intent(this.requireContext(), TrainingActivity::class.java))
            }
            getString(R.string.innov_id_card)->{
                startActivity(Intent(this.requireContext(), InnovIdCardActivity::class.java))
            }
            getString(R.string.refer_jobs_to_friend)->{
                startActivity(Intent(this.requireContext(), JobsAndReferFdsActivity::class.java))
            }
            getString(R.string.help_and_support)->{
                startActivity(Intent(this.requireContext(), HelpAndSupportActivity::class.java))
            }
            getString(R.string.logout)->{
                val intent = Intent(this.requireContext(), LoginActivity::class.java)
                preferenceUtils.clear()
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                this.requireActivity().finish()
            }
        }
//        (context as HomeActivity).binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun clickOnBack(position: Int) {
//        (context as HomeActivity).binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun clickOnViewProfile(position: Int) {

    }

}