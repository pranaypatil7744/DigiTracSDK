package com.example.digitracksdk.presentation.home.home_fragment.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.databinding.HomeBannerItemBinding
import com.innov.digitrac.domain.model.home_model.ListBannerModel
import com.example.digitracksdk.presentation.web_view.WebViewActivity
import com.example.digitracksdk.utils.ImageUtils
import com.example.digitracksdk.utils.PreferenceUtils

class HomeBannerAdapter(val context: Context, private val bannerList: ArrayList<ListBannerModel>) :
    RecyclerView.Adapter<HomeBannerAdapter.ViewHolder>() {

    lateinit var preferenceUtils: PreferenceUtils

    class ViewHolder(val binding: HomeBannerItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.home_banner_item, parent, false)
        val binding = HomeBannerItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = bannerList[position]
        holder.binding.apply {
            ImageUtils.INSTANCE?.loadBannerImage(imgHomeBanner, data.BannerUrl)
        }
        holder.itemView.setOnClickListener {
            val b = Bundle()
            preferenceUtils = PreferenceUtils(context)
            val innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
            if (!data.Hyperlink.isNullOrEmpty()) {
                if (data.IsInnovId == Constant.Yes) {
                    var hyperLink = data.Hyperlink
                    if (hyperLink?.contains("?") == true) {
                        hyperLink = hyperLink + "&innovid=" + innovId
                    } else {
                        hyperLink = hyperLink + "?innovid=" + innovId
                    }
                    b.putString(Constant.WEB_URL, "${hyperLink}")
                } else {
                    b.putString(Constant.WEB_URL, "${data.Hyperlink}")
                }

                /* if (data.BannerName == "AssociateSurvey")
                     b.putString(Constant.WEB_URL, "${data.Hyperlink}")
                 else
                     b.putString(Constant.WEB_URL, "${data.Hyperlink}?innovid=$innovId")
 */
                val i = Intent(context, WebViewActivity::class.java)
                i.putExtras(b)
                context.startActivity(i)
            }
        }

    }

    override fun getItemCount(): Int {
        return bannerList.size
    }

}