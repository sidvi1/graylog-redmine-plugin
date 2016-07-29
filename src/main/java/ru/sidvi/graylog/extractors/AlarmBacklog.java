package ru.sidvi.graylog.extractors;

import com.google.common.collect.Lists;
import org.graylog2.plugin.Message;
import org.graylog2.plugin.MessageSummary;
import org.graylog2.plugin.alarms.AlertCondition;

import java.util.Collections;
import java.util.List;

/**
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
class AlarmBacklog {

    private AlertCondition alertCondition;
    private List<MessageSummary> matchingMessages;

    public AlarmBacklog(AlertCondition alertCondition, List<MessageSummary> matchingMessages) {
        this.alertCondition = alertCondition;
        this.matchingMessages = matchingMessages;
    }

    public List<Message> getMathingMessages() {
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
