package ru.sidvi.graylog.api;

import org.junit.*;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.Parameter;
import org.mockserver.model.StringBody;
import org.mockserver.socket.PortFactory;
import org.mockserver.verify.VerificationTimes;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class RestApiClientTest {

    private ClientAndServer MOCK_SERVER;
    private int FREE_PORT;
    private String URI;
    private String API_ACCESS_KEY = "value_from_redmine_settings";

    @Before
    public void startMockServer() {
        FREE_PORT = PortFactory.findFreePort();
        URI = "http://localhost:" + FREE_PORT;
        MOCK_SERVER = startClientAndServer(FREE_PORT);
    }

    @After
    public void stopMockServer() {
        MOCK_SERVER.stop();
    }

    @Test
    public void shouldAddIssue() {
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
                                .withBody("{\"issues\":[{\"id\":576029,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":3,\"name\":\"Low\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"Subject 2\",\"description\":\"Description\\r\\n\\u003e\\u003eMD5:4edb0b639cd2517384d725e22e652dbf\\r\\n\",\"done_ratio\":0,\"created_on\":\"2016-07-22T08:19:04Z\",\"updated_on\":\"2016-07-22T08:19:04Z\"},{\"id\":575873,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":3,\"name\":\"Low\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"Subject 1\",\"description\":\"Description\\r\\n\\u003e\\u003eMD5:9ad2767ab82696d3d5343e14ffec1c23\\r\\n\",\"done_ratio\":0,\"created_on\":\"2016-07-21T17:15:26Z\",\"updated_on\":\"2016-07-21T17:15:26Z\"},{\"id\":575870,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":3,\"name\":\"Low\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"Subject 1\",\"description\":\"Description\\r\\n\\u003e\\u003eMD5:9ad2767ab82696d3d5343e14ffec1c23\\r\\n\",\"done_ratio\":0,\"created_on\":\"2016-07-21T17:13:25Z\",\"updated_on\":\"2016-07-21T17:13:25Z\"},{\"id\":575867,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":7,\"name\":\"Immediate\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"Subject 1\",\"description\":\"Description\\r\\n\\u003e\\u003eMD5:9ad2767ab82696d3d5343e14ffec1c23\\r\\n\",\"done_ratio\":0,\"created_on\":\"2016-07-21T17:05:50Z\",\"updated_on\":\"2016-07-21T17:05:50Z\"},{\"id\":575861,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":7,\"name\":\"Immediate\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"Subject 1\",\"description\":\"Description\\r\\n\\u003e\\u003eMD5:9ad2767ab82696d3d5343e14ffec1c23\\r\\n\",\"done_ratio\":0,\"created_on\":\"2016-07-21T16:57:57Z\",\"updated_on\":\"2016-07-21T16:57:57Z\"},{\"id\":575860,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":4,\"name\":\"Normal\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"Subject 1\",\"description\":\"Description\\r\\n\\u003e\\u003eMD5:9ad2767ab82696d3d5343e14ffec1c23\\r\\n\",\"done_ratio\":0,\"created_on\":\"2016-07-21T16:56:32Z\",\"updated_on\":\"2016-07-21T16:56:32Z\"},{\"id\":575680,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":4,\"name\":\"Normal\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"Example\",\"done_ratio\":0,\"created_on\":\"2016-07-21T09:40:23Z\",\"updated_on\":\"2016-07-21T09:40:23Z\"},{\"id\":575679,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":4,\"name\":\"Normal\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"test123\",\"done_ratio\":0,\"created_on\":\"2016-07-21T09:37:56Z\",\"updated_on\":\"2016-07-21T09:37:56Z\"},{\"id\":575460,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":4,\"name\":\"Normal\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"test123\",\"done_ratio\":0,\"created_on\":\"2016-07-20T12:59:33Z\",\"updated_on\":\"2016-07-20T12:59:33Z\"},{\"id\":575458,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":4,\"name\":\"Normal\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"test123\",\"done_ratio\":0,\"created_on\":\"2016-07-20T12:55:02Z\",\"updated_on\":\"2016-07-20T12:55:02Z\"},{\"id\":575456,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":4,\"name\":\"Normal\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"assigned_to\":{\"id\":466,\"name\":\"Robert Lodico\"},\"category\":{\"id\":673,\"name\":\"Glitches/Bugs/Exploits\"},\"subject\":\"test123\",\"description\":\"\",\"done_ratio\":0,\"created_on\":\"2016-07-20T12:53:22Z\",\"updated_on\":\"2016-07-20T13:01:45Z\"},{\"id\":575455,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":4,\"name\":\"Normal\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"assigned_to\":{\"id\":466,\"name\":\"Robert Lodico\"},\"category\":{\"id\":673,\"name\":\"Glitches/Bugs/Exploits\"},\"subject\":\"test123\",\"done_ratio\":0,\"created_on\":\"2016-07-20T12:53:13Z\",\"updated_on\":\"2016-07-20T12:53:13Z\"},{\"id\":575452,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":1,\"name\":\"Bug\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":4,\"name\":\"Normal\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"test 1 manual\",\"description\":\"\",\"done_ratio\":0,\"created_on\":\"2016-07-20T12:40:21Z\",\"updated_on\":\"2016-07-20T12:40:21Z\"}],\"total_count\":13,\"offset\":0,\"limit\":25}")
                );

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
                                .withBody("{\"issue_priorities\":[{\"id\":3,\"name\":\"Low\"},{\"id\":4,\"name\":\"Normal\",\"is_default\":true},{\"id\":5,\"name\":\"High\"},{\"id\":6,\"name\":\"Urgent\"},{\"id\":7,\"name\":\"Immediate\"}]}")
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
                                .withBody("{\"project\":{\"id\":40667,\"name\":\"test1111\",\"identifier\":\"test1111\",\"description\":\"\",\"homepage\":\"\",\"status\":1,\"custom_fields\":[{\"id\":5,\"name\":\"Project Type\",\"value\":\"Personal Use\"}],\"trackers\":[{\"id\":4,\"name\":\"Task\"},{\"id\":2,\"name\":\"Feature\"},{\"id\":1,\"name\":\"Bug\"},{\"id\":3,\"name\":\"Support\"}],\"created_on\":\"2016-07-20T12:37:57Z\",\"updated_on\":\"2016-07-20T12:37:57Z\"}}")
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
                                .withBody("{\"issue\":{\"id\":576030,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":3,\"name\":\"Low\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"First issue\",\"description\":\"Description.\\r\\n\\u003e\\u003eMD5:861f1968a657663fe3c32e6af31afa68\\r\\n\",\"done_ratio\":0,\"created_on\":\"2016-07-22T08:27:47Z\",\"updated_on\":\"2016-07-22T08:27:47Z\",\"attachments\":[]}}")
                );


        //when
        RedmineClient client = new RestApiClient(URI, API_ACCESS_KEY);
        IssueBean issue = new IssueBean();
        issue.setProjectIdentifier("test1111");
        issue.setDescription("Description.");
        issue.setTitle("First issue");
        client.create(issue);

        //then
        MOCK_SERVER.verify(

                request()
                        .withMethod("GET")
                        .withPath("/issues.json"),
                request()
                        .withMethod("GET")
                        .withPath("/enumerations/issue_priorities.json"),
                request()
                        .withMethod("GET")
                        .withPath("/projects/test1111.json"),
                request()
                        .withMethod("POST")
                        .withPath("/issues.json")
                        .withBody(new StringBody("{\"issue\":{\"subject\":\"First issue\",\"priority_id\":3,\"project_id\":40667,\"start_date\":null,\"description\":\"Description.\\n>>MD5:861f1968a657663fe3c32e6af31afa68\\n\"}}"))

        );
    }


    @Test
    public void shouldSkipToAddIssueWithExistingMD5() {
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
                                .withBody("{\"issues\":[{\"id\":576030,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":3,\"name\":\"Low\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"First issue\",\"description\":\"Description.\\r\\n\\u003e\\u003eMD5:861f1968a657663fe3c32e6af31afa68\\r\\n\",\"done_ratio\":0,\"created_on\":\"2016-07-22T08:27:47Z\",\"updated_on\":\"2016-07-22T08:27:47Z\"},{\"id\":576029,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":3,\"name\":\"Low\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"Subject 2\",\"description\":\"Description\\r\\n\\u003e\\u003eMD5:4edb0b639cd2517384d725e22e652dbf\\r\\n\",\"done_ratio\":0,\"created_on\":\"2016-07-22T08:19:04Z\",\"updated_on\":\"2016-07-22T08:19:04Z\"},{\"id\":575873,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":3,\"name\":\"Low\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"Subject 1\",\"description\":\"Description\\r\\n\\u003e\\u003eMD5:9ad2767ab82696d3d5343e14ffec1c23\\r\\n\",\"done_ratio\":0,\"created_on\":\"2016-07-21T17:15:26Z\",\"updated_on\":\"2016-07-21T17:15:26Z\"},{\"id\":575870,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":3,\"name\":\"Low\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"Subject 1\",\"description\":\"Description\\r\\n\\u003e\\u003eMD5:9ad2767ab82696d3d5343e14ffec1c23\\r\\n\",\"done_ratio\":0,\"created_on\":\"2016-07-21T17:13:25Z\",\"updated_on\":\"2016-07-21T17:13:25Z\"},{\"id\":575867,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":7,\"name\":\"Immediate\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"Subject 1\",\"description\":\"Description\\r\\n\\u003e\\u003eMD5:9ad2767ab82696d3d5343e14ffec1c23\\r\\n\",\"done_ratio\":0,\"created_on\":\"2016-07-21T17:05:50Z\",\"updated_on\":\"2016-07-21T17:05:50Z\"},{\"id\":575861,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":7,\"name\":\"Immediate\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"Subject 1\",\"description\":\"Description\\r\\n\\u003e\\u003eMD5:9ad2767ab82696d3d5343e14ffec1c23\\r\\n\",\"done_ratio\":0,\"created_on\":\"2016-07-21T16:57:57Z\",\"updated_on\":\"2016-07-21T16:57:57Z\"},{\"id\":575860,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":4,\"name\":\"Normal\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"Subject 1\",\"description\":\"Description\\r\\n\\u003e\\u003eMD5:9ad2767ab82696d3d5343e14ffec1c23\\r\\n\",\"done_ratio\":0,\"created_on\":\"2016-07-21T16:56:32Z\",\"updated_on\":\"2016-07-21T16:56:32Z\"},{\"id\":575680,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":4,\"name\":\"Normal\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"Example\",\"done_ratio\":0,\"created_on\":\"2016-07-21T09:40:23Z\",\"updated_on\":\"2016-07-21T09:40:23Z\"},{\"id\":575679,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":4,\"name\":\"Normal\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"test123\",\"done_ratio\":0,\"created_on\":\"2016-07-21T09:37:56Z\",\"updated_on\":\"2016-07-21T09:37:56Z\"},{\"id\":575460,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":4,\"name\":\"Normal\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"test123\",\"done_ratio\":0,\"created_on\":\"2016-07-20T12:59:33Z\",\"updated_on\":\"2016-07-20T12:59:33Z\"},{\"id\":575458,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":4,\"name\":\"Normal\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"test123\",\"done_ratio\":0,\"created_on\":\"2016-07-20T12:55:02Z\",\"updated_on\":\"2016-07-20T12:55:02Z\"},{\"id\":575456,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":4,\"name\":\"Normal\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"assigned_to\":{\"id\":466,\"name\":\"Robert Lodico\"},\"category\":{\"id\":673,\"name\":\"Glitches/Bugs/Exploits\"},\"subject\":\"test123\",\"description\":\"\",\"done_ratio\":0,\"created_on\":\"2016-07-20T12:53:22Z\",\"updated_on\":\"2016-07-20T13:01:45Z\"},{\"id\":575455,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":4,\"name\":\"Task\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":4,\"name\":\"Normal\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"assigned_to\":{\"id\":466,\"name\":\"Robert Lodico\"},\"category\":{\"id\":673,\"name\":\"Glitches/Bugs/Exploits\"},\"subject\":\"test123\",\"done_ratio\":0,\"created_on\":\"2016-07-20T12:53:13Z\",\"updated_on\":\"2016-07-20T12:53:13Z\"},{\"id\":575452,\"project\":{\"id\":40667,\"name\":\"test1111\"},\"tracker\":{\"id\":1,\"name\":\"Bug\"},\"status\":{\"id\":1,\"name\":\"New\"},\"priority\":{\"id\":4,\"name\":\"Normal\"},\"author\":{\"id\":54220,\"name\":\"V S\"},\"subject\":\"test 1 manual\",\"description\":\"\",\"done_ratio\":0,\"created_on\":\"2016-07-20T12:40:21Z\",\"updated_on\":\"2016-07-20T12:40:21Z\"}],\"total_count\":14,\"offset\":0,\"limit\":25}")
                );

        //when
        RedmineClient client = new RestApiClient(URI, API_ACCESS_KEY);
        IssueBean issue = new IssueBean();
        issue.setProjectIdentifier("test1111");
        issue.setDescription("Description.");
        issue.setTitle("First issue");
        client.create(issue);

        //then
        MOCK_SERVER.verify(
                request(), VerificationTimes.once()
        );
    }

}
