package com.example.digitracksdk.presentation.onboarding.candidate_details.adapter

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.ItemEditTextBinding
import com.innov.digitrac.databinding.ItemLabelBinding
import com.innov.digitrac.databinding.ItemSpinnerBinding
import com.example.digitracksdk.presentation.onboarding.candidate_details.model.CandidateDetails
import com.example.digitracksdk.presentation.onboarding.candidate_details.model.CandidateItemType

class CandidateDetailsAdapter(
    val context: Context,
    var list: ArrayList<CandidateDetails>,
    var genderAdapter: ArrayAdapter<String>
) : RecyclerView.Adapter<CandidateDetailsAdapter.ViewHolder>() {

    class ViewHolder : RecyclerView.ViewHolder {

        var itemLabelBinding: ItemLabelBinding? = null
        var itemEditTextBinding: ItemEditTextBinding? = null
        var itemSpinnerBinding: ItemSpinnerBinding? = null

        constructor(binding: ItemLabelBinding) : super(binding.root) {
            itemLabelBinding = binding
        }

        constructor(binding: ItemEditTextBinding) : super(binding.root) {
            itemEditTextBinding = binding
        }

        constructor(binding: ItemSpinnerBinding) : super(binding.root) {
            itemSpinnerBinding = binding
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            CandidateItemType.LABEL.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_label, parent, false)
                val binding = ItemLabelBinding.bind(view)
                return ViewHolder(binding)
            }
            CandidateItemType.NUMBER.value, CandidateItemType.TEXT.value, CandidateItemType.TEXT_EMAIL.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_edit_text, parent, false)
                val binding = ItemEditTextBinding.bind(view)
                return ViewHolder(binding)
            }
            else -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_spinner, parent, false)
                val binding = ItemSpinnerBinding.bind(view)
                return ViewHolder(binding)
            }

        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        if (holder.itemViewType == CandidateItemType.LABEL.value) {
            holder.itemLabelBinding?.apply {
                textLabel.text = data.title
            }
        } else if (holder.itemViewType == CandidateItemType.SPINNER.value) {
            holder.itemSpinnerBinding?.apply {
                val valueToUse = data.value as? String?:""
                textInputSpinner.hint = data.title
                textInputDropdown.isEnabled = data.isEnable
                textInputDropdown.text = Editable.Factory.getInstance().newEditable(valueToUse)
                textInputDropdown.setAdapter(genderAdapter)
                textInputDropdown.onItemClickListener =
                    AdapterView.OnItemClickListener { parent, view, position, id -> }
            }
        } else if (holder.itemViewType == CandidateItemType.NUMBER.value ||
            holder.itemViewType == CandidateItemType.TEXT_EMAIL.value ||
            holder.itemViewType == CandidateItemType.TEXT.value
        ) {
            holder.itemEditTextBinding?.apply {
                itemTextInput.hint = data.title

                val valueToUse = (data.value as? String) ?: ""
                itemEditText.apply {
                    text =
                        Editable.Factory.getInstance().newEditable(
                            valueToUse
                        )
                    isEnabled = data.isEnable
                    inputType = when (data.itemType.value) {
                        CandidateItemType.NUMBER.value -> {
                            InputType.TYPE_CLASS_NUMBER
                        }
                        CandidateItemType.TEXT_EMAIL.value -> {
                            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                        }
                        else -> {
                            InputType.TYPE_CLASS_TEXT
                        }
                    }
                    doOnTextChanged { text, start, before, count ->
                        data.value = text?:""
                    }
                }
            }
        } else {

        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].itemType.value
    }
    fun updateGenderAdapter(genderAdapter: ArrayAdapter<String>){
        this.genderAdapter  = genderAdapter
    }

    fun refresh(list: ArrayList<CandidateDetails>) {
        this.list = list
        notifyDataSetChanged()
    }

}