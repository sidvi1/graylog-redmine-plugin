package ru.sidvi.graylog.extractors;

import org.graylog2.plugin.Message;
import org.graylog2.plugin.alarms.AlertCondition;
import org.graylog2.plugin.streams.Stream;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.MoreObjects.firstNonNull;

/**
 * @author Vitaly Sidorov mail@vitaly-sidorov.com
 */
class ModelBuilder {

    Map<String, Object> model = new HashMap<>();

    public ModelBuilder addStream(Stream stream) {
        model.put(ModelKeys.STREAM, stream);
        return this;
    }

    public ModelBuilder addCheckResult(AlertCondition.CheckResult checkResult) {
        model.put(ModelKeys.CHECK_RESULT, checkResult);
        model.put(ModelKeys.ALERT_CONDITION, checkResult.getTriggeredCondition());
        return this;
    }

    public ModelBuilder addStreamUrl(String streamUrl) {
        model.put(ModelKeys.STREAM_URL, streamUrl);
        return this;
    }

    public ModelBuilder addBacklogMessages(List<Message> backlog) {
        final List<Message> messages = firstNonNull(backlog, Collections.<Message>emptyList());
        model.put(ModelKeys.BACKLOG, messages);
        model.put(ModelKeys.BACKLOG_SIZE, messages.size());
        return this;
    }

    public Map<String, Object> build() {
        return model;
    }

    /**
     * Holds keywords used in template.
     *
     * @author Vitaly Sidorov mail@vitaly-sidorov.com
     */
    private static final class ModelKeys {

        public static final String BACKLOG_SIZE = "backlog_size";
        public static final String BACKLOG = "backlog";
        public static final String ALERT_CONDITION = "alert_condition";
        public static final String STREAM_URL = "stream_url";
        public static final String CHECK_RESULT = "check_result";
        public static final String STREAM = "stream";
    }
}
