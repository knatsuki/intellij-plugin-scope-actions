package com.mukatalab.scopeActions.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.util.NlsActions
import com.intellij.psi.search.SearchScope
import javax.swing.Icon

abstract class ScopeAnAction(
    protected val scope: SearchScope,
    text: @NlsActions.ActionText String?,
    description: @NlsActions.ActionDescription String? = null,
    icon: Icon? = null,
) : AnAction(text, description, icon) {
}

