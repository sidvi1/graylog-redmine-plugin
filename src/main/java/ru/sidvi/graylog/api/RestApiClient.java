package ru.sidvi.graylog.api;

import com.google.common.collect.Lists;
import com.taskadapter.redmineapi.*;
import com.taskadapter.redmineapi.bean.*;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidvi.graylog.IssueDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
class RestApiClient implements RedmineClient {

    private final Logger logger = LoggerFactory.getLogger(RestApiClient.class);

    private IssueManager issueManager;
    private ProjectManager projectManager;

    public RestApiClient(String serverUrl, String apiKey) {
        logger.debug("Creating RedmineManager for server url {} and api key {}", serverUrl, apiKey);
        HttpClient client = new DefaultHttpClient();
        RedmineManager manager = RedmineManagerFactory.createWithApiKey(serverUrl, apiKey, client);
        logger.debug("RedmineManager created");
        issueManager = manager.getIssueManager();
        projectManager = manager.getProjectManager();
    }

    @Override
    public boolean create(IssueDTO holder) {
        try {
            logger.debug("Try to create issue {}", holder);
            Integer priority = getPriority(holder);
            Project project = getProjectByKey(holder.getProjectIdentifier());
            Tracker tracker = getTracker(holder);

            Issue issue = mapFromHolder(holder, project, priority, tracker);
            issueManager.createIssue(issue);
        } catch (RedmineException e) {
            logger.error("Exception while create issue {}", holder, e);
            return false;
        }
        return true;
    }

    @Override
    public List<IssueDTO> getAll(String projectIdentifier) {
        List<IssueDTO> issueDTOs = new ArrayList<>();
        try {
            logger.debug("Try to load all issue from project {}", projectIdentifier);
            issueDTOs = convert(issueManager.getIssues(projectIdentifier, null), projectIdentifier);
        } catch (RedmineException e) {
            logger.error("Exception while load all issues from project {}", projectIdentifier, e);
        }
        logger.debug("Load issues from project {}", projectIdentifier);
        logger.debug("{}", issueDTOs);
        return issueDTOs;
    }

    private Project getProjectByKey(String projectIdentifier) throws RedmineException {
        Project project = projectManager.getProjectByKey(projectIdentifier);
        logger.debug("Found project {} by key {}", Utils.toString(project), projectIdentifier);
        return project;
    }

    private Integer getPriority(IssueDTO holder) throws RedmineException {
        List<IssuePriority> issuePriorities = Lists.reverse(issueManager.getIssuePriorities());
        logger.debug("Found issue priorities {}", issuePriorities);

        Integer result = issuePriorities.get(0).getId();
        for (IssuePriority p : issuePriorities) {
            if (p.getName().equals(holder.getPriority())) {
                result = p.getId();
            }
        }

        logger.debug("Selected priority is {}", result);
        return result;
    }

    private Tracker getTracker(IssueDTO holder) throws RedmineException {
        List<Tracker> trackers = Lists.reverse(issueManager.getTrackers());
        logger.debug("Found trackers {}", Utils.toString(trackers));
        Tracker result = trackers.get(trackers.size() - 1); //we should have at least one tracker

        for (Tracker tracker : trackers) {
            if (tracker.getName().equals(holder.getType())) {
                result = tracker;
            }
        }

        logger.debug("Selected tracker is {}", Utils.toString(result));
        return result;
    }

    private List<IssueDTO> convert(List<Issue> issues, String projectIdentifier) {
        List<IssueDTO> result = new ArrayList<>();
        for (Issue fromServer : issues) {
            result.add(mapToHolder(fromServer, projectIdentifier));
        }
        return result;
    }

    private IssueDTO mapToHolder(Issue fromServer, String projectIdentifier) {
        IssueDTO issue = new IssueDTO();
        issue.setTitle(fromServer.getSubject());
        issue.setDescription(fromServer.getDescription());
        issue.setProjectIdentifier(projectIdentifier);
        issue.setPriority(fromServer.getPriorityText());
        issue.setType(fromServer.getTracker().getName());
        return issue;
    }

    private Issue mapFromHolder(IssueDTO holder, Project project, Integer priority, Tracker tracker) throws RedmineException {
        Issue i = IssueFactory.create(project.getId(), holder.getTitle());
        i.setTracker(tracker);
        i.setPriorityId(priority);
        i.setDescription(holder.getDescription());
        i.setProject(project);
        return i;
    }

    private static class Utils {

        private static String toString(List objects) {
            StringBuilder builder = new StringBuilder();
            for (Object o : objects) {
                if (o instanceof Tracker) {
                    builder.append(toString((Tracker) o));
                }
                if (o instanceof IssuePriority) {
                    builder.append(toString((IssuePriority) o));
                }
            }
            return builder.toString();
        }

        private static String toString(IssuePriority o) {
            return String.valueOf(o.getId()) + " " + o.getName() + ";";
        }

        private static String toString(Tracker o) {
            return String.valueOf(o.getId()) + " " + o.getName() + ";";
        }

        private static String toString(Project o) {
            return String.valueOf(o.getId()) + " " + o.getName() + ";";
        }
    }
}
