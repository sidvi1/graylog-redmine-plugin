package ru.sidvi.graylog;

import org.junit.Before;
import org.junit.Test;
import ru.sidvi.graylog.hash.Hash;
import ru.sidvi.graylog.hash.MD5Hash;
import ru.sidvi.graylog.api.IssueDTO;
import ru.sidvi.graylog.hash.IssueMarker;

import static org.junit.Assert.assertEquals;

public class IssueMarkerTest {
    private IssueDTO issue;
    private IssueMarker marker;


    @Before
    public void setUp() throws Exception {
        issue = new IssueDTO();
        issue.setTitle("title");
        issue.setDescription("description");
        issue.setProjectIdentifier("project_id");
        marker = new IssueMarker(new MD5Hash());
    }

    @Test
    public void shouldAddMarker() {
        IssueDTO actual = marker.append(issue);
        assertEquals("description" + Hash.BEGIN_MARKER + "274ee00348d5552d8cd72f7009c5a2d" + Hash.END_MARKER, actual.getDescription());
    }

    @Test
    public void shouldRemoveMarker() {
        IssueDTO expected = new IssueDTO(issue);
        issue = marker.append(issue);
        IssueDTO actual = marker.remove(issue);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldExtractMarker() {
        marker.append(issue);
        String hash = marker.extract(issue);
        assertEquals("274ee00348d5552d8cd72f7009c5a2d", hash);
    }
}
