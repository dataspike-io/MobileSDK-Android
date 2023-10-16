package io.dataspike.mobile_sdk.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import io.dataspike.mobile_sdk.AppInfo
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.data.image_caching.ImageCacheManager
import io.dataspike.mobile_sdk.databinding.ActivityDataspikeBinding
import io.dataspike.mobile_sdk.domain.models.VerificationState
import io.dataspike.mobile_sdk.utils.Utils.launchInMain
import io.dataspike.mobile_sdk.view.fragments.OnboardingFragment
import io.dataspike.mobile_sdk.view.view_models.DataspikeActivityViewModel
import io.dataspike.mobile_sdk.view.view_models.DataspikeViewModelFactory
import kotlinx.coroutines.runBlocking

internal const val REQUIREMENTS_TYPE = "requirements_type"
internal const val IMAGE_TYPE = "image_type"
internal const val POI = "poi"
internal const val POI_FRONT = "poi_front"
internal const val POI_BACK = "poi_back"
internal const val LIVENESS = "liveness_photo"
internal const val POA = "poa"

internal class DataspikeActivity : AppCompatActivity() {

    private var viewBinding: ActivityDataspikeBinding? = null
    private val viewModel: DataspikeActivityViewModel by viewModels { DataspikeViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityDataspikeBinding.inflate(layoutInflater)
        setContentView(viewBinding?.root)

        collectVerificationFlow()

        runBlocking {
            AppInfo.setAppInfo(
                getString(applicationInfo.labelRes),
                packageManager.getPackageInfo(packageName, 0).versionName,
            )
        }

        viewModel.getVerification()
    }

    override fun onResume() {
        super.onResume()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    override fun onDestroy() {
        ImageCacheManager.clearImageCache()
        super.onDestroy()
    }

    private fun collectVerificationFlow() {
        launchInMain {
            viewModel.verificationFlow.collect { result ->
                when (result) {
                    is VerificationState.VerificationSuccess -> {
                        supportFragmentManager
                            .beginTransaction()
                            .add(
                                R.id.fcv_container,
                                OnboardingFragment()
                            ).commit()
                    }
                    is VerificationState.VerificationError -> {
                        Toast.makeText(
                            this@DataspikeActivity,
                            "${result.error}: ${result.details}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}