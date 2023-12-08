package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import io.dataspike.mobile_sdk.databinding.VerificationResultLayoutBinding

internal class VerificationResultLayout @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null
): ConstraintLayout(context, attrSet) {

    init {
        VerificationResultLayoutBinding.inflate(LayoutInflater.from(context), this)
    }
}