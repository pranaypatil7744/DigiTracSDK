package com.example.digitracksdk.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.innov.digitrac.R

open class BaseActivity:AppCompatActivity() {
    var mCurrentPhotoPath: String? = ""
//    lateinit var preferenceUtils:PreferenceUtils
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onResume() {
        super.onResume()
        com.example.digitracksdk.utils.AppUtils.INSTANCE?.setLang(this)
    }

    fun showToast(msg:String){
        com.example.digitracksdk.utils.AppUtils.INSTANCE?.showLongToast(this,msg)
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
        recyclerView:RecyclerView?=null,
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

    fun hideKeyboard(mAcivity: Activity?) {
        val view = mAcivity?.currentFocus
        if (view != null) {
            val imm = mAcivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}