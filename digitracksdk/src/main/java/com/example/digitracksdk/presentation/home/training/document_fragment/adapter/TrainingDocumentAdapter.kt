package com.example.digitracksdk.presentation.home.training.document_fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.TrainingDocumentItemBinding
import com.example.digitracksdk.presentation.home.training.document_fragment.model.TrainingDocumentModel
import com.example.digitracksdk.utils.ImageUtils

class TrainingDocumentAdapter(
    val context: Context,
    var trainingList: ArrayList<TrainingDocumentModel>,
    private val listener: TrainingClickManager
) : RecyclerView.Adapter<TrainingDocumentAdapter.ViewHolder>() {

    class ViewHolder(var binding: TrainingDocumentItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.training_document_item, parent, false)
        val binding = TrainingDocumentItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = trainingList[position]
        holder.binding.apply {
            tvTitle.text = data.docName
            tvDocType.text = data.docType
            tvDocDetails.text = data.docDetails
            val bitMap = ImageUtils.INSTANCE?.stringToBitMap(data.docIcon)
            ImageUtils.INSTANCE?.loadBitMap(imgTrainingIcon,bitMap)
        }
        holder.itemView.setOnClickListener {
          listener.clickOnViewDocument(position)
        }
    }

    override fun getItemCount(): Int {
        return trainingList.size
    }

    interface TrainingClickManager{
        fun clickOnViewDocument(position: Int)
    }
}