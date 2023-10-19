package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import io.dataspike.mobile_sdk.databinding.CompleteTryAgainLayoutBinding

internal class CompleteTryAgainLayout @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null
): ConstraintLayout(context, attrSet) {

    private val viewBinding = CompleteTryAgainLayoutBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    fun setup(
        completeAction: () -> Unit,
        tryAgainAction: (() -> Unit)? = null,
        verificationFailed: Boolean? = false,
    ) {
        with(viewBinding) {
            mbComplete.setOnClickListener {
                completeAction.invoke()
            }

            with(mbTryAgain) {
                isVisible = verificationFailed == true
                setOnClickListener {
                    tryAgainAction?.invoke()
                }
            }
        }
    }
}