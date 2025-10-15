package com.example.digitracksdk.presentation.my_letters.offer_letter.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.OfferLetterItemBinding
import com.example.digitracksdk.presentation.my_letters.candidate_loi.model.CandidateLoiStatus
import com.example.digitracksdk.presentation.my_letters.offer_letter.model.OfferLetterModel

class OfferLetterAdapter(
    val context: Context,
    private val offerLetterList: ArrayList<OfferLetterModel>,
    private var listener: OfferLetterManager
) :
    RecyclerView.Adapter<OfferLetterAdapter.ViewHolder>() {

    class ViewHolder(val binding: OfferLetterItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.offer_letter_item, parent, false)
        val binding = OfferLetterItemBinding.bind(view)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = offerLetterList[position]
        holder.binding.apply {
            tvTitle.text = data.title
            tvSubTitle.text = "${data.designation} - ${data.location}"
            tvJoiningDate.text = context.getString(R.string.joining_date)+" "+data.joiningDate
            tvPdfName.text = data.fileUrl
            when (data.offerLetterStatus) {
                CandidateLoiStatus.ACCEPTED -> {
                    tvStatus.apply {
                        text = context.getString(R.string.accepted)
                        setTextColor(ContextCompat.getColorStateList(context, R.color.jungle_green))
                    }
                    btnAccept.visibility = GONE
                    btnReject.visibility = GONE
                }
                CandidateLoiStatus.REJECTED -> {
                    tvStatus.apply {
                        text = context.getString(R.string.rejected)
                        setTextColor(ContextCompat.getColorStateList(context, R.color.rejected_color))
                    }
                    btnAccept.visibility = GONE
                    btnReject.visibility = GONE
                }
                else -> {
                    tvStatus.apply {
                        text = context.getString(R.string.awaiting)
                        setTextColor(ContextCompat.getColorStateList(context, R.color.tango))
                    }
                    btnAccept.visibility = VISIBLE
                    btnReject.visibility = VISIBLE
                }
            }
            btnDownload.setOnClickListener {
                listener.onItemClick(position)
            }
            btnAccept.setOnClickListener {
                listener.clickOnAccept(position)
            }
            btnReject.setOnClickListener {
                listener.clickOnReject(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return offerLetterList.size
    }

    interface OfferLetterManager {
        fun onItemClick(position: Int)
        fun clickOnAccept(position: Int)
        fun clickOnReject(position: Int)
    }
}