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
import com.mukatalab.scopeActions.actions.ScopeAnAction
import com.mukatalab.scopeActions.notifyError
import com.mukatalab.scopeActions.notifyPotentiallyMissingSdk
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
    scope: SearchScope,
    text: @NlsActions.ActionText String?,
    description: @NlsActions.ActionDescription String? = null,
    icon: Icon? = null
) : ScopeAnAction(scope, text, description, icon) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val dataContext = e.dataContext
        val psiEl = e.getData(CommonDataKeys.PSI_ELEMENT) ?: run {
            notifyPotentiallyMissingSdk(project)
            return
        }

        updateFindUsagesOptionsWithScope(project, psiEl, dataContext)
        ActionManager.getInstance().getAction(ShowUsagesAction.ID).actionPerformed(e)
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

    private fun updateFindUsagesOptionsWithScope(
        project: Project,
        psiEl: PsiElement,
        dataContext: DataContext
    ) {
        getFindUsagesOptions(project, psiEl, dataContext)?.let {
            it.searchScope = scope
        } ?: notifyError(project, "Failed to retrieve Find Usages Options.")
    }

}
