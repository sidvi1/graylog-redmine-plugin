package ru.sidvi.graylog;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

/**
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
public final class TestUtils {
    public static String fromResource(String file) throws IOException {
        return IOUtils.toString(
                TestUtils.class.getClassLoader().getResourceAsStream(file),
                "UTF-8"
        );
    }
}
