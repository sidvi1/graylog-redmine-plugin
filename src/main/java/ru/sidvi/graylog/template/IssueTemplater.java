package ru.sidvi.graylog.template;

import org.apache.commons.lang.StringUtils;
import ru.sidvi.graylog.extractors.DataExtractor;

import javax.inject.Inject;

/**
 * Create templates for issue fields.
 *
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
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

    private TemplateEngine engine;

    @Inject
    public IssueTemplater(TemplateEngine engine) {
        this.engine = engine;
    }

    public String buildSubject(DataExtractor extractor, String value) {
        return engine.processTemplate(extractor, StringUtils.defaultIfEmpty(value, SUBJECT_TEMPLATE));
    }

    public String buildBody(DataExtractor extractor, String value) {
        return engine.processTemplate(extractor, StringUtils.defaultIfEmpty(value, BODY_TEMPLATE));
    }

}
