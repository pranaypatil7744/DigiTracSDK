package com.example.digitracksdk.presentation.home.geo_tracking_2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.ItemGeoTrackingSummaryBinding
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingList
import com.example.digitracksdk.utils.AppUtils

class GeoTrackingListingAdapter(
    private val context: Context,
    private var geoTrackingList: ArrayList<GeoTrackingList>,
    val listener: GeoTrackingListener
) : RecyclerView.Adapter<GeoTrackingListingAdapter.ViewHolder>() {


    class ViewHolder(var binding: ItemGeoTrackingSummaryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGeoTrackingSummaryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = geoTrackingList[position]
        holder.binding.apply {
            val tripDate = AppUtils.INSTANCE?.convertDateFormat(
                dateFormatToRead = "MM/dd/yyyy hh:mm:ss a",
                dateToRead = data.CreatedDate.lowercase(),
                dateFormatToConvert = "dd MMM yyyy"
            )
            val startTime = AppUtils.INSTANCE?.convertDateFormat(
                dateFormatToRead = "MM/dd/yyyy hh:mm:ss a",
                dateToRead = data.StartDateTime.lowercase(),
                dateFormatToConvert = "hh:mm a"
            )
            val endTime = AppUtils.INSTANCE?.convertDateFormat(
                dateFormatToRead = "MM/dd/yyyy hh:mm:ss a",
                dateToRead = data.EndDateTime.lowercase(),
                dateFormatToConvert = "hh:mm a"
            )

            tvTrackingDate.text = AppUtils.INSTANCE?.getFormattedString(
                stringOne = context.getString(R.string.tracking_date),
                stringTwo = tripDate
            )
            tvStartTime.text = AppUtils.INSTANCE?.getFormattedString(
                stringOne = context.getString(R.string.start_time),
                stringTwo = startTime
            )
            tvEndTime.text = AppUtils.INSTANCE?.getFormattedString(
                stringOne = context.getString(R.string.end_time),
                stringTwo = endTime
            )
            tvSourceValue.text = data.StartAddress
            tvDestinationValue.text = data.EndAddress
            btnView.setOnClickListener {
                listener.onViewButtonListener(position)
            }
        }

    }

    override fun getItemCount(): Int {
        return geoTrackingList.size
    }

    interface GeoTrackingListener {
        fun onViewButtonListener(position: Int) {}
    }
}