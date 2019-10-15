package com.hmzhou.language.plugin;

import com.hmzhou.language.common.utils.Log;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

public class HJLTask extends DefaultTask {

    @Input
    HJLExtension extension() {
        return getProject().getExtensions().getByType(HJLExtension.class);
    }

    @TaskAction
    void transiformLanguage() throws Exception {
        HJLExtension hjlExtension = extension();
        Project project = getProject();
        if (project.getParent() != null) {
            project = project.getProject();
        }

        Main.execute(project.getProjectDir().getAbsolutePath() + File.separator + hjlExtension.languagemap);
    }
}
