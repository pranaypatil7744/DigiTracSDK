package com.example.digitracksdk.presentation.home.home_fragment.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.*
import com.example.digitracksdk.presentation.home.home_fragment.model.HomeModel
import java.util.*

class HomeAdapter(val context: Context, private val homeItemList:ArrayList<HomeModel>,
                  val listener: HomeOnClickManager
): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    enum class HomeItemType(var value:Int){
        HOME_BANNER_ITEM(1),
        HOME_ITEM_TITLE(2),
        HOME_ITEM_WITHOUT_COUNTS(3),
        HOME_ITEM_BOTTOM(4),
        HOME_ITEM_ATTENDANCE(5),
        HOME_ITEM_PROFILE_STATUS(6)

    }

    class ViewHolder: RecyclerView.ViewHolder{
        var homeBannerItemBinding: HomeBannerListItemsBinding? = null
        var homeTitleItemBinding: HomeItemTitleBinding? = null
        var homeItemWithoutCountsBinding : HomeItemWithoutCountsBinding? = null
        var homeItemAttendanceBinding : HomeItemAttendanceBinding? = null
        var homeItemBottomBinding : HomeItemBottomBinding? = null
        var homeItemProfileStatusBinding : HomeItemProfileStatusBinding? = null
        constructor(binding: HomeBannerListItemsBinding):super(binding.root){
            homeBannerItemBinding = binding
        }
        constructor(binding: HomeItemTitleBinding):super(binding.root){
            homeTitleItemBinding = binding
        }
        constructor(binding: HomeItemWithoutCountsBinding):super(binding.root){
            homeItemWithoutCountsBinding = binding
        }
        constructor(binding: HomeItemAttendanceBinding):super(binding.root){
            homeItemAttendanceBinding = binding
        }
        constructor(binding: HomeItemBottomBinding):super(binding.root){
            homeItemBottomBinding = binding
        }
        constructor(binding: HomeItemProfileStatusBinding):super(binding.root){
            homeItemProfileStatusBinding = binding
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType){
            HomeItemType.HOME_BANNER_ITEM.value -> {
                val view = LayoutInflater.from(context).inflate(R.layout.home_banner_list_items,parent,false)
                val binding = HomeBannerListItemsBinding.bind(view)
                ViewHolder(binding)
            }
            HomeItemType.HOME_ITEM_PROFILE_STATUS.value -> {
                val view = LayoutInflater.from(context).inflate(R.layout.home_item_profile_status,parent,false)
                val binding = HomeItemProfileStatusBinding.bind(view)
                ViewHolder(binding)
            }
            HomeItemType.HOME_ITEM_TITLE.value -> {
                val view = LayoutInflater.from(context).inflate(R.layout.home_item_title,parent,false)
                val binding = HomeItemTitleBinding.bind(view)
                ViewHolder(binding)
            }
            HomeItemType.HOME_ITEM_WITHOUT_COUNTS.value -> {
                val view = LayoutInflater.from(context).inflate(R.layout.home_item_without_counts,parent,false)
                val binding = HomeItemWithoutCountsBinding.bind(view)
                ViewHolder(binding)
            }
            HomeItemType.HOME_ITEM_ATTENDANCE.value -> {
                val view = LayoutInflater.from(context).inflate(R.layout.home_item_attendance,parent,false)
                val binding = HomeItemAttendanceBinding.bind(view)
                ViewHolder(binding)
            }
            HomeItemType.HOME_ITEM_BOTTOM.value -> {
                val view = LayoutInflater.from(context).inflate(R.layout.home_item_bottom,parent,false)
                val binding = HomeItemBottomBinding.bind(view)
                ViewHolder(binding)
            }
            else ->{
                val view = LayoutInflater.from(context).inflate(R.layout.home_item_title,parent,false)
                val binding = HomeItemTitleBinding.bind(view)
                ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = homeItemList[position]
        when (holder.itemViewType) {
            HomeItemType.HOME_BANNER_ITEM.value -> {
                holder.homeBannerItemBinding?.apply {
                    var currentPage = 0
                    // Auto start of viewpager
                    val handler = Handler(Looper.getMainLooper())
                    val Update = Runnable {
                        if (currentPage == data.bannerList?.size) {
                            currentPage = 0
                        }
                        pager.setCurrentItem(
                            currentPage++,
                            true
                        )
                    }
                    val swipeTimer = Timer()
                    swipeTimer.schedule(object : TimerTask() {
                        override fun run() {
                            handler.post(Update)
                        }
                    }, 6000, 6000)
                    pager.adapter = data.bannerList?.let { HomeBannerAdapter(context, it) }
                }
            }
            HomeItemType.HOME_ITEM_PROFILE_STATUS.value -> {
                holder.homeItemProfileStatusBinding?.apply {
                    tvProfilePercentage.text = "${data.progress} ${context.getString(R.string.percentage)} ${context.getString(R.string._complete)}"
                    btnComplateNow.text = data.btnText
                    data.progress?.let { progressProfile.progress = it }
                    btnComplateNow.setOnClickListener {
                        listener.clickOnCompleteProfile(position)
                    }
                }
            }
            HomeItemType.HOME_ITEM_TITLE.value -> {
                holder.homeTitleItemBinding?.apply {
                    tvTitle.text = data.title
                    tvSubTitle.text = data.subTitle
                }
            }
            HomeItemType.HOME_ITEM_WITHOUT_COUNTS.value -> {
                holder.homeItemWithoutCountsBinding?.apply {
                    recyclerHomeItems.adapter =
                        data.homeDashboardMenu?.let { HomeDashboardAdapter(context, it,listener) }
                }
            }
            HomeItemType.HOME_ITEM_ATTENDANCE.value -> {
                holder.homeItemAttendanceBinding?.apply {
                    data.icon1?.let { imgAttendance.setImageResource(it) }
                    tvTitle.text = data.title
                    tvDate.text = data.subTitle
                    btnAttendance.text = data.btnText
                    btnAttendance.setOnClickListener {
                        listener.clickOnAttendance(position)
                    }
                }

            }
            HomeItemType.HOME_ITEM_BOTTOM.value -> {
                holder.homeItemBottomBinding?.apply {
                    data.bannerImage?.let { imgBanner.setImageResource(it) }
                    data.icon1?.let { imgIcon1.setImageResource(it) }
                    data.icon2?.let { imgIcon2.setImageResource(it) }
                    data.icon3?.let { imgIcon3.setImageResource(it) }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return homeItemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return homeItemList[position].homeItemType.value
    }

    interface HomeOnClickManager{
        fun clickOnItem(position: Int,itemName:String)
        fun clickOnAttendance(position: Int)
        fun clickOnCompleteProfile(position: Int)
    }
}