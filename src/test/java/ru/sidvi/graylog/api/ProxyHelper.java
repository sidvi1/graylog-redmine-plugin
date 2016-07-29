package ru.sidvi.graylog.api;

import com.taskadapter.redmineapi.RedmineException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockserver.integration.ClientAndProxy;

/**
 * This class used as helper, to record request to Redmine server.
 *
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
@Ignore
public class ProxyHelper {
    private ClientAndProxy proxy;

    @Test
    public void recordAddIssue() throws RedmineException {
        RedmineClient client = new RestApiClient("http://localhost:1090", "ca6c9fff48c3740ae5bf41332989c70d1328035e");
        IssueDTO issue = new IssueDTO();
        issue.setProjectIdentifier("test1111");
        issue.setType("Bug");
        issue.setPriority("Minor");
        issue.setDescription("Description.");
        issue.setTitle("First issue");
        client.create(issue);
    }

    @Test
    public void recordGetAll() throws RedmineException {
        RedmineClient client = new RestApiClient("http://localhost:1090", "ca6c9fff48c3740ae5bf41332989c70d1328035e");
        client.getAll("test1111");
    }

    @Before
    public void startProxy() {
        proxy = new ClientAndProxy(1090, "www.hostedredmine.com", 80);
    }

    @After
    public void stopProxy() {
        proxy.dumpToLogAsJava();
        proxy.stop();
    }

}
