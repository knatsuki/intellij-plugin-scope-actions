package com.mukatalab.scopeActions.services

import java.util.*

/**
 * Data class containing state for scope actions configuration.
 *
 * NOTE: This data is persisted via PersistentStateComponent per project. It has the following structural requirements:
 *  - Provide a default constructor (that can be instantiated with zero parameters)
 *  - Any child class associated via public field must also provide a default constructor. For this example,
 *    ScopeActionConfigOverride must also provide a default constructor.
 *
 */
data class ScopeActionsProjectConfigState(var overrides: MutableList<ScopeActionConfigOverride> = mutableListOf()) {
    fun getEnabled(scopeName: String, scopeActionType: String): Boolean {
        return getScopeActionConfigOverride(scopeName, scopeActionType)?.enabled
            ?: ScopeActionsDefault.getDefaultEnabled(scopeName, scopeActionType)
    }

    fun getActionId(scopeName: String, scopeActionType: String): String {
        return getScopeActionConfigOverride(scopeName, scopeActionType)?.customActionId
            ?: ScopeActionsDefault.getDefaultActionId(scopeName, scopeActionType)
    }

    fun setEnabled(scopeName: String, scopeActionType: String, enabled: Boolean) {
        getOrCreateScopeActionConfigOverride(scopeName, scopeActionType).also {
            it.enabled = enabled
        }

    }

    fun setActionId(scopeName: String, scopeActionType: String, actionId: String) {
        getOrCreateScopeActionConfigOverride(scopeName, scopeActionType).also {
            it.customActionId = actionId
        }
    }

    private fun getScopeActionConfigOverride(
        scopeName: String, scopeActionType: String
    ): ScopeActionConfigOverride? =
        overrides.find { it.scopeName == scopeName && it.scopeActionType == scopeActionType }

    private fun getOrCreateScopeActionConfigOverride(
        scopeName: String,
        scopeActionType: String
    ): ScopeActionConfigOverride {
        return getScopeActionConfigOverride(scopeName, scopeActionType) ?: run {
            val newConfigOverride = ScopeActionConfigOverride(scopeName, scopeActionType)
            overrides.add(newConfigOverride)
            newConfigOverride
        }

    }

    fun copy(): ScopeActionsProjectConfigState {
        return ScopeActionsProjectConfigState(overrides.map { it.copy() }.toMutableList())
    }

    private object ScopeActionsDefault {
        fun getDefaultActionId(scopeName: String, scopeActionType: String): String {
            return "${scopeActionType}${pascalCaseName(scopeName)}"
        }

        fun getDefaultEnabled(scopeName: String, scopeActionType: String): Boolean {
            return false
        }
    }
}

private fun pascalCaseName(scopeName: String) = scopeName.split(' ')
    .map { subString -> subString.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }
    .joinToString("")
