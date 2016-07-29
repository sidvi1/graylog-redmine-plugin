package ru.sidvi.graylog;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/**
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
public final class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);


    public static String fromResource(String file) {
        try {
            return load(file);
        } catch (IOException e) {
            logger.error("", e);
        }
        return "";
    }

    private static String load(String file) throws IOException {
        URL url = Resources.getResource(file);
        return Resources.toString(url, Charsets.UTF_8);
    }
}
