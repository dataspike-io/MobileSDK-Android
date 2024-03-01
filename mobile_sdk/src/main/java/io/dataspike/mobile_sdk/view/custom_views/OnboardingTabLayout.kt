package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.ImageView
import com.google.android.material.tabs.TabLayout
import io.dataspike.mobile_sdk.utils.dpToPx

internal class OnboardingTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TabLayout(context, attrs, defStyleAttr) {

    private fun createTabItem(
        color: Int,
    ): ImageView {
        val imageView = ImageView(context)
        val params = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        imageView.layoutParams = params

        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setSize(dpToPx(12F).toInt(), dpToPx(12F).toInt())
            this.color = ColorStateList.valueOf(color)
        }

        imageView.setImageDrawable(drawable)

        return imageView
    }

    fun setup(
        selectedColor: Int,
        deselectedColor: Int,
    ) {
        for (i in 0 until tabCount) {
            val tab = getTabAt(i)
            tab?.customView = if (tab?.isSelected == true) {
                createTabItem(selectedColor)
            } else {
                createTabItem(deselectedColor)
            }
        }

        addOnTabSelectedListener(object : OnTabSelectedListener {

            override fun onTabSelected(tab: Tab?) {
                tab?.customView = createTabItem(selectedColor)
            }

            override fun onTabUnselected(tab: Tab?) {
                tab?.customView = createTabItem(deselectedColor)
            }

            override fun onTabReselected(tab: Tab?) = Unit
        })
    }
}