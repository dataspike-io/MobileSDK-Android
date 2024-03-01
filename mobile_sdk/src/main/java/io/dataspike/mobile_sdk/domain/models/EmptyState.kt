package io.dataspike.mobile_sdk.domain.models

internal sealed class EmptyState {

    object EmptyStateSuccess: EmptyState()

    data class EmptyStateError(
        val error: String,
        val details: String,
    ): EmptyState()
}