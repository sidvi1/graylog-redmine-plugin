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

    private TemplateEngineAdapter engine;

    @Inject
    public IssueTemplater(TemplateEngineAdapter builder) {
        this.engine = builder;
    }

    public String buildSubject(DataExtractor extractor,String value) {
        return engine.processTemplate(extractor, defaultIfEmpty(value, SUBJECT_TEMPLATE));
    }

    public String buildBody(DataExtractor extractor, String value) {
        return engine.processTemplate(extractor, defaultIfEmpty(value,BODY_TEMPLATE));
    }

    private String defaultIfEmpty(String value, String defaultValue) {
        if(value != null && value.length() != 0) {
            return value;
        }
        return defaultValue;
    }
}
