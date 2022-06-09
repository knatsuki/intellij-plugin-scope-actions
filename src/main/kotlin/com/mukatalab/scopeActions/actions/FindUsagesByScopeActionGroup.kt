package com.mukatalab.scopeActions.actions

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.project.Project
import com.mukatalab.jumpy.actions.FindUsagesByScopeAction
import com.mukatalab.scopeActions.getUserDefinedSearchScopes
import com.mukatalab.scopeActions.services.ScopeActionsProjectConfigService

class FindUsagesByScopeActionGroup : ActionGroup() {
    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        val project: Project = e?.project ?: throw IllegalStateException()
        val dataContext: DataContext = e.dataContext
        val userDefinedScopes = getUserDefinedSearchScopes(project, dataContext)

        val scopeActionsService = ScopeActionsProjectConfigService.getInstance(project)
        val actionManagerService: ActionManager = ActionManager.getInstance()

        val scopeActions: MutableList<AnAction> = mutableListOf()
        for (scope in userDefinedScopes) {
            if (scopeActionsService.state.getEnabled(scope.displayName, "FindUsagesByScope")) {
                val scopeAction = FindUsagesByScopeAction(
                    scope,
                    "Find Usages by Scope: ${scope.displayName}"
                )
                scopeActions.add(scopeAction)
                actionManagerService.registerAction(
                    scopeActionsService.state.getActionId(
                        scope.displayName,
                        "FindUsagesByScope"
                    ),
                    scopeAction
                )
            }
        }

        return scopeActions.toTypedArray()
    }
}