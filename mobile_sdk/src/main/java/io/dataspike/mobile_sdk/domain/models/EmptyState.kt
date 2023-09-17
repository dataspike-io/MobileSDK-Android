package io.dataspike.mobile_sdk.domain.models

internal sealed class EmptyState {
    internal data class EmptyStateSuccess(
        val empty: String,
    ): EmptyState()

    internal data class EmptyStateError(
        val error: String,
        val details: String,
    ): EmptyState()
}