package com.mukatalab.scopeActions.services

/**
 * Configuration override for the given scope action type
 *
 * NOTE: Because this data class is used in PersistentStateComponent, it must provide a default constructor.
 */
data class ScopeActionConfigOverride(
    var scopeName: String? = null,
    var scopeActionType: String? = null,
    var enabled: Boolean? = null,
    var customActionId: String? = null
)
