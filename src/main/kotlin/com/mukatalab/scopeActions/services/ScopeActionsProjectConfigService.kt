package com.mukatalab.scopeActions.services

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

@State(
    name = "com.mukatalab.scopeActions.settings.ScopeActionsConfigurationService",
    storages = [Storage("ScopeActionsProjectConfiguration.xml")]

)
/**
 * Project service for storing/retrieving configuration for Scope Actions plugin
 */
class ScopeActionsProjectConfigService() :
    PersistentStateComponent<ScopeActionsProjectConfigState> {
    private var _state: ScopeActionsProjectConfigState = ScopeActionsProjectConfigState()
    private val actionTypes = listOf("ShowUsagesByScope", "FindUsagesByScope")

    val availableScopeActionTypes: List<String>
        get() = actionTypes

    fun setState(value: ScopeActionsProjectConfigState) {
        _state = value
    }

    override fun getState(): ScopeActionsProjectConfigState {
        return _state
    }


    override fun loadState(state: ScopeActionsProjectConfigState) {
        this._state = state
    }

    companion object {
        fun getInstance(project: Project): ScopeActionsProjectConfigService {
            return project.service<ScopeActionsProjectConfigService>()
        }
    }

}