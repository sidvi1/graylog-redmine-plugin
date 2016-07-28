package ru.sidvi.graylog.extractors;

import java.util.Map;

/**
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
public interface DataExtractor {
    Map<String, Object> extract();
}
