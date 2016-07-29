package ru.sidvi.graylog.api;

import com.google.common.collect.Lists;
import com.taskadapter.redmineapi.*;
import com.taskadapter.redmineapi.bean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
class RestApiClient implements RedmineClient {

    private final Logger logger = LoggerFactory.getLogger(IssueDTO.class);

    private IssueManager issueManager;
    private ProjectManager projectManager;

    public RestApiClient(String serverUrl, String apiKey) {
        RedmineManager manager = RedmineManagerFactory.createWithApiKey(serverUrl, apiKey);
        issueManager = manager.getIssueManager();
        projectManager = manager.getProjectManager();
    }

    @Override
    public void create(IssueDTO holder) {
        try {
            Integer priority = getPriority(issueManager.getIssuePriorities(), holder);
            Project project = projectManager.getProjectByKey(holder.getProjectIdentifier());
            Tracker tracker = getTracker(holder);

            Issue issue = fillIssue(holder, project, priority, tracker);
            issueManager.createIssue(issue);
        } catch (RedmineException e) {
            logger.error("Exception while create issue.", e);
        }
    }

    private Tracker getTracker(IssueDTO holder) throws RedmineException {
        List<Tracker> trackers = Lists.reverse(issueManager.getTrackers());
        for (Tracker tracker : trackers) {
            if(tracker.getName().equals(holder.getType())){
                return tracker;
            }
        }
        return trackers.get(trackers.size()-1); //we should have at least one tracker
    }

    @Override
    public List<IssueDTO> getAll(String projectIdentifier) {
        List<IssueDTO> issueDTOs = new ArrayList<>();
        try {
            List<Issue> issues = issueManager.getIssues(projectIdentifier, null);
            for (Issue fromServer : issues) {
                IssueDTO issue = new IssueDTO();
                issue.setTitle(fromServer.getSubject());
                issue.setDescription(fromServer.getDescription());
                issue.setProjectIdentifier(projectIdentifier);
                issue.setPriority(fromServer.getPriorityText());
                issue.setType(fromServer.getTracker().getName());
                issueDTOs.add(issue);
            }
        } catch (RedmineException e) {
            logger.error("Exception while getting all issues from project "+ projectIdentifier, e);
        }
        return issueDTOs;
    }


    private Issue fillIssue(IssueDTO holder, Project project, Integer priority, Tracker tracker) throws RedmineException {
        Issue i = IssueFactory.create(project.getId(), holder.getTitle());
        i.setTracker(tracker);
        i.setPriorityId(priority);
        i.setDescription(holder.getDescription());
        i.setProject(project);
        return i;
    }

    private Integer getPriority(List<IssuePriority> issuePriorities, IssueDTO holder) {
        issuePriorities = Lists.reverse(issuePriorities);
        for (IssuePriority p : issuePriorities) {
            if (p.getName().equals(holder.getPriority())) {
                return p.getId();
            }
        }
        return issuePriorities.get(0).getId();
    }
}
