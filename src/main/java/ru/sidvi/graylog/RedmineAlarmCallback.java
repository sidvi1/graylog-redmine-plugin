package ru.sidvi.graylog;

import org.graylog2.plugin.alarms.AlertCondition;
import org.graylog2.plugin.alarms.callbacks.AlarmCallback;
import org.graylog2.plugin.alarms.callbacks.AlarmCallbackConfigurationException;
import org.graylog2.plugin.alarms.callbacks.AlarmCallbackException;
import org.graylog2.plugin.configuration.Configuration;
import org.graylog2.plugin.configuration.ConfigurationException;
import org.graylog2.plugin.configuration.ConfigurationRequest;
import org.graylog2.plugin.configuration.fields.ConfigurationField;
import org.graylog2.plugin.configuration.fields.TextField;
import org.graylog2.plugin.streams.Stream;
import ru.sidvi.graylog.api.IssueBean;
import ru.sidvi.graylog.api.RedmineClient;
import ru.sidvi.graylog.api.RestApiClient;
import ru.sidvi.graylog.api.Utils;

import java.util.Map;

public class RedmineAlarmCallback implements AlarmCallback {
    private static final String SERVER_URL = "r_server_url";
    private static final String API_KEY = "r_api_key";
    private static final String PROJECT_IDENTIFIER = "r_project_identifier";
    private static final String ISSUE_TYPE = "r_issue_type";
    private static final String PRIORITY = "r_priority";
//    private static final String GRAYLOG_URL = "r_graylog_url";
    private Configuration configuration;

    @Override
    public void initialize(Configuration config) throws AlarmCallbackConfigurationException {
        this.configuration = config;
    }

    @Override
    public void call(Stream stream, AlertCondition.CheckResult result) throws AlarmCallbackException {
        String serverUrl = configuration.getString(SERVER_URL);
        String apiKey = configuration.getString(API_KEY);

        IssueBean issue = fillIssueFromForm();
        RedmineClient client = new RestApiClient(serverUrl, apiKey);
        client.create(issue);
    }

    private IssueBean fillIssueFromForm() {
        IssueBean issue = new IssueBean();
        issue.setProjectIdentifier(configuration.getString(PROJECT_IDENTIFIER));
        issue.setType(configuration.getString(ISSUE_TYPE));
        issue.setPriority(configuration.getString(PRIORITY));
        issue.setDescription(buildDescription());
        issue.setTitle(buildTitle());
        return issue;
    }

    private String buildTitle() {
        return "TITLE"; //TODO: add same as description
    }

    private String buildDescription() {
        return "DESCRIPTION TODO: \n"; //TODO: add description from template. using java template engine
    }

    @Override
    public ConfigurationRequest getRequestedConfiguration() {
        final ConfigurationRequest configurationRequest = new ConfigurationRequest();

        configurationRequest.addField (new TextField(
                SERVER_URL, "Redmine url", "", "Url to Redmine server.",
                ConfigurationField.Optional.NOT_OPTIONAL));

        configurationRequest.addField (new TextField(
                API_KEY, "Redmine api key", "", "Api key for Redmine server.",
                ConfigurationField.Optional.NOT_OPTIONAL));

        configurationRequest.addField (new TextField (
                PROJECT_IDENTIFIER, "Redmine project identifier", "", "Identifier for project under which the issue will be created.",
                ConfigurationField.Optional.NOT_OPTIONAL));

        configurationRequest.addField (new TextField (
                ISSUE_TYPE, "Redmine issue Type", "Bug", "Type of issue.",
                ConfigurationField.Optional.NOT_OPTIONAL));

        configurationRequest.addField (new TextField (
                PRIORITY, "Redmine issue priority", "Minor", "Priority of the issue.",
                ConfigurationField.Optional.OPTIONAL));

//        configurationRequest.addField (new TextField (
//                GRAYLOG_URL, "Graylog URL", null, "URL to your Graylog web interface. Used to build links in alarm notification.",
//                ConfigurationField.Optional.NOT_OPTIONAL));


        return configurationRequest;
    }

    @Override
    public String getName() {
        return "Graylog Redmine integration plugin";
    }

    @Override
    public Map<String, Object> getAttributes() {
        return configuration.getSource();
    }

    @Override
    public void checkConfiguration() throws ConfigurationException {

    }
}
