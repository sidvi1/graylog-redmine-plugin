package ru.sidvi.graylog.hash;

import ru.sidvi.graylog.api.IssueDTO;

import javax.inject.Inject;

/**
 * Add hash marker to one of issue fields.
 */
public class IssueMarker {
    private Hash hash;

    @Inject
    public IssueMarker(Hash hash) {
        this.hash = hash;
    }

    public IssueDTO append(IssueDTO issue) {
        String h = calculate(issue);
        issue.setDescription(hash.append(h, issue.getDescription()));
        return issue;
    }

    public IssueDTO remove(IssueDTO issue) {
        issue.setDescription(hash.remove(issue.getDescription()));
        return issue;
    }

    public String extract(IssueDTO issue) {
        return hash.extract(issue.getDescription());
    }

    public String calculate(IssueDTO issue) {
        return hash.calc(issue.toString());
    }
}
