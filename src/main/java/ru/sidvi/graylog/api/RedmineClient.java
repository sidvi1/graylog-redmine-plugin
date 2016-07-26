package ru.sidvi.graylog.api;

import java.util.List;

public interface RedmineClient {

    void create(IssueDTO issue);

    List<IssueDTO> getAll(String projectIdentifier);
}
