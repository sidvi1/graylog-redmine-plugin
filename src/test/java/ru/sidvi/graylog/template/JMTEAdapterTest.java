package ru.sidvi.graylog.template;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.sidvi.graylog.extractors.DataExtractor;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Vitaly Sidorov mail@vitaly-sidorov.com
 */

@RunWith(value = MockitoJUnitRunner.class)
public class JMTEAdapterTest {

    @InjectMocks
    private JMTEAdapter adapter;

    @Test
    public void shouldProcess() {
        Map<String, Object> result = new HashMap<>();
        result.put("stream_url", "URL");


        String template = "Stream url is ${stream_url}.";
        String actual = adapter.processTemplate(result, template);

        assertEquals("Stream url is URL.", actual);
    }
}
