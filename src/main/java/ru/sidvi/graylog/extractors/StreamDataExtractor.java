package ru.sidvi.graylog.extractors;

import org.graylog2.plugin.MessageSummary;
import org.graylog2.plugin.Tools;
import org.graylog2.plugin.alarms.AlertCondition;
import org.graylog2.plugin.streams.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final Logger logger = LoggerFactory.getLogger(StreamDataExtractor.class);

    public StreamDataExtractor(Stream stream, AlertCondition.CheckResult result, URI webInterfaceUri) {
        this.stream = stream;
        this.result = result;
        this.webInterfaceUri = webInterfaceUri;
    }

    @Override
    public Map<String, Object> extract() {
        logger.info("Exract data from stream {} with id {}", stream.getTitle(), stream.getId());

        final AlertCondition alertCondition = result.getTriggeredCondition();
        final List<MessageSummary> matchingMessages = result.getMatchingMessages();

        String id = stream.getId();
        int time = calculateTime(result);
        String alertStart = Tools.getISO8601String(result.getTriggeredAt().minusMinutes(time));
        String alertEnd = Tools.getISO8601String(result.getTriggeredAt());
        String streamUrl = buildStreamDetailsURL(webInterfaceUri, id, alertStart, alertEnd);

        AlarmBacklogExtractor backlog = new AlarmBacklogExtractor(alertCondition, matchingMessages);
        Map<String, Object> result = new ModelBuilder()
                .addStream(stream)
                .addCheckResult(this.result)
                .addStreamUrl(streamUrl)
                .addBacklogMessages(backlog.extractMatchingMessages())
                .build();

        logger.info("Builded model. Model keys count {}", result.size());

        return result;
    }

    private String buildStreamDetailsURL(URI baseUri, String streamId, String alertStart, String alertEnd) {
        String result = baseUri + "/streams/" + streamId + "/messages?rangetype=absolute&from=" + alertStart + "&to=" + alertEnd + "&q=*";
        logger.info("Stream details uri is {}", result);
        return result;
    }

    private int calculateTime(AlertCondition.CheckResult checkResult) {
        int result = DEFAULT_TIME_COUNT;
        Map<String, Object> parameters = checkResult.getTriggeredCondition().getParameters();
        if (parameters.get("time") != null) {
            result = (int) parameters.get("time");
        }

        logger.info("Time count to retrive messages is {}", result);

        return result;
    }
}
