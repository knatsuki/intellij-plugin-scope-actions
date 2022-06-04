package com.github.knatsuki.intellijpluginscopeactions.services

import com.intellij.openapi.project.Project
import com.github.knatsuki.intellijpluginscopeactions.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
