package com.example.digitracksdk.presentation.home.view_payout.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.ItemPayoutBinding
import com.example.digitracksdk.domain.model.view_payout.LSTReimbDetail

class ViewPayoutAdapter(val context: Context, private val payoutList: ArrayList<LSTReimbDetail>) :
    RecyclerView.Adapter<ViewPayoutAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemPayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_payout, parent, false)
        val binding = ItemPayoutBinding.bind(v)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = payoutList[position]
        holder.binding.apply {
            tvTitle.text = data.Type
            tvSubTitle.text = data.ReleaseDate
            tvAmt.text = data.ReimbursementAmount
            tvForDate.visibility = View.VISIBLE
            tvForDate.text = "${context.getString(R.string.payment_for_)} ${data.Month} ${data.Year}"
        }
    }

    override fun getItemCount(): Int {
        return payoutList.size
    }
}