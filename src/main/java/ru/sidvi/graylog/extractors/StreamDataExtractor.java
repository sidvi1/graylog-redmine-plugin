package ru.sidvi.graylog.extractors;

import com.google.common.collect.Lists;
import org.graylog2.plugin.Message;
import org.graylog2.plugin.MessageSummary;
import org.graylog2.plugin.alarms.AlertCondition;
import org.graylog2.plugin.streams.Stream;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * Extract data from Graylog stream based on alert condition.
 *
 * @author Vitaly Sidorov mail@vitaly-sidorov.com
 */
public class StreamDataExtractor implements DataExtractor {

    private static final int DEFAULT_MINUTES_AGO = 5;

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

        Integer backlogSize = result.getTriggeredCondition().getBacklog();
        List<MessageSummary> matchingMessages = result.getMatchingMessages();
        Object timeParam = result.getTriggeredCondition().getParameters().get("time");
        DateTime triggeredAt = result.getTriggeredAt();
        String streamUrl = new StreamDetailsUrlCreator(webInterfaceUri.toString(), stream.getId(), minutesAgo(timeParam), triggeredAt).createUrl();

        Map<String, Object> model = new ModelBuilder()
                .addStream(stream)
                .addCheckResult(result)
                .addStreamUrl(streamUrl)
                .addBacklogMessages(extractMatchingMessages(backlogSize, matchingMessages))
                .build();

        logger.debug("Builded model. Model keys count {}", model.size());
        return model;
    }

    private int minutesAgo(Object timeParam) {
        Integer result = defaultIfNull((Integer) timeParam, DEFAULT_MINUTES_AGO);
        logger.debug("Time count to retrive messages is {}", result);
        return result;
    }

    private List<Message> extractMatchingMessages(Integer backlogSize, List<MessageSummary> matchingMessages) {
        logger.debug("Extract matching messages from backlog summaries");
        final int effectiveBacklogSize = Math.min(backlogSize, matchingMessages.size());

        List<Message> backlog = Collections.emptyList();
        if (effectiveBacklogSize != 0) {
            backlog = Lists.newArrayListWithCapacity(effectiveBacklogSize);

            List<MessageSummary> backlogSummaries = matchingMessages.subList(0, effectiveBacklogSize);

            for (MessageSummary messageSummary : backlogSummaries) {
                backlog.add(messageSummary.getRawMessage());
            }
        }
        logger.debug("Backlog extracted message size is {}", backlog.size());
        return backlog;
    }
}
