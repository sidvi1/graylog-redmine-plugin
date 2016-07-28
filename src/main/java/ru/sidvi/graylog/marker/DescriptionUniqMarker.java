package ru.sidvi.graylog.marker;

import ru.sidvi.graylog.api.IssueDTO;
import ru.sidvi.graylog.marker.hash.Hash;

import javax.inject.Inject;

/**
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
public class DescriptionUniqMarker implements UniqIssueMarker {
    private Hash hash;

    @Inject
    public DescriptionUniqMarker(Hash hash) {
        this.hash = hash;
    }

    @Override
    public IssueDTO append(IssueDTO issue) {
        String h = calculate(issue);
        issue.setDescription(hash.append(h, issue.getDescription()));
        return issue;
    }

    @Override
    public IssueDTO remove(IssueDTO issue) {
        issue.setDescription(hash.remove(issue.getDescription()));
        return issue;
    }

    @Override
    public String extract(IssueDTO issue) {
        return hash.extract(issue.getDescription());
    }

    @Override
    public String calculate(IssueDTO issue) {
        return hash.calc(issue.toString());
    }
}
