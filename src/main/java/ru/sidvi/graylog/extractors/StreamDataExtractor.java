package ru.sidvi.graylog.extractors;

import org.graylog2.plugin.MessageSummary;
import org.graylog2.plugin.Tools;
import org.graylog2.plugin.alarms.AlertCondition;
import org.graylog2.plugin.streams.Stream;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Extract data from Graylog stream based on alert condition.
 *
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
public class StreamDataExtractor implements DataExtractor {

    private static final int DEFAULT_TIME_COUNT = 5;

    private final Stream stream;
    private final AlertCondition.CheckResult result;
    private URI webInterfaceUri;

    public StreamDataExtractor(Stream stream, AlertCondition.CheckResult result, URI webInterfaceUri) {
        this.stream = stream;
        this.result = result;
        this.webInterfaceUri = webInterfaceUri;
    }

    @Override
    public Map<String, Object> extract() {

        final AlertCondition alertCondition = result.getTriggeredCondition();
        final List<MessageSummary> matchingMessages = result.getMatchingMessages();
        AlarmBacklog backlog = new AlarmBacklog(alertCondition, matchingMessages);

        String id = stream.getId();
        int time = calculateTime(result);
        String alertStart = Tools.getISO8601String(result.getTriggeredAt().minusMinutes(time));
        String alertEnd = Tools.getISO8601String(result.getTriggeredAt());
        String streamUrl = buildStreamDetailsURL(webInterfaceUri, id, alertStart, alertEnd);

        return new ModelBuilder()
                .addStream(stream)
                .addCheckResult(result)
                .addStreamUrl(streamUrl)
                .addBacklogMessages(backlog.getMathingMessages())
                .build();
    }

    private String buildStreamDetailsURL(URI baseUri, String streamId, String alertStart, String alertEnd) {
        return baseUri + "/streams/" + streamId + "/messages?rangetype=absolute&from=" + alertStart + "&to=" + alertEnd + "&q=*";
    }

    private int calculateTime(AlertCondition.CheckResult checkResult) {
        Map<String, Object> parameters = checkResult.getTriggeredCondition().getParameters();
        if (parameters.get("time") != null) {
            return (int) parameters.get("time");
        }
        return DEFAULT_TIME_COUNT;
    }
}
