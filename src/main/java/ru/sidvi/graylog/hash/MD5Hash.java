package ru.sidvi.graylog.hash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Singleton
public class MD5Hash implements Hash {

    private final Logger logger = LoggerFactory.getLogger(MD5Hash.class);


    @Override
    public String calc(String value) {
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

    @Override
    public String append(String hash, String value) {
        return value + BEGIN_MARKER + hash + END_MARKER;
    }

    @Override
    public String extract(String value) {
        if (value.contains(BEGIN_MARKER)) {
            int begin = value.indexOf(BEGIN_MARKER);
            int end = value.indexOf(END_MARKER);
            return value.substring(begin + BEGIN_MARKER.length(), end);
        }
        return value;
    }

    @Override
    public String remove(String value) {
        if (value.contains(BEGIN_MARKER)) {
            int begin = value.indexOf(BEGIN_MARKER);
            int end = value.indexOf(END_MARKER);
            return value.substring(0, begin) + value.substring(end + END_MARKER.length(), value.length());
        }
        return value;
    }
}
