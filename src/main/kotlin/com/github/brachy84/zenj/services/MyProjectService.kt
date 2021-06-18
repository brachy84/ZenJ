package com.github.brachy84.zenj.services

import com.github.brachy84.zenj.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
