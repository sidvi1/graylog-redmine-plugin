package ru.sidvi.graylog.template;

import ru.sidvi.graylog.extractors.DataExtractor;

/**
 * Process template based on data from DataExtractor.
 *
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
public interface TemplateEngine {
    String processTemplate(DataExtractor extractor, String template);
}
