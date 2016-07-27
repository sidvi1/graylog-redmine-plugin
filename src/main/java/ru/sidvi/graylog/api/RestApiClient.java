package ru.sidvi.graylog.api;

import com.google.common.collect.Lists;
import com.taskadapter.redmineapi.*;
import com.taskadapter.redmineapi.bean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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
            Integer priority = getPriority(issueManager.getIssuePriorities(), IssuePriorityFactory.create(0), holder);
            Project project = projectManager.getProjectByKey(holder.getProjectIdentifier());
            Issue issue = fillIssue(holder, project, priority);
            issueManager.createIssue(issue);
        } catch (RedmineException e) {
            logger.error("", e);
        }
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
            }
        } catch (RedmineException e) {
            e.printStackTrace();
        }
        return issueDTOs;
    }


    private Issue fillIssue(IssueDTO holder, Project project, Integer priority) throws RedmineException {
        Issue i = IssueFactory.create(project.getId(), holder.getTitle());
//        issue1.setTracker(getTracker(mgr, holder)); //TODO: bug with tracker! ??
        i.setPriorityId(priority);
        i.setDescription(holder.getDescription());
        i.setProject(project);
        return i;
    }

    private Integer getPriority(List<IssuePriority> issuePriorities, IssuePriority defaultValue, IssueDTO holder) {
        issuePriorities = Lists.reverse(issuePriorities);
        for (IssuePriority p : issuePriorities) {
            if (p.getName().equals(holder.getPriority())) {
                return p.getId();
            }
            defaultValue = p;
        }
        return defaultValue.getId();
    }

    private Tracker getTracker(List<Tracker> trackers, Tracker defaultTracker, IssueDTO holder) throws RedmineException {
        for (Tracker t : trackers) {
            if (t.getName().equals(holder.getType())) {
                return t;
            }
            defaultTracker = t;
        }
        return defaultTracker;
    }
}
