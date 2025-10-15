package com.example.digitracksdk.presentation.onboarding.kyc_screen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.ViewDocumentItemBinding
import com.example.digitracksdk.domain.model.uploaded_documents.CandidateDocListModel

class KycAdapter(
    val context: Context,
    var kycDocList: ArrayList<CandidateDocListModel>,
    var listener: DocumentViewManager
) :
    RecyclerView.Adapter<KycAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewDocumentItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_document_item, parent, false)
        val binding = ViewDocumentItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = kycDocList[position]
        holder.binding.apply {
            etDocument.setText(data.DocName)
//            layoutDocument.hint = data.DocName
            btnView.setOnClickListener {
                listener.onViewClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return kycDocList.size
    }

    interface DocumentViewManager {
        fun onViewClick(position: Int)
    }
}