package ru.sidvi.graylog;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
public final class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);


    public static String fromResource(String file) {
        try {
            return load(file);
        } catch (IOException e) {
            logger.error("Exception while load resource from file {}", file, e);
        }
        return "";
    }

    private static String load(String file) throws IOException {
        return IOUtils.toString(
                Utils.class.getClassLoader().getResourceAsStream(file),
                "UTF-8"
        );
    }
}
