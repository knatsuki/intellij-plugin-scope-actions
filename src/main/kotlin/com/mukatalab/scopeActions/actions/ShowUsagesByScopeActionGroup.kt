package com.mukatalab.scopeActions.actions

import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.project.Project
import com.mukatalab.jumpy.actions.ShowUsagesByScopeAction
import com.mukatalab.scopeActions.getScopeActionTypeName
import com.mukatalab.scopeActions.getUserDefinedSearchScopes
import com.mukatalab.scopeActions.services.ScopeActionsProjectConfigService

class ShowUsagesByScopeActionGroup : ActionGroup() {
    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        val project: Project = e?.project ?: throw IllegalStateException()
        val dataContext: DataContext = e.dataContext
        val userDefinedScopes = getUserDefinedSearchScopes(project, dataContext)

        val scopeActionsService = ScopeActionsProjectConfigService.getInstance(project)
        val actionManagerService: ActionManager = ActionManager.getInstance()

        val scopeActions: MutableList<AnAction> = mutableListOf()
        for (scope in userDefinedScopes) {
            if (scopeActionsService.state.getEnabled(
                    scope.displayName,
                    getScopeActionTypeName(ShowUsagesByScopeAction::class)
                )
            ) {
                val scopeAction = ShowUsagesByScopeAction(
                    scope,
                    "Show Usages by Scope: ${scope.displayName}"
                )
                scopeActions.add(scopeAction)
                actionManagerService.registerAction(
                    scopeActionsService.state.getActionId(
                        scope.displayName,
                        getScopeActionTypeName(ShowUsagesByScopeAction::class)
                    ),
                    scopeAction
                )
            }
        }

        return scopeActions.toTypedArray()
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isVisible = e.getData(CommonDataKeys.PSI_ELEMENT) != null
    }
}