package io.dataspike.mobile_sdk.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.FragmentVerificationCompleteBinding
import io.dataspike.mobile_sdk.domain.models.EmptyState
import io.dataspike.mobile_sdk.utils.Utils.launchInMain
import io.dataspike.mobile_sdk.view.view_models.DataspikeViewModelFactory
import io.dataspike.mobile_sdk.view.view_models.VerificationCompleteViewModel

internal class VerificationCompleteFragment : BaseFragment() {

    private var viewBinding: FragmentVerificationCompleteBinding? = null
    private val viewModel: VerificationCompleteViewModel by viewModels { DataspikeViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentVerificationCompleteBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectUploadImageFlow()
        viewModel.proceedWithVerification()

        with(viewBinding?.clCompleteTryAgainLayout ?: return) {
            tvCompleteButton.setOnClickListener {
                activity?.finish()
            }
        }
    }

    private fun collectUploadImageFlow() {
        launchInMain {
            viewModel.proceedWithVerificationFlow.collect { result ->
                if (result is EmptyState.EmptyStateError) {
                    with(viewBinding ?: return@collect) {
                    clCompleteTryAgainLayout.tvTryAgainButton.visibility = View.VISIBLE
                    clCompleteTryAgainLayout.tvTryAgainButton.setOnClickListener {
                        activity?.supportFragmentManager?.popBackStack(
                            activity?.supportFragmentManager?.getBackStackEntryAt(0)?.id ?: -1,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                    }
                    ivCheckmark.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.red_x,
                            null
                        )
                    )
                    tvVerificationCompleteTitle.text =
                        requireContext().getString(R.string.verification_failed)
                    tvVerificationCompleteDescription.text =
                        requireContext().getString(R.string.something_went_wrong_this_time)
                }

//                    Toast.makeText(
//                        requireContext(),
//                        "${result.error}: ${result.details}",
//                        Toast.LENGTH_LONG
//                    ).show()
                }
            }
        }
    }
}