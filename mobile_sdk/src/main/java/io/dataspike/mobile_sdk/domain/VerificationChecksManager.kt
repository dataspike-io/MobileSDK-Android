package io.dataspike.mobile_sdk.domain

import io.dataspike.mobile_sdk.domain.models.ChecksDomainModel
import io.dataspike.mobile_sdk.domain.models.VerificationSettingsDomainModel

internal object VerificationChecksManager {
    fun setChecks(settings: VerificationSettingsDomainModel) {
        checks = ChecksDomainModel(
            poiIsRequired = settings.poiRequired,
            livenessIsRequired = settings.faceComparisonRequired,
            poaIsRequired = settings.poaRequired,
        )
    }

    var checks: ChecksDomainModel = ChecksDomainModel(
        poiIsRequired = false,
        livenessIsRequired = false,
        poaIsRequired = false,
    )
        private set

}
