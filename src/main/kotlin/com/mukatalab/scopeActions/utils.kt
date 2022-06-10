package com.mukatalab.scopeActions

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.project.Project
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.SearchScopeProvider
import com.mukatalab.scopeActions.actions.ScopeAnAction
import kotlin.reflect.KClass

fun getUserDefinedSearchScopes(project: Project, dataContext: DataContext): List<SearchScope> {
    return SearchScopeProvider.EP_NAME.extensions.find { it.displayName == "Other" }
        ?.getSearchScopes(project, dataContext)
        ?: listOf()
}

fun getScopeActionTypeName(klass: KClass<out ScopeAnAction>): String {
    return klass.simpleName ?: throw IllegalArgumentException("Anonymous class not supported")
}