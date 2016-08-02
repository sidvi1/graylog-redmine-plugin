package ru.sidvi.graylog.template;

import com.floreysoft.jmte.Engine;

import java.util.Map;

/**
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
public class JMTEAdapter implements TemplateEngine {

    @Override
    public String processTemplate(Map<String, Object> value, String template) {
        Engine engine = new Engine();
        return engine.transform(template, value);
    }
}
