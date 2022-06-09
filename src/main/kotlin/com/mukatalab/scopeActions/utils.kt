package com.mukatalab.scopeActions

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.project.Project
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.SearchScopeProvider

fun getUserDefinedSearchScopes(project: Project, dataContext: DataContext): List<SearchScope> {
    return SearchScopeProvider.EP_NAME.extensions.find { it.displayName == "Other" }
        ?.getSearchScopes(project, dataContext)
        ?: listOf()
}