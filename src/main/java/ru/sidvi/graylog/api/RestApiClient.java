package ru.sidvi.graylog.api;

import com.google.common.collect.Lists;
import com.taskadapter.redmineapi.*;
import com.taskadapter.redmineapi.bean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RestApiClient implements RedmineClient {

    private final Logger logger = LoggerFactory.getLogger(IssueDTO.class);

    private final String serverUrl;
    private String apiKey;

    public RestApiClient(String serverUrl, String apiKey) {
        this.serverUrl = serverUrl;
        this.apiKey = apiKey;
    }

    @Override
    public void create(IssueDTO holder) {

        try {
            RedmineManager mgr = RedmineManagerFactory.createWithApiKey(serverUrl, apiKey);
            IssueManager issueManager = mgr.getIssueManager();
            ProjectManager projectManager = mgr.getProjectManager();

            List<Issue> issuesInProject = issueManager.getIssues(holder.getProjectIdentifier(), null);
            String md5 = Utils.calculateMD5(holder.toString());
            if (!containsMD5(issuesInProject, md5)) {
                Integer priority = getPriority(issueManager.getIssuePriorities(), IssuePriorityFactory.create(0), holder);
                Project project = projectManager.getProjectByKey(holder.getProjectIdentifier());
                Issue issue = fillIssue(holder, project, priority);
                issue.setDescription(Utils.appendMD5(md5, issue.getDescription()));
                issueManager.createIssue(issue);
            }
        } catch (RedmineException e) {
            logger.error("", e);
        }
    }

    private boolean containsMD5(List<Issue> issues, String md5) {
        for (Issue i : issues) {
            String o = Utils.extractMD5(i.getDescription());
            if (md5.equals(o)) {
                return true;
            }
        }
        return false;
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
