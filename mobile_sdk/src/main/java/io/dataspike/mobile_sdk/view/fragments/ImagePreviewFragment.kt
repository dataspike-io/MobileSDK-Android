package io.dataspike.mobile_sdk.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import io.dataspike.mobile_sdk.databinding.FragmentImagePreviewBinding
import io.dataspike.mobile_sdk.utils.launchInMain
import io.dataspike.mobile_sdk.view.IMAGE_TYPE
import io.dataspike.mobile_sdk.view.custom_views.UPLOAD_SUCCESSFUL
import io.dataspike.mobile_sdk.view.custom_views.UPLOAD_WITH_ERRORS
import io.dataspike.mobile_sdk.view.ui_models.UploadImageUiState
import io.dataspike.mobile_sdk.view.view_models.DataspikeViewModelFactory
import io.dataspike.mobile_sdk.view.view_models.ImagePreviewViewModel

internal class ImagePreviewFragment: BaseFragment() {

    private var viewBinding: FragmentImagePreviewBinding? = null
    private val viewModel: ImagePreviewViewModel by viewModels { DataspikeViewModelFactory() }
    private var imageType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        imageType = arguments?.getString(IMAGE_TYPE)
        viewBinding = FragmentImagePreviewBinding.inflate(inflater, container, false)

        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectUploadImageFlow()
        collectLoading(viewModel, viewBinding?.llLoadingView?.root)
        setInitialUiState()
        viewModel.uploadImage(imageType)
    }

    private fun setInitialUiState() {
        with(viewBinding ?: return) {
            clSteps.setup(step = imageType)
            rivTakenImage.setImageDrawableFromBitmap(viewModel.getBitmapFromCache(imageType))
            hlHeader.setup(
                popBackStackAction = ::popBackStack,
                stringResId = getStringResFromImageType(imageType),
            )
        }
    }

    private fun updateUiState(uploadImageUiState: UploadImageUiState) {
        with(viewBinding ?: return) {
            clSteps.setup(
                step = imageType,
                stepIsSuccessful = uploadImageUiState is UploadImageUiState.UploadImageUiSuccess
            )

            when (uploadImageUiState) {
                is UploadImageUiState.UploadImageUiSuccess -> {
                    urlUploadResult.setup(uploadStatus = UPLOAD_SUCCESSFUL)
                    rrclButtons.setup(
                        popBackStackAction = ::popBackStack,
                        openRequirementsScreenAction = {
                            openRequirementsScreen(imageType)
                        },
                        continueButtonAction = {
                            navigateToFragment(
                                getFragmentFromType(
                                    uploadImageUiState.fragmentToNavigateToOnContinue
                                )
                            )
                        },
                    )

                    if (uploadImageUiState.shouldNavigateToSelectCountryFragment) {
                        navigateToFragment(SelectCountryFragment())
                    }
                }

                is UploadImageUiState.UploadImageUiError -> {
                    when {
                        uploadImageUiState.isExpired -> {
                            navigateToFragment(VerificationExpiredFragment())
                        }

                        uploadImageUiState.tooManyAttempts -> {
                            navigateToFragment(
                                getFragmentFromType(uploadImageUiState.fragmentToNavigateTo)
                            )
                        }

                        else -> {
                            urlUploadResult.setup(
                                uploadStatus = UPLOAD_WITH_ERRORS,
                                message = uploadImageUiState.errorMessage,
                            )
                            rrclButtons.setup(
                                popBackStackAction = ::popBackStack,
                                uploadSuccessful = false,
                            )
                            rlRequirements.setup(imageType = imageType)
                        }
                    }
                }
            }
        }
    }

    private fun collectUploadImageFlow() {
        launchInMain {
            viewModel.imageUploadedFlow.collect { uploadImageUiState ->
                updateUiState(uploadImageUiState)
            }
        }
    }
}