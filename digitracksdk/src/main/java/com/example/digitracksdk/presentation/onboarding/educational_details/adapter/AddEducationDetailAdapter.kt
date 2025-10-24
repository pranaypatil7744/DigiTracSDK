package com.example.digitracksdk.presentation.onboarding.educational_details.adapter

import android.content.Context
import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.ItemEditTextBinding
import com.example.digitracksdk.presentation.onboarding.educational_details.model.AddEducationDetailModel

class AddEducationDetailAdapter(
    val context: Context,
    var list: ArrayList<AddEducationDetailModel>
) : RecyclerView.Adapter<AddEducationDetailAdapter.ViewHolder>() {


    class ViewHolder : RecyclerView.ViewHolder {

        var itemEditTextBinding: ItemEditTextBinding? = null

        constructor(binding: ItemEditTextBinding) : super(binding.root) {
            itemEditTextBinding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_edit_text, parent, false)
        val binding = ItemEditTextBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.itemEditTextBinding?.apply {
            itemTextInput.hint = data.title
            itemEditText.text = Editable.Factory.getInstance().newEditable(data.value)

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}