package ru.sidvi.graylog.api;

import org.junit.*;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.Parameter;
import org.mockserver.model.StringBody;
import org.mockserver.socket.PortFactory;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static ru.sidvi.graylog.TestUtils.fromResource;

/**
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
public class RestApiClientTest {

    private static ClientAndServer MOCK_SERVER;
    private static int FREE_PORT;
    private static String URI;
    private String API_ACCESS_KEY = "value_from_redmine_settings";

    @BeforeClass
    public static void setUp() {
        FREE_PORT = PortFactory.findFreePort();
        URI = "http://localhost:" + FREE_PORT;
        MOCK_SERVER = startClientAndServer(FREE_PORT);
    }

    @AfterClass
    public static void tearDown() {
        MOCK_SERVER.stop();
    }

    @Before
    public void reset(){
        MOCK_SERVER.reset();
    }

    @Test
    public void shouldAddIssue() throws IOException {
        //given
        MOCK_SERVER
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/enumerations/issue_priorities.json")
                                .withQueryStringParameters(
                                        new Parameter("limit", "25"),
                                        new Parameter("offset", "0"),
                                        new Parameter("key", API_ACCESS_KEY)
                                )
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8")
                                )
                                .withBody(fromResource("redmine_requests/get_issue_priorities_response.json"))
                );

        MOCK_SERVER
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/projects/test1111.json")
                                .withQueryStringParameters(
                                        new Parameter("include", "trackers"),
                                        new Parameter("key", API_ACCESS_KEY)
                                )
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8")
                                )
                                .withBody(fromResource("redmine_requests/get_project_issues_response.json"))
                );

        MOCK_SERVER
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/trackers.json")
                                .withQueryStringParameters(
                                        new Parameter("limit", "25"),
                                        new Parameter("offset", "0"),
                                        new Parameter("key", API_ACCESS_KEY)
                                )
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8")
                                )
                                .withBody(fromResource("redmine_requests/get_trackers_response.json"))
                );

        MOCK_SERVER
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/issues.json")
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8")
                                )
                                .withQueryStringParameters(
                                        new Parameter("include", "attachments"),
                                        new Parameter("key", API_ACCESS_KEY)
                                )
                )
                .respond(
                        response()
                                .withStatusCode(201)
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8")
                                )
                                .withBody(fromResource("redmine_requests/post_issue_response.json"))
                );


        //when
        RestApiClient client = new RestApiClient(URI, API_ACCESS_KEY);
        IssueDTO issue = new IssueDTO();
        issue.setProjectIdentifier("test1111");
        issue.setType("Bug");
        issue.setPriority("Immediate");
        issue.setDescription("Description.");
        issue.setTitle("First issue");
        client.create(issue);

        //then
        MOCK_SERVER.verify(
                request()
                        .withMethod("GET")
                        .withPath("/enumerations/issue_priorities.json"),
                request()
                        .withMethod("GET")
                        .withPath("/projects/test1111.json"),
                request()
                        .withMethod("GET")
                        .withPath("/trackers.json"),
                request()
                        .withMethod("POST")
                        .withPath("/issues.json")
                        .withBody(new StringBody(fromResource("redmine_requests/post_issues_request.json")))

        );
    }

    @Test
    public void shouldGetAllIssues() throws IOException {
        //given
        MOCK_SERVER
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/issues.json")
                                .withQueryStringParameters(
                                        new Parameter("project_id", "test1111"),
                                        new Parameter("include", ""),
                                        new Parameter("limit", "25"),
                                        new Parameter("offset", "0"),
                                        new Parameter("key", API_ACCESS_KEY)
                                )
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8")
                                )
                                .withBody(fromResource("redmine_requests/get_all_response.json"))
                );


        //when
        RestApiClient client = new RestApiClient(URI, API_ACCESS_KEY);
        List<IssueDTO> actual = client.getAll("test1111");

        //then
        MOCK_SERVER.verify(
                request()
                        .withMethod("GET")
                        .withPath("/issues.json")
        );

        assertEquals(2, actual.size());
        
        IssueDTO issue1 = new IssueDTO();
        issue1.setTitle("Subject 1");
        issue1.setDescription("Description 1");
        issue1.setType("Bug");
        issue1.setProjectIdentifier("test1111");
        issue1.setPriority("Immediate");
        assertEquals(issue1, actual.get(0));

        IssueDTO issue2 = new IssueDTO();
        issue2.setTitle("Subject 2");
        issue2.setDescription("Description 2");
        issue2.setType("Bug");
        issue2.setProjectIdentifier("test1111");
        issue2.setPriority("Minor");
        assertEquals(issue2, actual.get(1));
    }
}
