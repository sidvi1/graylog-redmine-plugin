package ru.sidvi.graylog.template;

import com.floreysoft.jmte.Engine;
import ru.sidvi.graylog.DataExtractor;

import java.util.Map;

public class TemplateEngineAdapter {

    public String processTemplate(DataExtractor extractor, String template) {
        Map<String, Object> model = extractor.extract();
        Engine engine = new Engine();
        return engine.transform(template, model);
    }
}
