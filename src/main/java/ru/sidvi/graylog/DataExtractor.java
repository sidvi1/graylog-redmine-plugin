package ru.sidvi.graylog;

import com.google.common.collect.Lists;
import org.graylog2.plugin.Message;
import org.graylog2.plugin.MessageSummary;
import org.graylog2.plugin.Tools;
import org.graylog2.plugin.alarms.AlertCondition;
import org.graylog2.plugin.streams.Stream;
import ru.sidvi.graylog.template.TemplateKeywords;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.MoreObjects.firstNonNull;

/**
 * Extract data from Graylog stream and message.
 */
public class DataExtractor {

    private final Stream stream;
    private final AlertCondition.CheckResult result;
    private URI webInterfaceUri;

    public DataExtractor(Stream stream, AlertCondition.CheckResult result, URI webInterfaceUri) {
        this.stream = stream;
        this.result = result;
        this.webInterfaceUri = webInterfaceUri;
    }

    public Map<String, Object> extract() {
        List<Message> backlog = getAlarmBacklog(result);
        return getModel(stream, result, backlog);
    }

    private Map<String, Object> getModel(Stream stream, AlertCondition.CheckResult checkResult, List<Message> backlog) {
        Map<String, Object> model = new HashMap<>();
        model.put(TemplateKeywords.STREAM, stream);
        model.put(TemplateKeywords.CHECK_RESULT, checkResult);
        model.put(TemplateKeywords.ALERT_CONDITION, checkResult.getTriggeredCondition());

        final List<Message> messages = firstNonNull(backlog, Collections.<Message>emptyList());
        model.put(TemplateKeywords.BACKLOG, messages);
        model.put(TemplateKeywords.BACKLOG_SIZE, messages.size());

        String id = stream.getId();
        int time = calculateTime(checkResult);
        String alertStart = Tools.getISO8601String(checkResult.getTriggeredAt().minusMinutes(time));
        String alertEnd = Tools.getISO8601String(checkResult.getTriggeredAt());
        String streamUrl = buildStreamDetailsURL(webInterfaceUri, id, alertStart, alertEnd);
        model.put(TemplateKeywords.STREAM_URL, streamUrl);

        return model;
    }

    private String buildStreamDetailsURL(URI baseUri, String streamId, String alertStart, String alertEnd) {
        return baseUri + "/streams/" + streamId + "/messages?rangetype=absolute&from=" + alertStart + "&to=" + alertEnd + "&q=*";
    }

    private int calculateTime(AlertCondition.CheckResult checkResult) {
        int time = 5;
        if (checkResult.getTriggeredCondition().getParameters().get("time") != null) {
            time = (int) checkResult.getTriggeredCondition().getParameters().get("time");
        }
        return time;
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
