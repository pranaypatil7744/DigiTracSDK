package com.example.digitracksdk.presentation.onboarding.reference_details_screen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.ReferenceDetailsItemBinding
import com.example.digitracksdk.domain.model.onboarding.LstReferenceDetail

class ReferenceDetailsAdapter(
    val context: Context,
    var referenceDetailsList: ArrayList<LstReferenceDetail>,
    var listener: ReferenceListener
) : RecyclerView.Adapter<ReferenceDetailsAdapter.ViewHolder>() {
    class ViewHolder(val binding: ReferenceDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.reference_details_item,parent,false)
        val binding = ReferenceDetailsItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val data = referenceDetailsList[position]
        holder.binding.apply {
            tvName.text = data.Name
            tvEmail.text = data.EmailId
            tvMob.text = data.ContactNo
            tvStatus.text = data.ReferenceCategoryName
            containerReferenceDetails.setOnClickListener {
                listener.onReferenceClicked(position,data)
            }
        }
    }

    override fun getItemCount(): Int {
       return referenceDetailsList.size
    }

    interface ReferenceListener{
        fun onReferenceClicked(position: Int,data: LstReferenceDetail)
    }

    fun refresh(list: ArrayList<LstReferenceDetail>) {
        this.referenceDetailsList = list
        notifyDataSetChanged()
    }
}