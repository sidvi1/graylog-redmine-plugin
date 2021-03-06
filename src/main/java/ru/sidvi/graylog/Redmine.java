package ru.sidvi.graylog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidvi.graylog.api.RedmineClient;
import ru.sidvi.graylog.api.RedmineClientFactory;
import ru.sidvi.graylog.marker.UniqIssueMarker;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Vitaly Sidorov mail@vitaly-sidorov.com
 */
public class Redmine {

    private final Logger logger = LoggerFactory.getLogger(Redmine.class);
    private UniqIssueMarker marker;
    private RedmineClientFactory factory;

    @Inject
    public Redmine(UniqIssueMarker marker, RedmineClientFactory factory) {
        this.marker = marker;
        this.factory = factory;
    }

    public void saveIfNonExists(IssueDTO issue, String serverUrl, String apiKey) {
        logger.info("Try to save issue {} to server {} with api key {}", issue, serverUrl, apiKey);
        RedmineClient client = factory.create(serverUrl, apiKey);

        String projectIdentifier = issue.getProjectIdentifier();
        List<IssueDTO> projectIssues = client.getAll(projectIdentifier);
        logger.debug("Loaded {] issues from project {}", projectIssues.size(), projectIdentifier);

        boolean exists = isExists(issue, projectIssues);
        logger.debug("Issue {} already exists in project {}", issue, projectIdentifier);
        if (!exists) {
            IssueDTO marked = marker.append(issue);
            logger.debug("Marked issue before send to Redmine server: {}", marked);
            boolean isCreated = client.create(marked);
            if (isCreated) {
                logger.info("Issue created successfully");
            } else {
                logger.info("Issue not created");
            }
        }
    }

    private boolean isExists(IssueDTO issue, List<IssueDTO> issues) {
        String currentMarker = marker.calculate(issue);
        for (IssueDTO i : issues) {
            String remoteMarker = marker.extract(i);
            if (currentMarker.equals(remoteMarker)) {
                return true;
            }
        }
        return false;
    }
}
