/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.api.tasks.diagnostics.internal;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.util.Path;

import java.util.Set;

public class TaskDetailsFactory {
    private final Set<Project> projects;
    private final Project project;

    public TaskDetailsFactory(Project project) {
        this.project = project;
        this.projects = project.getAllprojects();
    }

    public TaskDetails create(final Task task) {
        final String path;
        Project project = task.getProject();
        if (projects.contains(project)) {
            path = this.project.relativeProjectPath(task.getPath());
        } else {
            path = task.getPath();
        }
        return new MyTaskDetails(path, task);
    }

    private static class MyTaskDetails implements TaskDetails {
        private final String path;
        private final Task task;
        private Path cachedPath;

        public MyTaskDetails(String path, Task task) {
            this.path = path;
            this.task = task;
        }

        @Override
        public Path getPath() {
            if (cachedPath == null) {
                cachedPath = Path.path(path);
            }
            return cachedPath;
        }

        @Override
        public String getDescription() {
            return task.getDescription();
        }
    }
}
