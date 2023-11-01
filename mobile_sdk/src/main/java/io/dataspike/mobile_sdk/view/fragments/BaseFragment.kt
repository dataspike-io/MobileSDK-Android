package io.dataspike.mobile_sdk.view.fragments

import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.domain.passVerificationCompletedResult
import io.dataspike.mobile_sdk.utils.launchInMain
import io.dataspike.mobile_sdk.view.IMAGE_TYPE
import io.dataspike.mobile_sdk.view.LIVENESS
import io.dataspike.mobile_sdk.view.POA
import io.dataspike.mobile_sdk.view.POI
import io.dataspike.mobile_sdk.view.POI_BACK
import io.dataspike.mobile_sdk.view.POI_FRONT
import io.dataspike.mobile_sdk.view.REQUIREMENTS_TYPE
import io.dataspike.mobile_sdk.view.VERIFICATION_COMPLETED
import io.dataspike.mobile_sdk.view.view_models.BaseViewModel

internal abstract class BaseFragment: Fragment() {

    protected fun collectLoading(viewModel: BaseViewModel, loadingView: LinearLayout?) {
        launchInMain {
            viewModel.loadingFlow.collect { isVisible ->
                loadingView?.isVisible = isVisible
            }
        }
    }

    protected fun makeToast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    protected fun passVerificationStatusAndFinish() {
        passVerificationCompletedResult()
        requireActivity().finish()
    }

    protected fun finishActivity() {
        activity?.finish()
    }

    protected fun retryVerification() {
        requireActivity().supportFragmentManager.popBackStack(
            requireActivity().supportFragmentManager.getBackStackEntryAt(0).id,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    protected open fun popBackStack() {
        parentFragmentManager.popBackStack()
    }

    protected fun openRequirementsScreen(requirementsType: String?) {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .add(
                R.id.fcv_container,
                RequirementsFragment().apply {
                    arguments = bundleOf(REQUIREMENTS_TYPE to requirementsType)
                }
            )
            .addToBackStack(null)
            .commit()
    }

    protected fun navigateToFragment(fragment: BaseFragment?, useAdd: Boolean? = true) {
        fragment ?: return

        if (useAdd == true) {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.fcv_container,
                    fragment
                )
                .addToBackStack(null)
                .commit()
        } else {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fcv_container,
                    fragment
                )
                .addToBackStack(null)
                .commit()
        }
    }

    protected fun getStringResFromImageType(imageType: String?, screenType: String? = null): Int {
        return if (screenType == REQUIREMENTS_SCREEN) {
            when (imageType) {
                POI_FRONT, POI_BACK -> { R.string.poi_requirements_title }

                LIVENESS -> { R.string.liveness_requirements_title }

                POA -> { R.string.poa_requirements_title }

                else -> { R.string.requirements }
            }
        } else {
            when (imageType) {
                POI_FRONT -> { R.string.front_photo_instructions }

                POI_BACK -> { R.string.back_photo_instructions }

                LIVENESS -> { R.string.liveness_instructions_bad_title }

                POA -> { R.string.let_s_upload_proof_of_address_document }

                else -> { R.string.take_a_photo }
            }
        }
    }

    protected fun getFragmentFromType(fragmentType: String): BaseFragment? {
        return when (fragmentType) {
            POI, POI_FRONT -> {
                PoiIntroFragment()
            }

            POI_BACK -> {
                PoiVerificationFragment().apply {
                    arguments = bundleOf(IMAGE_TYPE to POI_BACK)
                }
            }

            LIVENESS -> {
                LivenessVerificationFragment()
            }

            POA -> {
                PoaChooserFragment()
            }

            VERIFICATION_COMPLETED -> {
                VerificationCompleteFragment()
            }

            else -> {
                null
            }
        }
    }
}