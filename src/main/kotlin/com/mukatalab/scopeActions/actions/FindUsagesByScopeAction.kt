package com.mukatalab.jumpy.actions

import com.intellij.find.FindManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.util.NlsActions
import com.intellij.psi.search.SearchScopeProvider
import javax.swing.Icon

/**
 * Find Usages by scope for given scope name.
 */
class FindUsagesByScopeAction(
    private val scopeName: String,
    text: @NlsActions.ActionText String?,
    description: @NlsActions.ActionDescription String?,
    icon: Icon?
) : AnAction(text, description, icon) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val dataContext = e.dataContext
        val psiEl = e.getData(CommonDataKeys.PSI_ELEMENT) ?: return

        val userDefinedSearchScopeProvider =
            SearchScopeProvider.EP_NAME.extensions.find { it.displayName == "Other" } ?: return
        val scopes = userDefinedSearchScopeProvider.getSearchScopes(project, dataContext)

        val scope = scopes.find { searchScope ->
            searchScope.displayName == scopeName
        } ?: throw IllegalArgumentException("scopeName '${scopeName}' cannot be resolved.")

        FindManager.getInstance(project).findUsagesInScope(psiEl, scope)
    }
}

