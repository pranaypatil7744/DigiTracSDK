package com.example.digitracksdk.presentation.onboarding.family_details_screen.adapter

import android.content.Context
import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.ItemEditTextBinding
import com.example.digitracksdk.databinding.ItemMultiEditTextBinding
import com.example.digitracksdk.databinding.ItemSpinnerBinding
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailModel
import com.example.digitracksdk.presentation.onboarding.family_details_screen.model.FamilyViewType
import com.example.digitracksdk.presentation.onboarding.family_details_screen.model.MultiEditTextModel

class AddFamilyMemberAdapter(
    val context: Context,
    var list: ArrayList<DetailModel>,
) : RecyclerView.Adapter<AddFamilyMemberAdapter.ViewHolder>() {

    class ViewHolder : RecyclerView.ViewHolder {

        var itemEditTextBinding: ItemEditTextBinding? = null

        var itemSpinnerBinding: ItemSpinnerBinding? = null

        var itemMultiEditTextBinding: ItemMultiEditTextBinding? = null

        constructor(binding: ItemMultiEditTextBinding) : super(binding.root) {
            itemMultiEditTextBinding = binding
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
            FamilyViewType.EDIT_TEXT.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_edit_text, parent, false)
                val binding = ItemEditTextBinding.bind(view)
                ViewHolder(binding)
            }
            FamilyViewType.MULTI_EDITTEXT.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_multi_edit_text, parent, false)
                val binding = ItemMultiEditTextBinding.bind(view)
                ViewHolder(binding)
            }
            else -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_spinner, parent, false)
                val binding = ItemSpinnerBinding.bind(view)
                ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        when (holder.itemViewType) {
            FamilyViewType.EDIT_TEXT.value -> {
                holder.itemEditTextBinding?.apply {
                    itemTextInput.hint = data.title
                    itemEditText.apply {
                        text = Editable.Factory.getInstance().newEditable("Sss")
                        setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, data.icon ?: 0, 0)
                    }
                }
            }
            FamilyViewType.MULTI_EDITTEXT.value -> {
                holder.itemMultiEditTextBinding?.apply {
                    val editTextModel = (data.value as? MultiEditTextModel)
                    itemFirstInput.hint = editTextModel?.hint1
                    itemSecondInput.hint = editTextModel?.hint2
                    itemFirstEditText.text = Editable.Factory.getInstance().newEditable(
                        editTextModel?.value1 ?: ""
                    )
                    itemSecondEditText.text = Editable.Factory.getInstance().newEditable(
                        editTextModel?.value2 ?: ""
                    )
                }
            }
            else -> {

                holder.itemSpinnerBinding?.apply {

                }
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].itemType.value
    }


}