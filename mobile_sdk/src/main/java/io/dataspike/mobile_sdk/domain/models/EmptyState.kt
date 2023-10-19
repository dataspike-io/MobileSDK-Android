package io.dataspike.mobile_sdk.domain.models

internal sealed class EmptyState {
    internal object EmptyStateSuccess: EmptyState()

    internal data class EmptyStateError(
        val error: String,
        val details: String,
    ): EmptyState()
}