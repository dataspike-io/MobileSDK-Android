package io.dataspike.mobile_sdk.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.databinding.FragmentWebviewBinding
import io.dataspike.mobile_sdk.dependencies_provider.DataspikeInjector
import io.dataspike.mobile_sdk.utils.darkModeIsEnabled
import io.dataspike.mobile_sdk.view.LIVENESS
import io.dataspike.mobile_sdk.view.POA
import io.dataspike.mobile_sdk.view.POI_BACK
import io.dataspike.mobile_sdk.view.POI_FRONT
import io.dataspike.mobile_sdk.view.REQUIREMENTS_TYPE
import io.dataspike.mobile_sdk.view.UIManager
import io.dataspike.mobile_sdk.view.ui_models.ButtonConfigModel

internal const val REQUIREMENTS_SCREEN = "requirements_screen"
internal const val LIGHT_THEME = "&theme=light"
internal const val DARK_THEME = "&theme=dark"
internal const val HIDE_HEADER = "?hideHeader=true"

internal class WebViewFragment: BaseFragment() {

    private var viewBinding: FragmentWebviewBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentWebviewBinding.inflate(inflater, container, false)

        return viewBinding?.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val requirementsType = arguments?.getString(REQUIREMENTS_TYPE)
        val links = UIManager.getUiConfig().links.requirements
        val shortId = DataspikeInjector.component.shortId
        val theme = if ((requireActivity() as? AppCompatActivity)?.darkModeIsEnabled() == true) {
            DARK_THEME
        } else {
            LIGHT_THEME
        }
        val link = when (requirementsType) {
            POI_FRONT, POI_BACK -> links.poi

            LIVENESS -> links.liveness

            POA -> links.poa

            else -> links.poi
        } + shortId + HIDE_HEADER + theme

        with(viewBinding ?: return) {
            hlHeader.setup(
                popBackStackAction = ::popBackStack,
                stringResId = getStringResFromImageType(requirementsType, REQUIREMENTS_SCREEN),
                colorInt = typography.header.textColor,
            )
            bButtons.setup(
                topButtonConfig = ButtonConfigModel(
                    isVisible = true,
                    isEnabled = true,
                    isTransparent = false,
                    backgroundColors = Pair(palette.mainColor, palette.lightMainColor),
                    text = getString(R.string._continue),
                    textColors = Pair(palette.backgroundColor, palette.backgroundColor),
                    action = ::popBackStack,
                ),
            )
            with(wvRequirements) {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                webViewClient = WebViewClient()

                loadUrl(link)
            }
        }
    }
}