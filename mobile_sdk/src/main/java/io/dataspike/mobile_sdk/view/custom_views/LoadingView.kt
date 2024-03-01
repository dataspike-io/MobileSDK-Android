package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import io.dataspike.mobile_sdk.databinding.LoadingViewBinding
import io.dataspike.mobile_sdk.view.UIManager

internal class LoadingView(
    context: Context,
    attrs: AttributeSet? = null
): LinearLayout(
    context,
    attrs
)  {

    private val viewBinding = LoadingViewBinding.inflate(
        LayoutInflater.from(context),
        this
    )
    private val palette = UIManager.getUiConfig().theme.palette

    init {
        with(viewBinding) {
            clParent.setBackgroundColor(palette.backgroundColor)
            pbIndicator.indeterminateDrawable.colorFilter =
                PorterDuffColorFilter(palette.mainColor, PorterDuff.Mode.SRC_IN)
        }
    }

    fun setup(
        backgroundColor: Int,
        indicatorColor: Int,
    ) {
        with(viewBinding) {
            clParent.setBackgroundColor(backgroundColor)
            pbIndicator.indeterminateDrawable.setTint(indicatorColor)
        }
    }
}