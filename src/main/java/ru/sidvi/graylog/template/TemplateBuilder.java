package ru.sidvi.graylog.template;

import com.floreysoft.jmte.Engine;
import com.google.common.collect.Lists;
import org.graylog2.configuration.EmailConfiguration;
import org.graylog2.plugin.Message;
import org.graylog2.plugin.MessageSummary;
import org.graylog2.plugin.Tools;
import org.graylog2.plugin.alarms.AlertCondition;
import org.graylog2.plugin.configuration.Configuration;
import org.graylog2.plugin.streams.Stream;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Strings.isNullOrEmpty;

public class TemplateBuilder {

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

    private Configuration pluginConfig;
    protected EmailConfiguration configuration;

//    @Inject
    public TemplateBuilder(Configuration pluginConfig, EmailConfiguration configuration) {
        this.pluginConfig = pluginConfig;
        this.configuration = configuration;
    }

    public String buildSubject(Stream stream, AlertCondition.CheckResult checkResult) {
        return processTemplate(stream, checkResult, SUBJECT, SUBJECT_TEMPLATE);
    }

    public String buildBody(Stream stream, AlertCondition.CheckResult checkResult) {
        return processTemplate(stream, checkResult, BODY, BODY_TEMPLATE);
    }

    private String processTemplate(Stream stream, AlertCondition.CheckResult checkResult, String key, String tpl) {
        List<Message> backlog = getAlarmBacklog(checkResult);
        String template;
        if (pluginConfig == null || pluginConfig.getString(key) == null) {
            template = tpl;
        } else {
            template = pluginConfig.getString(key);
        }
        Map<String, Object> model = getModel(stream, checkResult, backlog);
        Engine engine = new Engine();

        return engine.transform(template, model);
    }

    private Map<String, Object> getModel(Stream stream, AlertCondition.CheckResult checkResult, List<Message> backlog) {
        Map<String, Object> model = new HashMap<>();
        model.put("stream", stream);
        model.put("check_result", checkResult);
        model.put("stream_url", buildStreamDetailsURL(configuration.getWebInterfaceUri(), checkResult, stream));
        model.put("alertCondition", checkResult.getTriggeredCondition());

        final List<Message> messages = firstNonNull(backlog, Collections.<Message>emptyList());
        model.put("backlog", messages);
        model.put("backlog_size", messages.size());

        return model;
    }

    private String buildStreamDetailsURL(URI baseUri, AlertCondition.CheckResult checkResult, Stream stream) {
        // Return an informational message if the web interface URL hasn't been set
        if (baseUri == null || isNullOrEmpty(baseUri.getHost())) {
            return "Please configure 'transport_email_web_interface_url' in your Graylog configuration file.";
        }

        int time = 5;
        if (checkResult.getTriggeredCondition().getParameters().get("time") != null) {
            time = (int) checkResult.getTriggeredCondition().getParameters().get("time");
        }

        DateTime dateAlertEnd = checkResult.getTriggeredAt();
        DateTime dateAlertStart = dateAlertEnd.minusMinutes(time);
        String alertStart = Tools.getISO8601String(dateAlertStart);
        String alertEnd = Tools.getISO8601String(dateAlertEnd);

        return baseUri + "/streams/" + stream.getId() + "/messages?rangetype=absolute&from=" + alertStart + "&to=" + alertEnd + "&q=*";
    }

    public List<Message> getAlarmBacklog(AlertCondition.CheckResult result) {
        final AlertCondition alertCondition = result.getTriggeredCondition();
        final List<MessageSummary> matchingMessages = result.getMatchingMessages();

        final int effectiveBacklogSize = Math.min(alertCondition.getBacklog(), matchingMessages.size());

        if (effectiveBacklogSize == 0) {
            return Collections.emptyList();
        }

        final List<MessageSummary> backlogSummaries = matchingMessages.subList(0, effectiveBacklogSize);

        final List<Message> backlog = Lists.newArrayListWithCapacity(effectiveBacklogSize);

        for (MessageSummary messageSummary : backlogSummaries) {
            backlog.add(messageSummary.getRawMessage());
        }

        return backlog;
    }
}
