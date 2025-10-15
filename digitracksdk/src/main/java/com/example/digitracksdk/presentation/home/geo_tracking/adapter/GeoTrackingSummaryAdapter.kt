package com.example.digitracksdk.presentation.home.geo_tracking.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.GeoTrackingItemBinding
import com.example.digitracksdk.presentation.home.geo_tracking.model.GeoTrackingModel

class GeoTrackingSummaryAdapter
    (
    private val appContext: Context,
    private var geoTrackingList: ArrayList<GeoTrackingModel>,
    val listener : GeoTrackingClickManager
) : RecyclerView.Adapter<GeoTrackingSummaryAdapter.ViewHolder>() {


    class ViewHolder(var binding: GeoTrackingItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(appContext).inflate(R.layout.geo_tracking_item, parent, false)
        val binding = GeoTrackingItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = geoTrackingList[position]
        holder.binding.apply {
            tvTrackingDate.text = data.TrackingStartDateTime
            tvStartAddress.text = data.StartAddress
            tvEndAddress.text = data.EndAddress
            btnView.setOnClickListener{
                listener.clickOnViewButton(position)
            }
        }

    }

    override fun getItemCount(): Int {
        return geoTrackingList.size
    }
    interface  GeoTrackingClickManager
    {
       fun clickOnViewButton(position  :Int)
    }

}