package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import io.dataspike.mobile_sdk.databinding.HeaderLayoutBinding

internal class HeaderLayout @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null
): ConstraintLayout(context, attrSet) {

    private val viewBinding = HeaderLayoutBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    fun setup(
        popBackStackAction: () -> Unit,
        @StringRes stringResId: Int,
        @ColorRes colorResId: Int,
    ) {
        with(viewBinding) {
            with(tvTopInstructions) {
                setTextColor(
                    ResourcesCompat.getColor(resources, colorResId, null)
                )
                text = context.getString(stringResId)
            }

            with(ivBackButton) {
                ivBackButton.setColorFilter(
                    ResourcesCompat.getColor(resources, colorResId, null)
                )
                setOnClickListener {
                    popBackStackAction.invoke()
                }
            }
        }
    }
}