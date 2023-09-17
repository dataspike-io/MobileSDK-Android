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
import io.dataspike.mobile_sdk.domain.VerificationChecksManager
import io.dataspike.mobile_sdk.domain.models.UploadImageState
import io.dataspike.mobile_sdk.utils.Utils.launchInMain
import io.dataspike.mobile_sdk.view.IMAGE_TYPE
import io.dataspike.mobile_sdk.view.LIVENESS
import io.dataspike.mobile_sdk.view.POA
import io.dataspike.mobile_sdk.view.POI_BACK
import io.dataspike.mobile_sdk.view.POI_FRONT
import io.dataspike.mobile_sdk.view.view_models.DataspikeViewModelFactory
import io.dataspike.mobile_sdk.view.view_models.ImagePreviewViewModel
//!!!
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
            imageType ?: ""
        )

        with(viewBinding ?: return) {
            var requirementsType: String = POI_REQUIREMENTS
            val image = when (imageType) {
                POI_FRONT -> {
                    ImageCacheManager.poiFront
                }

                POI_BACK -> {
                    ImageCacheManager.poiBack
                }

                LIVENESS -> {
                    requirementsType = LIVENESS_REQUIREMENTS
                    ImageCacheManager.liveness
                }

                POA -> {
                    requirementsType = POA_REQUIREMENTS
                    ImageCacheManager.poa
                }

                else -> null
            }

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
                        requireContext().getString(R.string.liveness_instructions_title)
                    }
                    POA -> {
                        requireContext().getString(R.string.poa_instructions_title)
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
                    openRequirementsScreen(requirementsType)
                }

                tvRedoButton.setOnClickListener {
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
                if (uploadImageResult is UploadImageState.UploadImageSuccess) {
                    with(viewBinding ?: return@collect) {
                        clUploadResult.tvUploadSuccessful.visibility = View.VISIBLE
                        clUploadResult.tvUploadWithErrors.visibility = View.GONE
                        clPoiRequirements.root.visibility = View.GONE
                        clLivenessRequirements.root.visibility = View.GONE
                        clPoaRequirements.root.visibility = View.GONE
                        clButtons.tvRequirements.visibility = View.VISIBLE
                        clButtons.tvContinueButton.visibility = View.VISIBLE

                        clButtons.tvContinueButton.setOnClickListener {
                            val fragmentToGoTo = when (imageType) {
                                POI_FRONT -> {
                                    if (uploadImageResult.detectedTwoSideDocument) {
                                        POIVerificationFragment().apply {
                                            arguments = bundleOf(IMAGE_TYPE to POI_BACK)
                                        }
                                    } else {
                                        when {
                                            VerificationChecksManager.checks.livenessIsRequired -> {
                                                LivenessVerificationFragment()
                                            }

                                            VerificationChecksManager.checks.poaIsRequired -> {
                                                POAVerificationFragment()
                                            }

                                            else -> {
                                                VerificationCompleteFragment()
                                            }
                                        }
                                    }
                                }

                                POI_BACK -> {
                                    when {
                                        VerificationChecksManager.checks.livenessIsRequired -> {
                                            LivenessVerificationFragment()
                                        }

                                        VerificationChecksManager.checks.poaIsRequired -> {
                                            POAVerificationFragment()
                                        }

                                        else -> {
                                            VerificationCompleteFragment()
                                        }
                                    }
                                }

                                LIVENESS -> {
                                    if (VerificationChecksManager.checks.poaIsRequired) {
                                        POAVerificationFragment()
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

                            activity
                                ?.supportFragmentManager
                                ?.beginTransaction()
                                ?.add(
                                    R.id.container,
                                    fragmentToGoTo
                                )
                                ?.addToBackStack(null)
                                ?.commit()
                        }
                    }
                } else {
                    //TODO fix
                    with(viewBinding ?: return@collect) {
                        clUploadResult.tvUploadSuccessful.visibility = View.GONE
                        clUploadResult.tvUploadWithErrors.visibility = View.VISIBLE
                        clUploadResult.tvUploadWithErrors.text = (uploadImageResult as UploadImageState.UploadImageError).message
                        clButtons.tvRequirements.visibility = View.GONE
                        clButtons.tvContinueButton.visibility = View.GONE
                        clButtons.tvRedoButton.background = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.purple_button_background,
                            null
                        )
                        clButtons.tvRedoButton.setTextColor(
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