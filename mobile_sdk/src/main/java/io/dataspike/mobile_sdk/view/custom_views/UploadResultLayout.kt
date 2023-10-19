package io.dataspike.mobile_sdk.view.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import io.dataspike.mobile_sdk.databinding.UploadResultLayoutBinding

internal const val UPLOAD_SUCCESSFUL = 0
internal const val UPLOAD_WITH_ERRORS = 1
internal const val NEED_TO_SELECT_COUNTRY = 2

internal class UploadResultLayout @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet? = null
): ConstraintLayout(context, attrSet) {

    private val viewBinding = UploadResultLayoutBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    fun setup(uploadStatus: Int, errorMessage: String? = null) {
        with(viewBinding) {
            when (uploadStatus) {
                UPLOAD_SUCCESSFUL -> {
                    tvUploadSuccessful.visibility = VISIBLE
                }

                UPLOAD_WITH_ERRORS -> {
                    tvUploadWithErrors.text = errorMessage
                    tvUploadWithErrors.visibility = VISIBLE
                }

                NEED_TO_SELECT_COUNTRY -> {
                    tvCountryNotIdentified.visibility = VISIBLE
                }
            }
        }
    }
}