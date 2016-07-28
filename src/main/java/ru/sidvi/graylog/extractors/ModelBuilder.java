package ru.sidvi.graylog.extractors;

import org.graylog2.plugin.Message;
import org.graylog2.plugin.alarms.AlertCondition;
import org.graylog2.plugin.streams.Stream;
import ru.sidvi.graylog.template.TemplateKeywords;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.MoreObjects.firstNonNull;

/**
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
class ModelBuilder {

    Map<String, Object> model = new HashMap<>();

    public ModelBuilder addStream(Stream stream) {
        model.put(TemplateKeywords.STREAM, stream);
        return this;
    }

    public ModelBuilder addCheckResult(AlertCondition.CheckResult checkResult) {
        model.put(TemplateKeywords.CHECK_RESULT, checkResult);
        model.put(TemplateKeywords.ALERT_CONDITION, checkResult.getTriggeredCondition());
        return  this;
    }

    public ModelBuilder addStreamUrl(String streamUrl) {
        model.put(TemplateKeywords.STREAM_URL, streamUrl);
        return this;
    }

    public ModelBuilder addBacklogMessages(List<Message> backlog){
        final List<Message> messages = firstNonNull(backlog, Collections.<Message>emptyList());
        model.put(TemplateKeywords.BACKLOG, messages);
        model.put(TemplateKeywords.BACKLOG_SIZE, messages.size());
        return this;
    }

    public Map<String, Object> build() {
        return model;
    }
}
