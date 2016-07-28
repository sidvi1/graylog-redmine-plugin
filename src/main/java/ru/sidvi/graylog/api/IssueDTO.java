package ru.sidvi.graylog.api;

import com.google.common.base.Objects;

/**
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
public class IssueDTO {

    private String title;
    private String description;
    private String projectIdentifier;
    private String type = "Bug";
    private String priority = "Minor";

    public IssueDTO(IssueDTO issue) {
        title = issue.getTitle();
        description = issue.getDescription();
        projectIdentifier = issue.getProjectIdentifier();
        type = issue.getType();
        priority = issue.getPriority();
    }

    public IssueDTO() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IssueDTO issueDTO = (IssueDTO) o;
        return Objects.equal(title, issueDTO.title) &&
                Objects.equal(description, issueDTO.description) &&
                Objects.equal(projectIdentifier, issueDTO.projectIdentifier) &&
                Objects.equal(type, issueDTO.type) &&
                Objects.equal(priority, issueDTO.priority);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title, description, projectIdentifier, type, priority);
    }
}
