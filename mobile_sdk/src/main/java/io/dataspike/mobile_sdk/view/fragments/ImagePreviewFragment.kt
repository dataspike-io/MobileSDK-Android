package io.dataspike.mobile_sdk.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.google.android.material.internal.ViewUtils.dpToPx
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.data.image_caching.ImageCacheManager
import io.dataspike.mobile_sdk.databinding.FragmentImagePreviewBinding
import io.dataspike.mobile_sdk.domain.VerificationManager
import io.dataspike.mobile_sdk.domain.mappers.ERROR_CODE_EXPIRED
import io.dataspike.mobile_sdk.domain.models.UploadImageState
import io.dataspike.mobile_sdk.utils.Utils.launchInMain
import io.dataspike.mobile_sdk.view.IMAGE_TYPE
import io.dataspike.mobile_sdk.view.LIVENESS
import io.dataspike.mobile_sdk.view.POA
import io.dataspike.mobile_sdk.view.POI_BACK
import io.dataspike.mobile_sdk.view.POI_FRONT
import io.dataspike.mobile_sdk.view.view_models.DataspikeViewModelFactory
import io.dataspike.mobile_sdk.view.view_models.ImagePreviewViewModel

//TODO ?
@SuppressLint("RestrictedApi")
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

        viewModel.uploadImage(
            requireContext().getExternalFilesDir(null)?.absolutePath ?: "",
            imageType
        )

        with(viewBinding ?: return) {
            viewBinding?.clSteps?.setStepsState(imageType ?: "")

            val image = ImageCacheManager.getBitmapFromCache(imageType)

            image?.let {
                val roundedDrawable = RoundedBitmapDrawableFactory.create(resources, it).apply {
                    cornerRadius = dpToPx(requireContext(), 14)
                }
                ivTakenImage.setImageDrawable(roundedDrawable)
            }

            with(clBlackTextHeaderLayout) {
                tvTopInstructions.text = when (imageType) {
                    POI_FRONT, POI_BACK -> {
                        requireContext().getString(R.string.poi_instructions_title)
                    }

                    LIVENESS -> {
                        requireContext().getString(R.string.liveness_instructions_bad_title)
                    }

                    POA -> {
                        requireContext().getString(R.string.let_s_upload_proof_of_address_document)
                    }

                    else -> {
                        ""
                    }
                }

                ivBackButton.setOnClickListener {
                    parentFragmentManager.popBackStack()
                }
            }

            with(clButtons) {
                tvRequirements.setOnClickListener {
                    openRequirementsScreen(imageType)
                }

                mbRedo.setOnClickListener {
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun collectUploadImageFlow() {
        launchInMain {

            //TODO move from fragment
//            viewModel.imagePreviewUiState.collect { state ->
//                when(state) {
//                    is ImagePreviewUiState.Success -> ;
//                    is ImagePreviewUiState.Error -> ;
//                }
//
//            }
            viewModel.imageUploadedFlow.collect { uploadImageResult ->
                viewBinding?.clSteps?.setStepsState(
                    imageType ?: "",
                    uploadImageResult is UploadImageState.UploadImageSuccess
                )

                if (uploadImageResult is UploadImageState.UploadImageSuccess) {
                    with(viewBinding ?: return@collect) {
                        clUploadResult.tvUploadSuccessful.visibility = View.VISIBLE
                        clUploadResult.tvUploadWithErrors.visibility = View.GONE
                        clPoiRequirements.root.visibility = View.GONE
                        clLivenessRequirements.root.visibility = View.GONE
                        clPoaRequirements.root.visibility = View.GONE
                        clButtons.tvRequirements.visibility = View.VISIBLE
                        clButtons.mbContinue.visibility = View.VISIBLE

                        clButtons.mbContinue.setOnClickListener {
                            val fragmentToNavigateTo = when (imageType) {
                                POI_FRONT -> {
                                    if (uploadImageResult.detectedTwoSideDocument) {
                                        POIVerificationFragment().apply {
                                            arguments = bundleOf(IMAGE_TYPE to POI_BACK)
                                        }
                                    } else {
                                        when {
                                            VerificationManager.checks.livenessIsRequired -> {
                                                LivenessVerificationFragment()
                                            }

                                            VerificationManager.checks.poaIsRequired -> {
                                                POAChooserFragment()
                                            }

                                            else -> {
                                                VerificationCompleteFragment()
                                            }
                                        }
                                    }
                                }

                                POI_BACK -> {
                                    when {
                                        VerificationManager.checks.livenessIsRequired -> {
                                            LivenessVerificationFragment()
                                        }

                                        VerificationManager.checks.poaIsRequired -> {
                                            POAChooserFragment()
                                        }

                                        else -> {
                                            VerificationCompleteFragment()
                                        }
                                    }
                                }

                                LIVENESS -> {
                                    if (VerificationManager.checks.poaIsRequired) {
                                        POAChooserFragment()
                                    } else {
                                        VerificationCompleteFragment()
                                    }
                                }

                                POA -> {
                                    VerificationCompleteFragment()
                                }

                                else -> {
                                    VerificationCompleteFragment()
                                }
                            }

                            navigateToFragment(fragmentToNavigateTo)
                        }

                        if (imageType == POI_FRONT && uploadImageResult.detectedCountry.isEmpty()) {
                            navigateToFragment(SelectCountryFragment())
                        }
                    }
                } else {
                    val uploadImageError = uploadImageResult as? UploadImageState.UploadImageError

                    if (uploadImageError?.code == ERROR_CODE_EXPIRED) {
                        navigateToFragment(VerificationExpiredFragment())
                    }

                    //TODO fix
                    with(viewBinding ?: return@collect) {
                        clUploadResult.tvUploadSuccessful.visibility = View.GONE
                        clUploadResult.tvUploadWithErrors.visibility = View.VISIBLE
                        clUploadResult.tvUploadWithErrors.text = uploadImageError?.message
                        clButtons.tvRequirements.visibility = View.GONE
                        clButtons.mbContinue.visibility = View.GONE
                        clButtons.mbRedo.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.purple_button_background,
                            null
                        )
                        clButtons.mbRedo.setTextColor(
                            ResourcesCompat.getColor(
                                resources,
                                R.color.white,
                                null
                            )
                        )

                        when (imageType) {
                            POI_FRONT, POI_BACK -> {
                                clPoiRequirements.root.visibility = View.VISIBLE
                            }

                            LIVENESS -> {
                                clLivenessRequirements.root.visibility = View.VISIBLE
                            }

                            POA -> {
                                clPoaRequirements.root.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }
    }
}