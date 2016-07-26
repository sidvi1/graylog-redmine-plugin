package ru.sidvi.graylog.template;

import org.graylog2.plugin.configuration.Configuration;
import ru.sidvi.graylog.DataExtractor;

import javax.inject.Inject;

/**
 * Create templates for issue fields.
 */
public class IssueTemplater {

    public static final String BODY_TEMPLATE = "##########\n" +
            "Alert Description: ${check_result.resultDescription}\n" +
            "Date: ${check_result.triggeredAt}\n" +
            "Stream ID: ${stream.id}\n" +
            "Stream title: ${stream.title}\n" +
            "Stream description: ${stream.description}\n" +
            "Alert Condition Title: ${alertCondition.title}\n" +
            "${if stream_url}Stream URL: ${stream_url}${end}\n" +
            "\n" +
            "Triggered condition: ${check_result.triggeredCondition}\n" +
            "##########\n\n" +
            "${if backlog}" +
            "Last messages accounting for this alert:\n" +
            "${foreach backlog message}" +
            "${message}\n\n" +
            "${end}" +
            "${else}" +
            "<No backlog>\n" +
            "${end}" +
            "\n";
    public static final String SUBJECT_TEMPLATE = "Graylog alert for stream: ${stream.title}: ${check_result.resultDescription}";
    public static final String SUBJECT = "r_subject";
    public static final String BODY = "r_body";

    private TemplateEngineAdapter engine;
    private Configuration configuration;

    @Inject
    public IssueTemplater(TemplateEngineAdapter builder, Configuration configuration) {
        this.engine = builder;
        this.configuration = configuration;
    }

    public String buildSubject(DataExtractor extractor) {
        String key = SUBJECT;
        return engine.processTemplate(extractor, fromConfigOrDefault(key, SUBJECT_TEMPLATE));
    }

    public String buildBody(DataExtractor extractor) {
        String key = BODY;
        return engine.processTemplate(extractor, fromConfigOrDefault(key, BODY_TEMPLATE));
    }

    private String fromConfigOrDefault(String key, String defaultTemplate) {
        if (configuration != null && configuration.getString(key) != null) {
            defaultTemplate = configuration.getString(key);
        }
        return defaultTemplate;
    }

}
