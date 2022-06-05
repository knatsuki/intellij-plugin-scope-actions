package com.mukatalab.scopeActions.settings

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.SearchScopeProvider
import com.mukatalab.scopeActions.services.ScopeActionsProjectConfigService
import javax.swing.JComponent

class ScopeActionsProjectSettingsConfigurable(private val project: Project) : Configurable {
    private var view: ScopeActionsProjectSettingsView? = null
    private val scopeActionsProjectConfigService: ScopeActionsProjectConfigService =
        ScopeActionsProjectConfigService.getInstance(project)

    override fun getDisplayName(): String {
        return "Scope Actions Configuration"
    }

    override fun createComponent(): JComponent? {
        view = ScopeActionsProjectSettingsView(
            userDefinedScopes,
            scopeActionsProjectConfigService.availableScopeActionTypes
        )
        return view?.mainPanelComponent
    }

    override fun reset() {
        view?.scopeActionsProjectConfigState = scopeActionsProjectConfigService.state.copy()
    }

    override fun isModified(): Boolean {
        return view?.scopeActionsProjectConfigState != scopeActionsProjectConfigService.state
    }

    override fun apply() {
        scopeActionsProjectConfigService.state =
            view?.scopeActionsProjectConfigState?.copy()
                ?: throw RuntimeException("apply() called in incorrect context.")
    }

    override fun disposeUIResources() {
        view = null
    }

    private val userDefinedScopes: List<SearchScope>
        get() {
            val userDefinedSearchScopeProvider =
                SearchScopeProvider.EP_NAME.extensions.find { it.displayName == "Other" }
                    ?: throw java.lang.RuntimeException("Failed to retrieve custom search scope")
            return userDefinedSearchScopeProvider.getSearchScopes(project, DataContext.EMPTY_CONTEXT)
        }
}