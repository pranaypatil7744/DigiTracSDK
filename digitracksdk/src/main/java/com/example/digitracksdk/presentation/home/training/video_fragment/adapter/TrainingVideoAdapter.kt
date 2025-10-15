package com.example.digitracksdk.presentation.home.training.video_fragment.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.databinding.TrainingVideoItemBinding
import com.example.digitracksdk.presentation.home.training.video_fragment.model.TrainingVideoModel
import com.example.digitracksdk.presentation.web_view.WebViewActivity
import com.example.digitracksdk.utils.ImageUtils

class TrainingVideoAdapter(val context: Context,val trainingVideoList:ArrayList<TrainingVideoModel>):RecyclerView.Adapter<TrainingVideoAdapter.ViewHolder>() {

    class ViewHolder(var binding:TrainingVideoItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.training_video_item, parent, false)
        val binding = TrainingVideoItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = trainingVideoList[position]
        holder.binding.apply {
            val bitMap = ImageUtils.INSTANCE?.stringToBitMap(data.videoImg)
            ImageUtils.INSTANCE?.loadBitMap(imgVideoBg,bitMap)
        }
        holder.itemView.setOnClickListener {
            val b = Bundle()
            b.putString(Constant.WEB_URL,data.videoLink)
            b.putString(Constant.SCREEN_NAME,context.getString(R.string.training))
            val i = Intent(context, WebViewActivity::class.java)
            i.putExtras(b)
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return trainingVideoList.size
    }

}