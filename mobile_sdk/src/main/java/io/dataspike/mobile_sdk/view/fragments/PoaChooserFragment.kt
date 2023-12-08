package io.dataspike.mobile_sdk.view.fragments

import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.FragmentPoaChooserBinding
import io.dataspike.mobile_sdk.utils.acceptableForUploadFileFormats
import io.dataspike.mobile_sdk.utils.displayMetrics
import io.dataspike.mobile_sdk.utils.toBitmap
import io.dataspike.mobile_sdk.view.IMAGE_TYPE
import io.dataspike.mobile_sdk.view.POA
import io.dataspike.mobile_sdk.view.view_models.BaseViewModel
import io.dataspike.mobile_sdk.view.view_models.DataspikeViewModelFactory


internal class PoaChooserFragment : BaseFragment() {

    private var viewBinding: FragmentPoaChooserBinding? = null
    private val viewModel by viewModels<BaseViewModel> { DataspikeViewModelFactory() }
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
            hlHeaderLayout.setup(
                popBackStackAction = ::popBackStack,
                stringResId = R.string.let_s_upload_proof_of_address_document,
            )

            rtapulButtons.setup(
                openRequirementsAction = ::openPoaRequirementsScreen,
                takeAPhotoAction = {
                    navigateToFragment(PoaVerificationFragment())
                },
                pickFileAction = ::pickFile,
            )

            clSteps.setup(step = POA)
        }
    }

    private fun initGetFile() {
        getFile = registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->
            uri ?: return@registerForActivityResult

            requireActivity().contentResolver.query(
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

                            val width = displayMetrics.widthPixels
                            val height = (width * 1.3).toInt()

                            viewModel.putBitmapIntoCache(
                                POA,
                                inputStream.toBitmap(
                                    fileType,
                                    width,
                                    height
                                )
                            )

                            navigateToFragment(
                                ImagePreviewFragment().apply {
                                    arguments = bundleOf(IMAGE_TYPE to POA)
                                }
                            )
                        }
                    } else {
                        makeToast(getString(R.string.file_size_must_be_between_100kb_and_8mb))
                    }
                } else {
                    makeToast(getString(R.string.please_select_a_supported_file_format))
                }

                cursor?.close()
            }
        }
    }

    private fun pickFile() {
        getFile?.launch(acceptableForUploadFileFormats)
    }

    private fun openPoaRequirementsScreen() {
        openRequirementsScreen(POA)
    }
}