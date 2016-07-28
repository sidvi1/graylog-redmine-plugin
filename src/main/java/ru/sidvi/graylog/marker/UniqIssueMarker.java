package ru.sidvi.graylog.marker;

import ru.sidvi.graylog.api.IssueDTO;

/**
 * Add hash marker to one of issue fields.
 *
 * @author Vitaly Sidorov {@literal mail@vitaly-sidorov.com}
 */
public interface UniqIssueMarker {
    IssueDTO append(IssueDTO issue);

    IssueDTO remove(IssueDTO issue);

    String extract(IssueDTO issue);

    String calculate(IssueDTO issue);
}
