package io.dataspike.mobile_sdk

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import io.dataspike.mobile_sdk.dependencies_provider.DataspikeInjector
import io.dataspike.mobile_sdk.dependencies_provider.model.DataspikeDependencies
import io.dataspike.mobile_sdk.view.DataspikeActivity

object DataspikeManager {

//    private var verificationCompletedCallback: VerificationCompletedCallback? = null

    fun startDataspikeFlow(
        dataspikeDependencies: DataspikeDependencies,
        context: Context,
    ) {
        DataspikeInjector.setComponent(dataspikeDependencies)

        startActivity(
            context,
            Intent(
                context,
                DataspikeActivity::class.java
            ),
            null
        )
    }

//    fun setVerificationCompletedCallback(
//        verificationCompletedCallback: VerificationCompletedCallback
//    ) {
//        this.verificationCompletedCallback = verificationCompletedCallback
//    }
//
//    fun passVerificationCompletedResult(verificationSucceeded: Boolean) {
//        verificationCompletedCallback?.onVerificationCompleted(verificationSucceeded)
//    }
//
//    interface VerificationCompletedCallback {
//
//        fun onVerificationCompleted(verificationSucceeded: Boolean)
//    }
}
