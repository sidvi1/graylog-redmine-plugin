package ru.sidvi.graylog.api;

import com.google.common.collect.Lists;
import com.taskadapter.redmineapi.*;
import com.taskadapter.redmineapi.bean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RestApiClient implements RedmineClient {

    private final Logger logger = LoggerFactory.getLogger(IssueBean.class);

    private final String serverUrl;
    private String apiKey;

    public RestApiClient(String serverUrl, String apiKey) {
        this.serverUrl = serverUrl;
        this.apiKey = apiKey;
    }

    @Override
    public void create(IssueBean holder) {

        try {
            RedmineManager mgr = RedmineManagerFactory.createWithApiKey(serverUrl, apiKey);
            List<Issue> issuesInProject = getIssuesFromProject(holder, mgr);
            String md5 = Utils.calculateMD5(holder.toString());
            if (!containsMD5(issuesInProject, md5)) {
                Issue issue = fillIssue(holder, mgr);
                issue.setDescription(Utils.appendMD5(md5, issue.getDescription()));
                mgr.getIssueManager().createIssue(issue);
            }
        } catch (RedmineException e) {
            logger.error("", e);
        }
    }

    private List<Issue> getIssuesFromProject(IssueBean holder, RedmineManager mgr) throws RedmineException {
        IssueManager issueManager = mgr.getIssueManager();
        return issueManager.getIssues(holder.getProjectIdentifier(), null);
    }

    private Issue fillIssue(IssueBean holder, RedmineManager mgr) throws RedmineException {
        Issue issue1 = IssueFactory.createWithSubject(holder.getTitle());
//        issue1.setTracker(getTracker(mgr, holder)); //TODO: bug with tracker! ??
        issue1.setPriorityId(getPriority(mgr, holder));
        issue1.setDescription(holder.getDescription());
        ProjectManager projectManager = mgr.getProjectManager();
        Project projectByKey = projectManager.getProjectByKey(holder.getProjectIdentifier());
        issue1.setProject(projectByKey);
        return issue1;
    }

    private Integer getPriority(RedmineManager mgr, IssueBean holder) {
        try {
            List<IssuePriority> issuePriorities = mgr.getIssueManager().getIssuePriorities();
            issuePriorities = Lists.reverse(issuePriorities);
            IssuePriority last = IssuePriorityFactory.create(0); //TODO: should test and think
            for (IssuePriority p : issuePriorities) {
                if (p.getName().equals(holder.getPriority())) {
                    return p.getId();
                }
                last = p;
            }
            return last.getId();
        } catch (RedmineException e) {
            logger.error("", e);
        }

        return 0;
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

    private Tracker getTracker(RedmineManager mgr, IssueBean holder) throws RedmineException {
        List<Tracker> trackers = mgr.getIssueManager().getTrackers();
        Tracker last = TrackerFactory.create(0); //TODO: should test and think
        for (Tracker t : trackers) {
            if (t.getName().equals(holder.getType())) {
                return t;
            }
            last = t;
        }
        return last;
    }

}
