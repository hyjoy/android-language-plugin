package com.hmzhou.language.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class HJLanguagePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.getExtensions().create("hjlanguage", HJLExtension.class);
        project.getTasks().create("a_hjlanguage", HJLTask.class);
    }
}