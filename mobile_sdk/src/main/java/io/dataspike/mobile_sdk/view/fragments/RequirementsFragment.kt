package io.dataspike.mobile_sdk.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.FragmentRequirementsBinding
import io.dataspike.mobile_sdk.view.REQUIREMENTS_TYPE

internal const val REQUIREMENTS_SCREEN = "requirements_screen"

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
            bthlHeader.setup(
                popBackStackAction = ::popBackStack,
                stringResId = getStringResFromImageType(requirementsType, REQUIREMENTS_SCREEN),
                colorResId = R.color.black,
            )
            rlRequirements.setup(imageType = requirementsType)
            mbContinue.setOnClickListener { popBackStack() }
        }
    }
}