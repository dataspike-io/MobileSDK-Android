package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import io.dataspike.mobile_sdk.databinding.RequirementsTakeAPhotoUploadLayoutBinding

internal class RequirementsTakeAPhotoUploadLayout @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null
): ConstraintLayout(context, attrSet) {

    private val viewBinding = RequirementsTakeAPhotoUploadLayoutBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    fun setup(
        openRequirementsAction: () -> Unit,
        takeAPhotoAction: () -> Unit,
        pickFileAction: () -> Unit,
    ) {
        with(viewBinding) {
            tvRequirements.setOnClickListener {
                openRequirementsAction.invoke()
            }
            mbTakeAPhoto.setOnClickListener {
                takeAPhotoAction.invoke()
            }
            mbUpload.setOnClickListener {
                pickFileAction.invoke()
            }
        }
    }
}