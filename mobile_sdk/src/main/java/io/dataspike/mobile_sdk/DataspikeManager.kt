package io.dataspike.mobile_sdk

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import io.dataspike.mobile_sdk.dependencies_provider.DataspikeInjector
import io.dataspike.mobile_sdk.dependencies_provider.model.DataspikeDependencies
import io.dataspike.mobile_sdk.view.DataspikeActivity

object DataspikeManager {

    fun startDataspikeFlow(
        dataspikeDependencies: DataspikeDependencies,
        callback: VerificationCompletedCallback,
        context: Context,
    ) {
        DataspikeInjector.setComponent(dataspikeDependencies)
        setVerificationCompletedCallback(callback)

        startActivity(
            context,
            Intent(
                context,
                DataspikeActivity::class.java
            ),
            null
        )
    }
}
