package com.mukatalab.scopeActions.extensionPoints

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.mukatalab.scopeActions.actions.FindUsagesByScopeActionGroup
import com.mukatalab.scopeActions.actions.ShowUsagesByScopeActionGroup

class ScopeActionsInitialization : StartupActivity, DumbAware {
    override fun runActivity(project: Project) {
        val findUsagesGroupAction = ActionManager.getInstance().getAction("scopeActions.FindUsagesByScopeActionGroup")
        if (findUsagesGroupAction is FindUsagesByScopeActionGroup) {
            findUsagesGroupAction.loadChildren(project)
        }

        val showUsagesGroupAction = ActionManager.getInstance().getAction("scopeActions.ShowUsagesByScopeActionGroup")
        if (showUsagesGroupAction is ShowUsagesByScopeActionGroup) {
            showUsagesGroupAction.loadChildren(project)
        }
    }

}