package ru.sidvi.graylog;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;

/**
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
public final class TestUtils {
    public static String fromResource(String file) throws IOException {
        URL url = Resources.getResource(file);
        return Resources.toString(url, Charsets.UTF_8);
    }
}
