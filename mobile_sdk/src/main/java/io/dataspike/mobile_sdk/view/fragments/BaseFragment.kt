package io.dataspike.mobile_sdk.view.fragments

import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.dataspike.mobile_sdk.DataspikeVerificationStatus
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.passVerificationCompletedResult
import io.dataspike.mobile_sdk.utils.Utils.launchInMain
import io.dataspike.mobile_sdk.utils.Utils.visible
import io.dataspike.mobile_sdk.view.REQUIREMENTS_TYPE
import io.dataspike.mobile_sdk.view.view_models.BaseViewModel

internal abstract class BaseFragment: Fragment() {

    private val viewModel: BaseViewModel by viewModels()

    fun collectLoading(viewModel: BaseViewModel, loadingView: LinearLayout?) {
        launchInMain {
            viewModel.loadingFlow.collect { isVisible ->
                loadingView?.visible(isVisible)
            }
        }
    }

    fun openRequirementsScreen(requirementsType: String?) {
        activity
            ?.supportFragmentManager
            ?.beginTransaction()
            ?.add(
                R.id.fcv_container,
                RequirementsFragment().apply {
                    arguments = bundleOf(REQUIREMENTS_TYPE to requirementsType)
                }
            )
            ?.addToBackStack(null)
            ?.commit()
    }

    fun navigateToFragment(fragment: BaseFragment, add: Boolean? = true) {
        if (add == true) {
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.add(
                    R.id.fcv_container,
                    fragment
                )
                ?.addToBackStack(null)
                ?.commit()
        } else {
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(
                    R.id.fcv_container,
                    fragment
                )
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    fun makeToast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    fun setVerificationStatusAndFinish(
        dataspikeVerificationStatus: DataspikeVerificationStatus
    ) {
        passVerificationCompletedResult(dataspikeVerificationStatus)
        activity?.finish()
    }
}