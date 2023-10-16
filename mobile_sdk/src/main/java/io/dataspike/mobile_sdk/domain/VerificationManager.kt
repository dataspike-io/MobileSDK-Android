package io.dataspike.mobile_sdk.domain

import android.util.Log
import io.dataspike.mobile_sdk.domain.models.ChecksDomainModel
import io.dataspike.mobile_sdk.domain.models.VerificationSettingsDomainModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

internal object VerificationManager {
    fun setChecksAndExpiration(
        settings: VerificationSettingsDomainModel,
        status: String,
        expiresAt: String,
    ) {
        checks = ChecksDomainModel(
            poiIsRequired = settings.poiRequired,
            livenessIsRequired = settings.faceComparisonRequired,
            poaIsRequired = settings.poaRequired,
        )

        this.status = status
        this.expiresAt = expiresAt
    }

    var checks: ChecksDomainModel = ChecksDomainModel(
        poiIsRequired = false,
        livenessIsRequired = false,
        poaIsRequired = false,
    )
        private set

    private var expiresAt: String = ""
    var status: String = ""

    val millisecondsUntilVerificationExpired
        get() = try {
            val dateFormat = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.getDefault()
            )

            //TODO timezone
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val parsedDate = dateFormat.parse(expiresAt)

            (parsedDate?.time ?: 0) - Date().time
        } catch (e: Exception) {
            Log.d("Dataspike", e.message.toString())
            0
        }
}
