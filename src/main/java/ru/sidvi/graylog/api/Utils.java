package ru.sidvi.graylog.api;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    public static final String MD5_PATTERN = ">>MD5:";
    static final Logger logger = LoggerFactory.getLogger(IssueDTO.class);
    public static final int MD5_LEN = 32;

    public static String extractMD5(String description) {
        if (description.contains(MD5_PATTERN)) {
            int i = description.indexOf(MD5_PATTERN);
            return description.substring(i + MD5_PATTERN.length(), description.length()).substring(0, MD5_LEN);
        }
        return description;
    }

    static String calculateMD5(String value) {
        String md5 = "";

        byte[] content = value.getBytes();

        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(content, 0, content.length);
            md5 = new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            logger.warn("Error in MD5 generation (MD5-string=" + md5 + "): " + e.getMessage());
        }
        return md5;
    }

    public static String appendMD5(String md5, String value) {
        return value + "\n" + MD5_PATTERN + md5 + "\n";
    }

    static String fromFile(String file) throws IOException {
        return IOUtils.toString(
                Utils.class.getClassLoader().getResourceAsStream(file),
                "UTF-8"
        );
    }
}
