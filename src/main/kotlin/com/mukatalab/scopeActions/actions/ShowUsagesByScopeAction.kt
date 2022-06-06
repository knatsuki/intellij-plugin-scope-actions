package com.mukatalab.jumpy.actions

import com.intellij.find.FindManager
import com.intellij.find.actions.ShowUsagesAction
import com.intellij.find.findUsages.FindUsagesHandlerFactory.OperationMode
import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.find.impl.FindManagerImpl
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsActions
import com.intellij.psi.PsiElement
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.SearchScopeProvider
import javax.swing.Icon

/**
 * The objective is to create a dynamic action that can show usages by specified scope.
 * From poking through intellij's api, it doesn't seem to support it besides manually configuring it on gui.
 *
 * Much of [com.intellij.find.actions.ShowUsagesAction] is encapsulated by "internal" protected methods.
 * In order to avoid reimplementing them, the approach taken here is to change the default search scope before delegating
 * to [com.intellij.find.actions.ShowUsagesAction].
 *
 * UPDATE(22/05/28):
 * Overriding setting's defaultScopeName works. However [com.intellij.find.findUsages.FindUsagesOptions.findScopeByName]
 * only searches in [com.intellij.psi.search.PredefinedSearchScopeProvider] so the approach doesn't work. The
 * user-defined scopes are defined in extensions.
 *
 * [com.intellij.find.actions.findUsages]
 *
 * UPDATE(22/05/29):
 *
 * PsiElement variant calls [com.intellij.find.actions.ShowUsagesAction.startFindUsages]. This in turn retrieves the
 * options used by FindUsages. One of the options is searchScope [com.intellij.find.findUsages.FindUsagesOptions].
 * This FindUsagesOptions instance is used to create ShowUsagesActionHandler isntance via
 * [com.intellij.find.actions.ShowUsagesAction.createActionHandler]. The [com.intellij.find.actions.ShowUsagesActionHandler.getSelectedScope]
 * returns the FindUsagesOptions.searchScope.
 *
 * Thus, if we can update the searchScope option of FindUsagesOptions prior to invoking the ShowUsages action, we
 * achieve the result we want. Indeed, that is the strategy we take here.
 *
 * @see com.intellij.find.actions.ShowUsagesAction
 */
class ShowUsagesByScopeAction(
    private val scopeName: String,
    text: @NlsActions.ActionText String?,
    description: @NlsActions.ActionDescription String?,
    icon: Icon?
) : AnAction(text, description, icon) {
    constructor() : this("dummy", null, null, null)

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val dataContext = e.dataContext
        val psiEl = e.getData(CommonDataKeys.PSI_ELEMENT) ?: return

        val searchScope = getSearchScope(project, dataContext) ?: return
        val options = getFindUsagesOptions(project, psiEl, dataContext) ?: return
        options.searchScope = searchScope

        ActionManager.getInstance().getAction(ShowUsagesAction.ID).actionPerformed(e)
    }

    private fun getSearchScope(
        project: Project,
        dataContext: DataContext
    ): SearchScope? {
        val userDefinedSearchScopeProvider =
            SearchScopeProvider.EP_NAME.extensions.find { it.displayName == "Other" } ?: return null
        val scopes = userDefinedSearchScopeProvider.getSearchScopes(project, dataContext)
        return scopes.find { searchScope -> searchScope.displayName == scopeName }
    }

    private fun getFindUsagesOptions(
        project: Project,
        psiEl: PsiElement,
        dataContext: DataContext
    ): FindUsagesOptions? {
        val findUsagesManager = (FindManager.getInstance(project) as FindManagerImpl).findUsagesManager
        val handler = findUsagesManager.getFindUsagesHandler(psiEl, OperationMode.USAGES_WITH_DEFAULT_OPTIONS)
            ?: return null

        return handler.getFindUsagesOptions(dataContext)
    }


}
