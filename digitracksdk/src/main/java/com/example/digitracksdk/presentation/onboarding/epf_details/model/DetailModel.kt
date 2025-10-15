package com.example.digitracksdk.presentation.onboarding.epf_details.model

import androidx.annotation.Keep


/***
 * For managing EPF ,ESIC Details, Add Person (Reference ) Details
 */

@Keep
data class DetailModel(
    var title: String? = "",
    var value: Any? = null,
    var isEnabled: Boolean = false,
    var isVisibleIcon: Boolean = false,
    var itemType: DetailItemType = DetailItemType.EDIT_TEXT,
    var icon: Int? = 0,
    var spinnerType: SpinnerType? = null,
    var error: String? = null,
    var btnText: String? = "",
    var maxLength: Int? = null,
    var isFocus:Boolean? = false,
    var isExtraNumberField : Boolean?= false,

    )

enum class SpinnerType(val value: Int) {
    GENDER(1),
    REFERENCE(2),
    CURRENTLY_EMPLOYEED(3),
    RELATION(4),
    RESIDING_WITH_YOU(5),
    NOMINEE(6),
    YESNO(7),
    EDUCATION(8),
    BLOOD_GROUP(9),
    MARITAL_STATUS(10),
    BANK_LIST(11),
    EDUCATIONAL_STREAM(12),
    STATE(13)
}

enum class DetailItemType(val value: Int) {
    LABEL(1),
    EDIT_TEXT(2),
    EDIT_TEXT_DATE(3),
    EDIT_TEXT_NUMBER(4),
    EDIT_TEXT_DECIMAL(13),
    EDIT_ALPHA_NUMBER(14),
    EDIT_TEXT_REMARK(15),
    EDIT_TEXT_EMAIL(5),
    SPINNER(6),
    DOCUMENT(7),
    MULTI_EDITTEXT(8),
    MULTI_EDITTEXT_WITH_DATE(11),
    CHECKBOX(9),
    RADIO_GROUP(10),
    MULTI_EDITTEXT_WITH_NUMBER(12),
    EDIT_TEXT_EXTRA_NUMBER_FIELD(16),
}


