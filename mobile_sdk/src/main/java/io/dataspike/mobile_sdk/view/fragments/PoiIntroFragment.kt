package io.dataspike.mobile_sdk.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.FragmentPoiIntroBinding
import io.dataspike.mobile_sdk.view.IMAGE_TYPE
import io.dataspike.mobile_sdk.view.POI_FRONT

internal class PoiIntroFragment: BaseFragment() {

    private var viewBinding: FragmentPoiIntroBinding? = null

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
            roblButtons.setup(
                stringResId = R.string._continue,
                continueButtonAction = ::navigateToPoiVerificationFragment,
            ) {
                openRequirementsScreen(POI_FRONT)
            }
            hlHeader.setup(
                popBackStackAction = ::popBackStack,
                stringResId = R.string.poi_intro_instructions,
                colorResId = R.color.black,
            )
            clSteps.setup(step = POI_FRONT)
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