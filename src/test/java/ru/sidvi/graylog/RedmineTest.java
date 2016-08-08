package ru.sidvi.graylog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.sidvi.graylog.api.RedmineClient;
import ru.sidvi.graylog.api.RedmineClientFactory;
import ru.sidvi.graylog.marker.DescriptionUniqMarker;
import ru.sidvi.graylog.marker.UniqIssueMarker;
import ru.sidvi.graylog.marker.hash.Hash;
import ru.sidvi.graylog.marker.hash.MD5Hash;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * @author Vitaly Sidorov mail@vitaly-sidorov.com
 */
@RunWith(value = MockitoJUnitRunner.class)
public class RedmineTest {

    private static final String SERVER_URL = "http://somedomain.com";
    private static final String API_KEY = "some_api_key";
    private static final String PROJECT_IDENTIFIER = "PROJECT_IDENTIFIER";

    @InjectMocks
    private Redmine redmine;

    @Mock
    private RedmineClientFactory clientFactory;

    @Mock
    private RedmineClient client;

    private IssueDTO toSave;
    private List<IssueDTO> fromServer;

    @Before
    public void setUp() {
        UniqIssueMarker marker = new DescriptionUniqMarker(new MD5Hash());
        redmine = new Redmine(marker, clientFactory);

        toSave = createIssue("Subject", "Description", PROJECT_IDENTIFIER, "Bug", "Low");

        fromServer = new ArrayList<>();
        fromServer.add(createIssue("Subject 1", "Description 1", PROJECT_IDENTIFIER, "Bug", "Low"));
        fromServer.add(createIssue("Subject 2", "Description 2", PROJECT_IDENTIFIER, "Bug", "Low"));

        when(client.getAll(PROJECT_IDENTIFIER)).thenReturn(fromServer);
        when(clientFactory.create(SERVER_URL, API_KEY)).thenReturn(client);
    }


    @Test
    public void shouldSave() {
        //given

        //when
        redmine.saveIfNonExists(toSave, SERVER_URL, API_KEY);

        //then
        verify(client, times(1)).getAll(PROJECT_IDENTIFIER);
        verify(client, times(1)).create(toSave);
    }

    @Test
    public void shouldSkipSave() {
        //given
        String description = "Description" + Hash.BEGIN_MARKER + "42f0f9cb787c1f3101798d4b4198a513" + Hash.END_MARKER;
        fromServer.add(createIssue("Subject", description, PROJECT_IDENTIFIER, "Bug", "Low"));
        //when
        redmine.saveIfNonExists(toSave, SERVER_URL, API_KEY);

        //then
        verify(client, times(1)).getAll(PROJECT_IDENTIFIER);
        verify(client, times(0)).create(toSave);
    }

    private IssueDTO createIssue(String subject, String description, String projectIdentifier, String type, String priority) {
        IssueDTO toSave = new IssueDTO();
        toSave.setTitle(subject);
        toSave.setDescription(description);
        toSave.setProjectIdentifier(projectIdentifier);
        toSave.setType(type);
        toSave.setPriority(priority);
        return toSave;
    }
}
