package com.example.digitracksdk.presentation.my_letters.offer_letter.adapter

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.CandidateLoiItemBinding
import com.example.digitracksdk.domain.model.my_letters.AssociateAllLettersListModel

class OtherLetterAdapter(val context:Context,
                         private val letterList:ArrayList<AssociateAllLettersListModel>, val listener: OtherLetterManager
):RecyclerView.Adapter<OtherLetterAdapter.ViewHolder>() {

    class ViewHolder(val binding: CandidateLoiItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.candidate_loi_item, parent, false)
        val binding = CandidateLoiItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = letterList[position]
        holder.binding.apply {
            tvTitle.text = data.LetterType
            tvSubTitle.text = context.getString(R.string.remark)+" : "+data.Remarks
            tvJoiningDate.text = context.getString(R.string.generated_on_)+data.LetterDate
            tvPdfName.text = data.LetterType+".pdf"
            btnDownload.setOnClickListener{
                listener.clickOnDownload(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return letterList.size
    }

    interface OtherLetterManager{
        fun clickOnDownload(position: Int)
    }

}