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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */

@RunWith(value = MockitoJUnitRunner.class)
public class JMTEAdapterTest {

    @InjectMocks
    private JMTEAdapter adapter;

    @Mock
    private DataExtractor extractor;

    @Test
    public void shouldProcess() {
        Map<String, Object> result = new HashMap<>();
        result.put("stream_url", "URL");

        when(extractor.extract()).thenReturn(result);

        String template = "Stream url is ${stream_url}.";
        String actual = adapter.processTemplate(extractor, template);

        assertEquals("Stream url is URL.", actual);
        verify(extractor, times(1)).extract();
    }


}
