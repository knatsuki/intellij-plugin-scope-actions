package com.mukatalab.jumpy.actions

import com.intellij.find.FindManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.util.NlsActions
import com.intellij.psi.search.SearchScope
import com.mukatalab.scopeActions.actions.ScopeAnAction
import com.mukatalab.scopeActions.notifyPotentiallyMissingSdk
import javax.swing.Icon

/**
 * Find Usages by scope for given scope name.
 */
class FindUsagesByScopeAction(
    scope: SearchScope,
    text: @NlsActions.ActionText String?,
    description: @NlsActions.ActionDescription String? = null,
    icon: Icon? = null
) : ScopeAnAction(scope, text, description, icon) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val psiEl = e.getData(CommonDataKeys.PSI_ELEMENT) ?: run {
            notifyPotentiallyMissingSdk(project)
            return
        }

        FindManager.getInstance(project).findUsagesInScope(psiEl, scope)
    }
}

