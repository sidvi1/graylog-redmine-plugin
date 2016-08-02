package ru.sidvi.graylog;

import org.graylog2.configuration.EmailConfiguration;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sidvi.graylog.extractors.DataExtractor;
import ru.sidvi.graylog.extractors.StreamDataExtractor;
import ru.sidvi.graylog.template.TemplateEngine;

import javax.inject.Inject;
import java.net.URI;
import java.util.Map;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.apache.commons.lang.StringUtils.defaultIfEmpty;
import static ru.sidvi.graylog.Utils.fromResource;

/**
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
public class RedmineAlarmCallback implements AlarmCallback {
    private static final String BODY_TEMPLATE = fromResource("body_template.tpl");
    private static final String SUBJECT_TEMPLATE = fromResource("subject_template.tpl");
    private static final String SERVER_URL = "r_server_url";
    private static final String API_KEY = "r_api_key";
    private static final String PROJECT_IDENTIFIER = "r_project_identifier";
    private static final String ISSUE_TYPE = "r_issue_type";
    private static final String PRIORITY = "r_priority";
    private static final String BODY = "r_body";
    private static final String SUBJECT = "r_subject";

    private final Logger logger = LoggerFactory.getLogger(RedmineAlarmCallback.class);

    private Configuration configuration;
    private Redmine readmine;
    private TemplateEngine engine;
    private EmailConfiguration emailConfig;

    @Inject
    public RedmineAlarmCallback(Redmine readmine, TemplateEngine engine, EmailConfiguration emailConfig) {
        this.readmine = readmine;
        this.engine = engine;
        this.emailConfig = emailConfig;
    }

    @Override
    public void initialize(Configuration config) throws AlarmCallbackConfigurationException {
        this.configuration = config;
    }

    @Override
    public void call(Stream stream, AlertCondition.CheckResult result) throws AlarmCallbackException {
        String serverUrl = configuration.getString(SERVER_URL);
        String apiKey = configuration.getString(API_KEY);

        DataExtractor extractor = new StreamDataExtractor(stream, result, getBaseUri());
        Map<String, Object> values = extractor.extract();
        logger.info("Values extracted from stream. Values count is {}", values.size());
        IssueDTO issue = fillIssueFromForm(values);

        readmine.saveIfNonExists(issue, serverUrl, apiKey);
    }

    private IssueDTO fillIssueFromForm(Map<String, Object> values) {
        IssueDTO issue = new IssueDTO();
        issue.setProjectIdentifier(configuration.getString(PROJECT_IDENTIFIER));
        issue.setType(configuration.getString(ISSUE_TYPE));
        issue.setPriority(configuration.getString(PRIORITY));
        issue.setDescription(engine.processTemplate(values, defaultIfEmpty(configuration.getString(BODY), BODY_TEMPLATE)));
        issue.setTitle(engine.processTemplate(values, defaultIfEmpty(configuration.getString(SUBJECT), SUBJECT_TEMPLATE)));
        return issue;
    }

    private URI getBaseUri() {
        URI webInterfaceUri = emailConfig.getWebInterfaceUri();
        // Return an informational message if the web interface URL hasn't been set
        if (webInterfaceUri == null || isNullOrEmpty(webInterfaceUri.getHost())) {
            logger.warn("Please configure 'transport_email_web_interface_url' in your Graylog configuration file.");
        }
        return webInterfaceUri;
    }

    @Override
    public ConfigurationRequest getRequestedConfiguration() {
        final ConfigurationRequest configurationRequest = new ConfigurationRequest();

        configurationRequest.addField(new TextField(
                SERVER_URL, "Server url", "", "Url to Redmine server.",
                ConfigurationField.Optional.NOT_OPTIONAL));

        configurationRequest.addField(new TextField(
                API_KEY, "Api key", "", "Api key for Redmine server.",
                ConfigurationField.Optional.NOT_OPTIONAL));

        configurationRequest.addField(new TextField(
                PROJECT_IDENTIFIER, "Project identifier", "", "Identifier for project under which the swill be created.",
                ConfigurationField.Optional.NOT_OPTIONAL));

        configurationRequest.addField(new TextField(
                SUBJECT,
                "Issue subject template",
                SUBJECT_TEMPLATE,
                "The template to generate subject from.",
                ConfigurationField.Optional.NOT_OPTIONAL
        ));

        configurationRequest.addField(new TextField(
                BODY,
                "Issue description tempate",
                BODY_TEMPLATE,
                "The template to generate the description from.",
                ConfigurationField.Optional.NOT_OPTIONAL,
                TextField.Attribute.TEXTAREA));

        configurationRequest.addField(new TextField(
                ISSUE_TYPE, "Issue tracker", "Bug", "Tracker for issue.",
                ConfigurationField.Optional.OPTIONAL));

        configurationRequest.addField(new TextField(
                PRIORITY, "Issue priority", "Low", "Priority of the issue.",
                ConfigurationField.Optional.OPTIONAL));


        return configurationRequest;
    }

    @Override
    public String getName() {
        return "Redmine Alarm Callback";
    }

    @Override
    public Map<String, Object> getAttributes() {
        return configuration.getSource();
    }

    @Override
    public void checkConfiguration() throws ConfigurationException {

    }
}
