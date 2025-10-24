package com.example.digitracksdk.presentation.my_letters.candidate_loi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.CandidateLoiItemBinding
import com.example.digitracksdk.presentation.my_letters.candidate_loi.model.CandidateLoiModel
import com.example.digitracksdk.presentation.my_letters.candidate_loi.model.CandidateLoiStatus

class CandidateLoiAdapter(
    val context: Context,
    private val candidateLoiList: ArrayList<CandidateLoiModel>,
    private var listener: CandidateLoiManager
) :
    RecyclerView.Adapter<CandidateLoiAdapter.ViewHolder>() {

    class ViewHolder(val binding: CandidateLoiItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.candidate_loi_item, parent, false)
        val binding = CandidateLoiItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = candidateLoiList[position]
        holder.binding.apply {
            tvTitle.text = data.name
            tvSubTitle.text = data.companyName
            tvJoiningDate.text = data.joiningDate
            tvNo.text = data.no
            tvPdfName.text = data.fileUrl
            when (data.candidateLoiStatus) {
                CandidateLoiStatus.ACCEPTED -> {
                    tvStatus.apply {
                        text = context.getString(R.string.accepted)
                        setTextColor(ContextCompat.getColorStateList(context, R.color.jungle_green))
                    }
                }
                CandidateLoiStatus.REJECTED -> {
                    tvStatus.apply {
                        text = context.getString(R.string.rejected)
                        setTextColor(ContextCompat.getColorStateList(context, R.color.rejected_color))
                    }
                }
                else -> {
                    tvStatus.apply {
                        text = context.getString(R.string.awaiting)
                        setTextColor(ContextCompat.getColorStateList(context, R.color.tango))
                    }
                }
            }
            btnDownload.setOnClickListener {
                listener.onItemClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return candidateLoiList.size
    }

    interface CandidateLoiManager {
        fun onItemClick(position: Int)
    }
}