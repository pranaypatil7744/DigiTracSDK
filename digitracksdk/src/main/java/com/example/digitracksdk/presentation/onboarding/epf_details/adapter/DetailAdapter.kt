package com.example.digitracksdk.presentation.onboarding.epf_details.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.*
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailItemType
import com.example.digitracksdk.presentation.onboarding.epf_details.model.DetailModel
import com.example.digitracksdk.presentation.onboarding.epf_details.model.SpinnerType
import com.example.digitracksdk.presentation.onboarding.family_details_screen.model.MultiEditTextModel
import com.example.digitracksdk.presentation.onboarding.family_details_screen.model.TxtInputType
import com.example.digitracksdk.utils.DecimalDigitsInputFilter


class DetailAdapter(
    val context: Context,
    var list: ArrayList<DetailModel>,
    var bloodGroupAdapter: ArrayAdapter<String>? = null,
    var maritalStatusAdapter: ArrayAdapter<String>? = null,
    var educationalCategoryAdapter: ArrayAdapter<String>? = null,
    var educationalStreamAdapter: ArrayAdapter<String>? = null,
    var genderAdapter: ArrayAdapter<String>? = null,
    var referenceAdapter: ArrayAdapter<String>? = null,
    var currentlyEmployeedAdapter: ArrayAdapter<String>? = null,
    var relationAdapter: ArrayAdapter<String>? = null,
    var residingWithYouAdapter: ArrayAdapter<String>? = null,
    var nomineeAdapter: ArrayAdapter<String>? = null,
    var genderRadioList: ArrayList<String> = ArrayList(),
    var yesNoAdapter: ArrayAdapter<String>? = null,
    var bankListAdapter: ArrayAdapter<String>? = null,
    var stateNameAdapter: ArrayAdapter<String>? = null,
    var listener: DetailListener? = null
) : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    class ViewHolder : RecyclerView.ViewHolder {

        var itemEditTextBinding: ItemEditTextBinding? = null

        var itemLabelBinding: ItemLabelBinding? = null

        var itemDocumentBinding: ItemDocumentBinding? = null

        var itemMultiEditTextBinding: ItemMultiEditTextBinding? = null

        var itemSpinnerBinding: ItemSpinnerBinding? = null


        var itemCheckBoxDescriptionBinding: ItemCheckBoxDescriptionBinding? = null

        var itemRadioGroupBinding: ItemRadioGroupBinding? = null

        var itemEditTextRemarkBinding: ItemEditTextRemarkBinding? = null
        var itemExtraFieldBinding: ItemExtraFieldBinding? = null


        constructor(binding: ItemRadioGroupBinding) : super(binding.root) {
            itemRadioGroupBinding = binding
        }

        constructor(binding: ItemEditTextBinding) : super(binding.root) {
            itemEditTextBinding = binding
        }

        constructor(binding: ItemEditTextRemarkBinding) : super(binding.root) {
            itemEditTextRemarkBinding = binding
        }

        constructor(binding: ItemLabelBinding) : super(binding.root) {
            itemLabelBinding = binding
        }

        constructor(binding: ItemDocumentBinding) : super(binding.root) {
            itemDocumentBinding = binding
        }

        constructor(binding: ItemMultiEditTextBinding) : super(binding.root) {
            itemMultiEditTextBinding = binding
        }

        constructor(binding: ItemSpinnerBinding) : super(binding.root) {
            itemSpinnerBinding = binding
        }

        constructor(binding: ItemCheckBoxDescriptionBinding) : super(binding.root) {
            itemCheckBoxDescriptionBinding = binding
        }

        constructor(binding: ItemExtraFieldBinding) : super(binding.root) {
            itemExtraFieldBinding = binding
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        when (viewType) {
            DetailItemType.DOCUMENT.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_document, parent, false)
                val binding = ItemDocumentBinding.bind(view)
                return ViewHolder(binding)
            }

            DetailItemType.LABEL.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_label, parent, false)
                val binding = ItemLabelBinding.bind(view)
                return ViewHolder(binding)
            }

            DetailItemType.MULTI_EDITTEXT.value, DetailItemType.MULTI_EDITTEXT_WITH_NUMBER.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_multi_edit_text, parent, false)
                val binding = ItemMultiEditTextBinding.bind(view)
                return ViewHolder(binding)
            }

            DetailItemType.MULTI_EDITTEXT.value, DetailItemType.MULTI_EDITTEXT_WITH_DATE.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_multi_edit_text, parent, false)
                val binding = ItemMultiEditTextBinding.bind(view)
                return ViewHolder(binding)
            }

            DetailItemType.SPINNER.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_spinner, parent, false)
                val binding = ItemSpinnerBinding.bind(view)
                return ViewHolder(binding)
            }

            DetailItemType.CHECKBOX.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_check_box_description, parent, false)
                val binding = ItemCheckBoxDescriptionBinding.bind(view)
                return ViewHolder(binding)
            }

            DetailItemType.RADIO_GROUP.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_radio_group, parent, false)
                val binding = ItemRadioGroupBinding.bind(view)
                return ViewHolder(binding)
            }

            DetailItemType.EDIT_TEXT_REMARK.value -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_edit_text_remark, parent, false)
                val binding = ItemEditTextRemarkBinding.bind(view)
                return ViewHolder(binding)
            }

            DetailItemType.EDIT_TEXT_EXTRA_NUMBER_FIELD.value -> {
                return ViewHolder(
                    ItemExtraFieldBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_edit_text, parent, false)
                val binding = ItemEditTextBinding.bind(view)
                return ViewHolder(binding)
            }

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        when (holder.itemViewType) {
            DetailItemType.EDIT_TEXT.value, DetailItemType.EDIT_TEXT_NUMBER.value, DetailItemType.EDIT_TEXT_DECIMAL.value,
            DetailItemType.EDIT_TEXT_EMAIL.value, DetailItemType.EDIT_ALPHA_NUMBER.value -> {
                holder.setIsRecyclable(false)

                holder.itemEditTextBinding?.apply {
                    itemTextInput.apply {
                        val layoutParam = if (layoutParams is RecyclerView.LayoutParams)
                            layoutParams as RecyclerView.LayoutParams
                        else
                            layoutParams as ConstraintLayout.LayoutParams

                        layoutParam.bottomMargin = if (position == list.size - 1) {
                            20
                        } else
                            0
                        layoutParams = layoutParam
                        requestLayout()
                    }

                    val valueToUse = (data.value as? String) ?: ""
                    itemTextInput.hint = data.title
                    itemTextInput.error = data.error
                    itemEditText.apply {
                        isEnabled = data.isEnabled
                        if (data.isFocus == true) {
                            requestFocus()
                        } else {
                            clearFocus()
                        }

                        if (data.isExtraNumberField == true) {
                            if (data.isVisibleIcon) {
                                ivClose.visibility = View.VISIBLE
                                ivClose.setOnClickListener {
                                    listener?.onRemoveExtraFieldClick(position)
                                }
                            } else
                                ivClose.visibility = View.GONE
                        } else {
                            ivClose.visibility = View.GONE
                        }

                        if (data.itemType == DetailItemType.EDIT_TEXT_DECIMAL) {
                            filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(2, 2))
                        } else {
                            if (data.maxLength != null) {
                                filters = arrayOf(InputFilter.LengthFilter(data.maxLength ?: 50))
                            }
                        }

                        text = Editable.Factory.getInstance().newEditable(valueToUse)
                        inputType = when (data.itemType.value) {
                            DetailItemType.EDIT_TEXT_NUMBER.value,
                            -> {
                                InputType.TYPE_CLASS_NUMBER
                            }

                            DetailItemType.EDIT_TEXT_EMAIL.value -> {
                                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                            }

                            DetailItemType.EDIT_TEXT.value -> {

                                InputType.TYPE_CLASS_TEXT
                            }

                            DetailItemType.EDIT_TEXT_DECIMAL.value -> {
                                InputType.TYPE_NUMBER_FLAG_DECIMAL
                            }

                            else -> {
                                InputType.TYPE_NULL
                            }
                        }

                        if (data.itemType == DetailItemType.EDIT_TEXT || data.itemType == DetailItemType.MULTI_EDITTEXT) {

                            val digist =
                                "qwertzuiopasdfghjklyxcvbnm,_,-, ,QWERTYUIOPLKJHGFDSAZXCVBNM"
                            keyListener = DigitsKeyListener.getInstance(digist)
                            setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
                        } else if (data.itemType == DetailItemType.EDIT_ALPHA_NUMBER) {
                            val digist =
                                "qwertzuiopasdfghjklyxcvbnm,_,-, ,QWERTYUIOPLKJHGFDSAZXCVBNM,0123456789"
                            keyListener = DigitsKeyListener.getInstance(digist)
                            setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
                        }

                        doAfterTextChanged {
                            val txt = it.toString().trim()
                            data.value = txt
                            listener?.onTextChanged(position, txt)
                        }
                    }
                }

            }

            DetailItemType.EDIT_TEXT_EXTRA_NUMBER_FIELD.value -> {
                holder.itemExtraFieldBinding?.apply {
                    btnAddNo.apply {
                        visibility = if(data.isEnabled) View.VISIBLE else View.GONE
                        text = data.title
                        setOnClickListener {
                            listener?.clickAddNumber(position)
                        }
                    }
                }
            }

            DetailItemType.EDIT_TEXT_REMARK.value -> {
                holder.itemEditTextRemarkBinding?.apply {
                    val valueToUse = (data.value as? String) ?: ""
                    itemTextInput.hint = data.title
                    itemTextInput.error = data.error
                    itemEditText.apply {
                        isEnabled = data.isEnabled
                        if (data.isFocus == true) {
                            requestFocus()
                        } else {
                            clearFocus()
                        }
                        text = Editable.Factory.getInstance().newEditable(valueToUse)
                        doAfterTextChanged {
                            val txt = it.toString().trim()
                            data.value = txt
                            listener?.onTextChanged(position, txt)
                        }
                    }
                }

            }

            DetailItemType.EDIT_TEXT_DATE.value -> {
                holder.itemEditTextBinding?.apply {
                    val valueToUse = (data.value as? String) ?: ""
                    itemTextInput.hint = data.title
                    itemTextInput.error = data.error
                    itemEditText.apply {
                        isEnabled = data.isEnabled
                        text = Editable.Factory.getInstance().newEditable(valueToUse)
                        inputType = InputType.TYPE_NULL
                        isFocusable = false

                        doAfterTextChanged {
                            val txt = it.toString().trim()
                            data.value = txt
                            listener?.onTextChanged(position, txt)
                        }

                        setOnClickListener {
                            listener?.openDatePicker(position)
                        }

                    }


                }
            }

            DetailItemType.DOCUMENT.value -> {
                holder.itemDocumentBinding?.apply {
                    //                etUploadImage.text = Editable.Factory.getInstance().newEditable(data.title)
                    //                layoutDocument.hint = data.title
                    etUploadImage.setText(data.title)
                    //                etUploadImage.text = Editable.Factory.getInstance().newEditable(data.value.toString())
                    btnView.text = data.btnText
                    btnView.setOnClickListener {
                        listener?.clickOnAddOrViewButton(position)
                    }
                }
            }

            DetailItemType.LABEL.value -> {
                holder.itemLabelBinding?.apply {
                    textLabel.text = data.title
                }
            }

            DetailItemType.MULTI_EDITTEXT.value -> {
                holder.itemMultiEditTextBinding?.apply {
                    val editTextModel = (data.value as? MultiEditTextModel)
                    itemFirstEditText.apply {
                        isEnabled = data.isEnabled
                        text = Editable.Factory.getInstance().newEditable(
                            editTextModel?.value1 ?: ""
                        )
                        hint = editTextModel?.hint1
                        inputType = if (editTextModel?.inputType == TxtInputType.NUMBER) {
                            InputType.TYPE_CLASS_NUMBER
                        } else {
                            InputType.TYPE_CLASS_TEXT
                        }
                        if (editTextModel?.maxLength1 != null) {
                            filters =
                                arrayOf(InputFilter.LengthFilter(editTextModel.maxLength1 ?: 50))
                        }
                    }
                    itemSecondEditText.apply {
                        isEnabled = data.isEnabled
                        text = Editable.Factory.getInstance().newEditable(
                            editTextModel?.value2 ?: ""
                        )
                        hint = editTextModel?.hint2
                        if (editTextModel?.inputType2 == TxtInputType.NUMBER) {
                            inputType = InputType.TYPE_CLASS_NUMBER
                        } else {
                            inputType = InputType.TYPE_CLASS_TEXT
                        }
                        if (editTextModel?.maxLength2 != null) {
                            filters =
                                arrayOf(InputFilter.LengthFilter(editTextModel.maxLength2 ?: 50))
                        }
                    }
                    itemFirstEditText.doOnTextChanged { text, start, before, count ->
                        (data.value as? MultiEditTextModel)?.value1 = text.toString()
                    }
                    itemSecondEditText.doOnTextChanged { text, start, before, count ->
                        (data.value as? MultiEditTextModel)?.value2 = text.toString()
                    }
                }

            }

            DetailItemType.MULTI_EDITTEXT_WITH_NUMBER.value -> {
                holder.setIsRecyclable(false)
                holder.itemMultiEditTextBinding?.apply {
                    val editTextModel = (data.value as? MultiEditTextModel)
                    itemFirstInput.apply {
                        hint = editTextModel?.hint1
                        error = editTextModel?.error1
                    }
                    itemSecondInput.apply {
                        hint = editTextModel?.hint2
                        error = editTextModel?.error2
                    }
                    itemFirstEditText.apply {
                        isEnabled = data.isEnabled
                        inputType = InputType.TYPE_CLASS_NUMBER
                        text = Editable.Factory.getInstance().newEditable(
                            editTextModel?.value1 ?: ""
                        )
                        if (editTextModel?.maxLength1 != null) {
                            filters =
                                arrayOf(InputFilter.LengthFilter(editTextModel.maxLength1 ?: 50))
                        }
                        if (editTextModel?.isFocus1 == true) {
                            requestFocus()
                        } else {
                            clearFocus()
                        }
                    }
                    itemSecondEditText.apply {
                        isEnabled = data.isEnabled
                        inputType = InputType.TYPE_CLASS_NUMBER
                        text = Editable.Factory.getInstance().newEditable(
                            editTextModel?.value2 ?: ""
                        )
                        if (editTextModel?.maxLength2 != null) {
                            filters =
                                arrayOf(InputFilter.LengthFilter(editTextModel.maxLength2 ?: 50))
                        }
                        if (editTextModel?.isFocus2 == true) {
                            requestFocus()
                        } else {
                            clearFocus()
                        }
                    }
                    itemFirstEditText.doOnTextChanged { text, start, before, count ->
                        (data.value as? MultiEditTextModel)?.value1 = text.toString()
                    }
                    itemSecondEditText.doOnTextChanged { text, start, before, count ->
                        (data.value as? MultiEditTextModel)?.value2 = text.toString()
                    }
                }

            }

            DetailItemType.MULTI_EDITTEXT_WITH_DATE.value -> {
                holder.itemMultiEditTextBinding?.apply {
                    val editTextModel = (data.value as? MultiEditTextModel)
                    itemFirstInput.apply {
                        hint = editTextModel?.hint1
                        error = editTextModel?.error1
                    }
                    itemSecondInput.apply {
                        hint = editTextModel?.hint2
                        error = editTextModel?.error2
                    }
                    itemFirstInput.isEnabled = editTextModel?.isEnable1 == true
                    itemSecondInput.isEnabled = editTextModel?.isEnable2 == true
                    itemFirstEditText.apply {
                        isFocusable = false
                        isEnabled = data.isEnabled
                        inputType = InputType.TYPE_NULL
                        text = Editable.Factory.getInstance().newEditable(
                            editTextModel?.value1 ?: ""
                        )
                    }
                    itemSecondEditText.apply {
                        isFocusable = false
                        isEnabled = data.isEnabled
                        inputType = InputType.TYPE_NULL
                        text = Editable.Factory.getInstance().newEditable(
                            editTextModel?.value2 ?: ""
                        )
                    }
                    itemFirstEditText.setOnClickListener {
                        listener?.openDatePicker(position)
                    }
                    itemSecondEditText.setOnClickListener {
                        if (data.isEnabled && editTextModel?.isEnable2 == true) {
                            listener?.openDatePicker2(position)
                        }
                    }
                }

            }

            DetailItemType.SPINNER.value -> {
                holder.setIsRecyclable(false)
                holder.itemSpinnerBinding?.apply {
                    val valueToUse = (data.value as? String) ?: ""
                    val adapterToUse = when (data.spinnerType?.value) {
                        SpinnerType.REFERENCE.value -> {
                            referenceAdapter
                        }

                        SpinnerType.CURRENTLY_EMPLOYEED.value -> {
                            currentlyEmployeedAdapter
                        }

                        SpinnerType.BLOOD_GROUP.value -> {
                            bloodGroupAdapter
                        }

                        SpinnerType.MARITAL_STATUS.value -> {
                            maritalStatusAdapter
                        }

                        SpinnerType.STATE.value -> {
//                            if (data.isEnabled) {
//                                holder.itemView.visibility = View.VISIBLE
//                            } else {
//                                holder.itemView.visibility = View.GONE
//                            }
                            stateNameAdapter
                        }

                        SpinnerType.EDUCATION.value -> {
                            educationalCategoryAdapter
                        }

                        SpinnerType.EDUCATIONAL_STREAM.value -> {
                            educationalStreamAdapter
                        }

                        SpinnerType.RELATION.value -> {
                            relationAdapter
                        }

                        SpinnerType.RESIDING_WITH_YOU.value -> {
                            residingWithYouAdapter
                        }

                        SpinnerType.NOMINEE.value -> {
                            nomineeAdapter
                        }

                        SpinnerType.YESNO.value -> {
                            yesNoAdapter
                        }

                        SpinnerType.BANK_LIST.value -> {
                            bankListAdapter
                        }

                        else -> {
                            genderAdapter
                        }
                    }
                    textInputSpinner.apply {
                        hint = data.title
                        isEnabled = data.isEnabled
                        error = data.error
                        setOnClickListener {
                            textInputDropdown.setAdapter(adapterToUse)
                        }
                    }
                    textInputDropdown.isEnabled = data.isEnabled
                    textInputDropdown.text =
                        Editable.Factory.getInstance().newEditable(valueToUse)
                    textInputDropdown.setAdapter(adapterToUse)
                    textInputDropdown.onItemClickListener =
                        AdapterView.OnItemClickListener { parent, view, posi, id ->
                            data.value = textInputDropdown.text.toString().trim()
                            textInputDropdown.clearFocus()
                            if (null != parent.selectedItem) {
                                listener?.onSpinnerDataSelected(
                                    posi,
                                    parent.selectedItem.toString(), position
                                )
                            } else {
                                listener?.onSpinnerDataSelected(
                                    posi,
                                    parent.adapter.getItem(posi).toString(),
                                    position
                                )
                            }
                        }
                    textInputDropdown.setOnClickListener {
                        textInputDropdown.setAdapter(adapterToUse)
                    }
                }
            }

            DetailItemType.CHECKBOX.value -> {
                holder.itemCheckBoxDescriptionBinding?.apply {
                    val isChecked = (data.value as? Boolean)
                    textDescription.apply {
                        text = data.title
                    }
                    itemCheckBox.setOnClickListener {
                        data.value = itemCheckBox.isChecked
                    }

                }
            }

            DetailItemType.RADIO_GROUP.value -> {
                holder.itemRadioGroupBinding?.apply {
                    textTitle.text = data.title
                    radioGroup.orientation = LinearLayout.HORIZONTAL
                    for (i in 0 until genderRadioList.size) {
                        val radioBtn = RadioButton(context)
                        radioBtn.id = i
                        radioBtn.text = genderRadioList[i]
                        //TODO apply font here.
                        //                    val font = Typeface.createFromAsset(context.assets, "plus_jakarta_display_medium")
                        //                    radioBtn.setTypeface(font)
                        radioBtn.buttonTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    context,
                                    R.color.blue_ribbon
                                )
                            )
                        radioGroup.addView(radioBtn)

                        radioBtn.setOnClickListener {
                            data.value = radioBtn.text
                        }

                    }
                }
            }

            else -> {

            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun refresh(list: ArrayList<DetailModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].itemType.value
    }

    interface DetailListener {
        fun openDatePicker(position: Int) {}
        fun openDatePicker2(position: Int) {}
        fun onSpinnerDataSelected(selectedPosition: Int, data: String, itemPosition: Int) {}
        fun clickOnAddOrViewButton(position: Int) {}
        fun onTextChanged(position: Int, text: String) {}

        fun clearText(position: Int) {}
        fun clickAddNumber(position: Int) {}
        fun onRemoveExtraFieldClick(position: Int) {}
    }

}