package com.example.digitracksdk.presentation.home.notification.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.NotificationItemBinding
import com.example.digitracksdk.domain.model.notification.ListNotificationsModel
import com.example.digitracksdk.utils.AppUtils

class NotificationAdapter(val context: Context, val listener: NotificationClickManager, val notificationList:ArrayList<ListNotificationsModel>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    class ViewHolder(var binding: NotificationItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false)
        val binding = NotificationItemBinding.bind(view)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = notificationList[position]
        holder.binding.apply {
            tvHeading.text = context.getString(R.string.notification_from)+" "+data.ClientName
            tvNotificationCaption.text = context.getString(R.string.subject_caption)+" "+data.Caption
            tvDateTime.text = AppUtils.INSTANCE?.convertDateFormat("MM/dd/yyyy hh:mm:ss",data.UploadedOn.toString(),"dd MMM yyyy")
        }
        holder.itemView.setOnClickListener {
            listener.clickOnItem(position)
        }
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    interface NotificationClickManager {
        fun clickOnItem(position: Int)
    }
}