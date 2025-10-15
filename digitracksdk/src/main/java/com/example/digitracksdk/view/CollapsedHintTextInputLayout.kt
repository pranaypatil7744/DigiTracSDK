package com.example.digitracksdk.view

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputLayout
import java.lang.Exception
import java.lang.reflect.Method


class CollapsedHintTextInputLayout : TextInputLayout {
    var collapseHintMethod: Method? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    fun init() {
        isHintAnimationEnabled = false
        try {
            collapseHintMethod =
                TextInputLayout::class.java.getDeclaredMethod(
                    "collapseHint",
                    Boolean::class.javaPrimitiveType
                )
            collapseHintMethod?.isAccessible = true
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    override fun invalidate() {
        super.invalidate()
        try {
            collapseHintMethod?.invoke(this, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}