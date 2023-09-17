package io.dataspike.mobile_sdk.view.fragments

import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.utils.Utils.launchInMain
import io.dataspike.mobile_sdk.utils.Utils.visible
import io.dataspike.mobile_sdk.view.view_models.BaseViewModel

internal const val REQUIREMENTS_TYPE = "requirements_type"
internal const val POI_REQUIREMENTS = "POI_requirements"
internal const val POA_REQUIREMENTS = "poa_requirements"
internal const val LIVENESS_REQUIREMENTS = "liveness_requirements"

internal abstract class BaseFragment: Fragment() {

    private val viewModel: BaseViewModel by viewModels()

    fun collectLoading(viewModel: BaseViewModel, loadingView: LinearLayout?) {
        launchInMain {
            viewModel.loadingFlow.collect { isVisible ->
                loadingView?.visible(isVisible)
            }
        }
    }

    fun openRequirementsScreen(requirementsType: String) {
        activity
            ?.supportFragmentManager
            ?.beginTransaction()
            ?.add(
                R.id.container,
                RequirementsFragment().apply {
                    arguments = bundleOf(REQUIREMENTS_TYPE to requirementsType)
                }
            )
            ?.addToBackStack(null)
            ?.commit()
    }
}