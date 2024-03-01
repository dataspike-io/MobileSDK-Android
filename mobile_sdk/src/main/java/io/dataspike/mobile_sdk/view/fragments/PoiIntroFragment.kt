package io.dataspike.mobile_sdk.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.FragmentPoiIntroBinding
import io.dataspike.mobile_sdk.utils.setup
import io.dataspike.mobile_sdk.view.IMAGE_TYPE
import io.dataspike.mobile_sdk.view.POI_FRONT
import io.dataspike.mobile_sdk.view.UIManager
import io.dataspike.mobile_sdk.view.ui_models.ButtonConfigModel

internal class PoiIntroFragment: BaseFragment() {

    private var viewBinding: FragmentPoiIntroBinding? = null
    private val imageLink = UIManager.getUiConfig().links.intro.poi

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentPoiIntroBinding.inflate(inflater, container, false)

        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding ?: return) {
            bButtons.setup(
                topButtonConfig = ButtonConfigModel(
                    isVisible = true,
                    isEnabled = true,
                    isTransparent = false,
                    backgroundColors = Pair(palette.mainColor, palette.lightMainColor),
                    text = getString(R.string._continue),
                    textColors = Pair(palette.backgroundColor, palette.backgroundColor),
                    action = ::navigateToPoiVerificationFragment,
                ),
                requirementsIsVisible = true,
                requirementsAction = { openRequirementsScreen(POI_FRONT) },
            )
            hlHeader.setup(
                popBackStackAction = ::popBackStack,
                stringResId = R.string.poi_intro_instructions,
                colorInt = typography.header.textColor,
            )
            clSteps.setup(step = POI_FRONT)
            Glide
                .with(acivPoiExample)
                .load(imageLink)
                .into(acivPoiExample)
            with(tvPoiIntroText) {
                setup(
                    font = typography.bodyTwo.font,
                    textSize = typography.bodyTwo.textSize,
                    textColor = typography.bodyTwo.textColor,
                )
            }
        }
    }

    private fun navigateToPoiVerificationFragment() {
        navigateToFragment(
            PoiVerificationFragment().apply {
                arguments = bundleOf(IMAGE_TYPE to POI_FRONT)
            }
        )
    }
}