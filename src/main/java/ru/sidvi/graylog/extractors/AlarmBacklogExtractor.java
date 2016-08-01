package ru.sidvi.graylog.extractors;

import com.google.common.collect.Lists;
import org.graylog2.plugin.Message;
import org.graylog2.plugin.MessageSummary;
import org.graylog2.plugin.alarms.AlertCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
class AlarmBacklogExtractor {

    private AlertCondition alertCondition;
    private List<MessageSummary> matchingMessages;

    private final Logger logger = LoggerFactory.getLogger(AlarmBacklogExtractor.class);

    public AlarmBacklogExtractor(AlertCondition alertCondition, List<MessageSummary> matchingMessages) {
        this.alertCondition = alertCondition;
        this.matchingMessages = matchingMessages;
    }

    public List<Message> extractMatchingMessages() {
        logger.info("Extract matching messages from backlog summaries");
        final int effectiveBacklogSize = Math.min(alertCondition.getBacklog(), matchingMessages.size());

        if (effectiveBacklogSize == 0) {
            return Collections.emptyList();
        }

        final List<MessageSummary> backlogSummaries = matchingMessages.subList(0, effectiveBacklogSize);

        final List<Message> backlog = Lists.newArrayListWithCapacity(effectiveBacklogSize);

        for (MessageSummary messageSummary : backlogSummaries) {
            backlog.add(messageSummary.getRawMessage());
        }

        logger.info("Backlog extracted message size is {}", backlog.size());
        return backlog;
    }
}
