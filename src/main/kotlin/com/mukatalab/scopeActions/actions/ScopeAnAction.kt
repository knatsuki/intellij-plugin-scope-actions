package com.mukatalab.scopeActions.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.util.NlsActions
import com.intellij.psi.search.SearchScope
import javax.swing.Icon

abstract class ScopeAnAction(
    protected val scope: SearchScope,
    text: @NlsActions.ActionText String?,
    description: @NlsActions.ActionDescription String? = null,
    icon: Icon? = null,
) : AnAction(text, description, icon) {


    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.getData(CommonDataKeys.PSI_ELEMENT) != null
    }
}

