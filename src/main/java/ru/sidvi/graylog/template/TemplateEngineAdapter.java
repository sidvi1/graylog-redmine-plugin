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
import ru.sidvi.graylog.DataExtractor;

import javax.inject.Inject;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Strings.isNullOrEmpty;

public class TemplateEngineAdapter {

    public String processTemplate(DataExtractor extractor, String template) {
        Map<String, Object> model = extractor.extract();
        Engine engine = new Engine();
        return engine.transform(template, model);
    }
}
