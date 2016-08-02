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
    private final Logger logger = LoggerFactory.getLogger(StreamDataExtractor.class);
    private URI webInterfaceUri;

    public StreamDataExtractor(Stream stream, AlertCondition.CheckResult result, URI webInterfaceUri) {
        this.stream = stream;
        this.result = result;
        this.webInterfaceUri = webInterfaceUri;
    }

    @Override
    public Map<String, Object> extract() {
        logger.debug("Exract data from stream {} with id {}", stream.getTitle(), stream.getId());

        AlarmBacklogExtractor backlog = new AlarmBacklogExtractor(result.getTriggeredCondition(), result.getMatchingMessages());
        Map<String, Object> result = new ModelBuilder()
                .addStream(stream)
                .addCheckResult(this.result)
                .addStreamUrl(buildStreamDetailsURL())
                .addBacklogMessages(backlog.extractMatchingMessages())
                .build();

        logger.debug("Builded model. Model keys count {}", result.size());

        return result;
    }

    private String buildStreamDetailsURL() {
        String id = stream.getId();
        int time = calculateTime(result);
        String alertStart = Tools.getISO8601String(result.getTriggeredAt().minusMinutes(time));
        String alertEnd = Tools.getISO8601String(result.getTriggeredAt());

        String result = webInterfaceUri + "/streams/" + id + "/messages?rangetype=absolute&from=" + alertStart + "&to=" + alertEnd + "&q=*";

        logger.debug("Stream details uri is {}", result);
        return result;
    }

    private int calculateTime(AlertCondition.CheckResult checkResult) {
        int result = DEFAULT_TIME_COUNT;
        Map<String, Object> parameters = checkResult.getTriggeredCondition().getParameters();
        if (parameters.get("time") != null) {
            result = (int) parameters.get("time");
        }

        logger.debug("Time count to retrive messages is {}", result);

        return result;
    }
}
