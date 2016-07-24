package ru.sidvi.graylog.api;

import org.slf4j.Logger;

public class IssueDTO {

    public String title;
    public String description;
    public String projectIdentifier;
    public String type;
    public String priority;
    @Override
    public String toString() {
        return "IssueDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", projectIdentifier='" + projectIdentifier + '\'' +
                ", type='" + type + '\'' +
                ", priority='" + priority + '\'' +
                '}';
    }

    public String getProjectIdentifier() {
        return projectIdentifier;
    }

    public void setProjectIdentifier(String projectIdentifier) {
        this.projectIdentifier = projectIdentifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
