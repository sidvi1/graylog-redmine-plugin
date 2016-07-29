package ru.sidvi.graylog.api;

import ru.sidvi.graylog.IssueDTO;

import java.util.List;

/**
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
public interface RedmineClient {

    boolean create(IssueDTO issue);

    List<IssueDTO> getAll(String projectIdentifier);
}
