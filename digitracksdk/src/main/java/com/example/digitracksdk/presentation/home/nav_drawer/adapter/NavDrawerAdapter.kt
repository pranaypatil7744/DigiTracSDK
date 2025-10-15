package com.example.digitracksdk.presentation.home.nav_drawer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.NavItemDividerBinding
import com.innov.digitrac.databinding.NavItemProfileBinding
import com.innov.digitrac.databinding.NavItemsBinding
import com.example.digitracksdk.presentation.home.nav_drawer.model.NavDrawerModel
import com.example.digitracksdk.presentation.home.nav_drawer.model.NavDrawerType
import com.example.digitracksdk.utils.ImageUtils

class NavDrawerAdapter(val context: Context, private val navMenuList: ArrayList<NavDrawerModel>, val listener: NavDrawerClickManager) :
    RecyclerView.Adapter<NavDrawerAdapter.ViewHolder>() {

    class ViewHolder: RecyclerView.ViewHolder{
        var navItemProfileBinding : NavItemProfileBinding? = null
        var navItemsBinding: NavItemsBinding?= null
        var navItemDividerBinding: NavItemDividerBinding? = null
        constructor(binding: NavItemProfileBinding):super(binding.root){
            navItemProfileBinding = binding
        }
        constructor(binding:NavItemsBinding):super(binding.root){
            navItemsBinding = binding
        }
        constructor(binding: NavItemDividerBinding):super(binding.root){
            navItemDividerBinding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType){
            NavDrawerType.NAV_PROFILE.value -> {
                val view = LayoutInflater.from(context).inflate(R.layout.nav_item_profile,parent,false)
                val binding = NavItemProfileBinding.bind(view)
                ViewHolder(binding)
            }
            NavDrawerType.NAV_ITEMS.value -> {
                val view = LayoutInflater.from(context).inflate(R.layout.nav_items,parent,false)
                val binding = NavItemsBinding.bind(view)
                ViewHolder(binding)
            }
            NavDrawerType.DIVIDER.value -> {
                val view = LayoutInflater.from(context).inflate(R.layout.nav_item_divider,parent,false)
                val binding = NavItemDividerBinding.bind(view)
                ViewHolder(binding)
            }
            else -> {
                val view = LayoutInflater.from(context).inflate(R.layout.nav_items,parent,false)
                val binding = NavItemsBinding.bind(view)
                ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = navMenuList[position]
        if (holder.itemViewType == NavDrawerType.NAV_ITEMS.value){
            holder.navItemsBinding?.apply {
                tvNavItem.text = data.itemName
                data.itemIcon?.let { iconNav.setImageResource(it) }
            }
            holder.itemView.setOnClickListener {
                listener.clickOnItems(position)
            }
        }else if (holder.itemViewType == NavDrawerType.NAV_PROFILE.value){
            holder.navItemProfileBinding?.apply {
                tvName.text = data.profileName
                tvEmail.text =data.email
                ImageUtils.INSTANCE?.loadBitMap(imgProfile,data.profilePic)
                btnNext.setImageResource(R.drawable.ic_circle_next)
                btnBack.setOnClickListener {
                    listener.clickOnBack(position)
                }
                btnNext.setOnClickListener {
                    listener.clickOnViewProfile(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return navMenuList.size
    }

    override fun getItemViewType(position: Int): Int {
        return navMenuList[position].navDrawerType.value
    }

    interface NavDrawerClickManager{
        fun clickOnItems(position: Int)
        fun clickOnBack(position: Int)
        fun clickOnViewProfile(position: Int)
    }
}