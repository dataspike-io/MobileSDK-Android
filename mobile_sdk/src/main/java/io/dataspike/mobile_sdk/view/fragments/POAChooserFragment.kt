package io.dataspike.mobile_sdk.view.fragments

import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.data.image_caching.ImageCacheManager
import io.dataspike.mobile_sdk.databinding.FragmentPoaChooserBinding
import io.dataspike.mobile_sdk.utils.Utils.toBitmap
import io.dataspike.mobile_sdk.utils.acceptableForUploadFileFormats
import io.dataspike.mobile_sdk.view.IMAGE_TYPE
import io.dataspike.mobile_sdk.view.POA


internal class POAChooserFragment : BaseFragment() {

    private var viewBinding: FragmentPoaChooserBinding? = null
    private var getFile: ActivityResultLauncher<Array<String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initGetFile()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentPoaChooserBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding ?: return) {
            with(clBlackTextHeaderLayout) {
                tvTopInstructions.text =
                    requireContext().getString(R.string.poa_instructions_title)
                ivBackButton.setOnClickListener {
                    parentFragmentManager.popBackStack()
                }
            }

            with(clButtons) {
                tvRequirements.setOnClickListener {
                    openRequirementsScreen(POA)
                }
                mbTakeAPhoto.setOnClickListener {
                    goToFragment(POAVerificationFragment())
                }
                mbUpload.setOnClickListener {
                    pickFile()
                }
            }
        }
    }

    private fun initGetFile() {
        getFile = registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->
            uri ?: return@registerForActivityResult

            activity?.contentResolver?.query(
                uri,
                null,
                null,
                null,
                null
            ).use { cursor ->
                val fileSize: Long
                val contentResolver = requireContext().contentResolver
                val fileType = contentResolver?.getType(uri) ?: return@registerForActivityResult
                val sizeIndex = cursor?.getColumnIndex(OpenableColumns.SIZE)

                cursor?.moveToFirst()

                fileSize = cursor?.getLong(sizeIndex ?: 0) ?: 0L

                if (acceptableForUploadFileFormats.contains(fileType)) {
                    if (fileSize in 100_000..8_000_000) {
                        contentResolver.openInputStream(uri).use { inputStream ->
                            inputStream ?: return@registerForActivityResult

                            val display = activity?.windowManager?.defaultDisplay

                            ImageCacheManager.putBitmapIntoCache(
                                POA,
                                inputStream.toBitmap(
                                    fileType,
                                    display?.width,
                                    display?.height
                                )
                            )

                            goToFragment(
                                ImagePreviewFragment().apply {
                                    arguments = bundleOf(IMAGE_TYPE to POA)
                                }
                            )
                        }
                    } else {
                        makeToast("File size must be between 100KB and 8MB")
                    }
                } else {
                    makeToast("Please select a supported file format")
                }

                cursor?.close()
            }
        }
    }

    private fun pickFile() {
        getFile?.launch(acceptableForUploadFileFormats)
    }
}