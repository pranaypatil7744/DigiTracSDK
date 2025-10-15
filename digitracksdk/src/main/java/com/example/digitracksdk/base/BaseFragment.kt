package com.example.digitracksdk.base

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.innov.digitrac.R

open class BaseFragment: Fragment() {

    fun showToast(msg:String){
        com.example.digitracksdk.utils.AppUtils.INSTANCE?.showLongToast(requireContext(), msg)
    }

    fun toggleFadeView(
        parent: View,
        loader: View,
        imageView: ImageView,
        showLoader: Boolean
    ) {

        if (showLoader) {
            com.example.digitracksdk.utils.AppUtils.INSTANCE?.hideFadeView(parent, com.example.digitracksdk.Constant.VIEW_ANIMATE_DURATION)
            com.example.digitracksdk.utils.AppUtils.INSTANCE?.showFadeView(loader, com.example.digitracksdk.Constant.VIEW_ANIMATE_DURATION)
            com.example.digitracksdk.utils.ImageUtils.INSTANCE?.loadLocalGIFImage(imageView, R.drawable.loader)
            loader.visibility = View.VISIBLE
        } else {
            com.example.digitracksdk.utils.AppUtils.INSTANCE?.hideView(loader, com.example.digitracksdk.Constant.VIEW_ANIMATE_DURATION)
            com.example.digitracksdk.utils.AppUtils.INSTANCE?.showView(parent, com.example.digitracksdk.Constant.VIEW_ANIMATE_DURATION)
            loader.visibility = View.GONE
        }
    }
    fun noDataLayout(
        noDataLayout:View,
        recyclerView: RecyclerView?=null,
        viewPager2: ViewPager2?=null,
        show:Boolean
    ){
        if (show){
            recyclerView?.visibility=View.GONE
            viewPager2?.visibility=View.GONE
            noDataLayout.visibility=View.VISIBLE
        }else
        {
            recyclerView?.visibility=View.VISIBLE
            viewPager2?.visibility=View.VISIBLE
            noDataLayout.visibility=View.GONE
        }
    }

}