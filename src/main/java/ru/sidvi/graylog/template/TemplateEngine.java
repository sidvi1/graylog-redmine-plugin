package ru.sidvi.graylog.template;

import ru.sidvi.graylog.extractors.DataExtractor;

import java.util.Map;

/**
 * Process template based on data from DataExtractor.
 *
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
public interface TemplateEngine {
    String processTemplate(Map<String, Object> value, String template);
}
