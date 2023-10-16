package io.dataspike.mobile_sdk.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.FragmentRequirementsBinding
import io.dataspike.mobile_sdk.view.LIVENESS
import io.dataspike.mobile_sdk.view.POA
import io.dataspike.mobile_sdk.view.POI_BACK
import io.dataspike.mobile_sdk.view.POI_FRONT
import io.dataspike.mobile_sdk.view.REQUIREMENTS_TYPE

internal class RequirementsFragment: BaseFragment() {

    private var viewBinding: FragmentRequirementsBinding? = null
    private var requirementsType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentRequirementsBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requirementsType = arguments?.getString(REQUIREMENTS_TYPE)

        with(viewBinding ?: return) {
            clBlackTextHeaderLayout.ivBackButton.setOnClickListener {
                parentFragmentManager.popBackStack()
            }

            mbContinue.setOnClickListener {
                parentFragmentManager.popBackStack()
            }

            clBlackTextHeaderLayout.tvTopInstructions.text = when (requirementsType) {
                POI_FRONT, POI_BACK -> {
                    clPoiRequirements.root.visibility = View.VISIBLE
                    requireContext().getString(R.string.poi_requirements_title)
                }

                LIVENESS -> {
                    clLivenessRequirements.root.visibility = View.VISIBLE
                    requireContext().getString(R.string.liveness_requirements_title)
                }

                POA -> {
                    clPoaRequirements.root.visibility = View.VISIBLE
                    requireContext().getString(R.string.poa_requirements_title)
                }


                else -> {
                    ""
                }
            }
        }
    }
}