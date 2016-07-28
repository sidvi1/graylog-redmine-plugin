package ru.sidvi.graylog.api;

import java.util.List;

/**
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
public interface RedmineClient {

    void create(IssueDTO issue);

    List<IssueDTO> getAll(String projectIdentifier);
}
