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

internal class POIIntroFragment: BaseFragment() {

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
            with(clButtons) {
                mbContinue.text = requireContext().getString(R.string._continue)
                mbContinue.setOnClickListener {
                    navigateToFragment(
                        POIVerificationFragment().apply {
                            arguments = bundleOf(IMAGE_TYPE to POI_FRONT)
                        }
                    )
                }
                tvRequirements.setOnClickListener {
                    openRequirementsScreen(POI_FRONT)
                }
            }

            with(clBlackTextHeaderLayout) {
                tvTopInstructions.text = requireContext().getString(
                    R.string.place_the_document_in_frame_and_take_a_photo
                )
                ivBackButton.setOnClickListener {
                    parentFragmentManager.popBackStack()
                }
            }

            clSteps.setStepsState(POI_FRONT)
        }
    }
}