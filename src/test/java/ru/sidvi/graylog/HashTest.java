package ru.sidvi.graylog;

import org.junit.Before;
import org.junit.Test;
import ru.sidvi.graylog.hash.Hash;
import ru.sidvi.graylog.hash.MD5Hash;

import static org.junit.Assert.assertEquals;

public class HashTest {

    private MD5Hash hash;

    @Before
    public void setUp() {
        hash = new MD5Hash();
    }

    @Test
    public void testExtraction() {
        String md5 = "5a105e8b9d40e1329780d62ea2265d8a";
        String actual = hash.extract("test1" + Hash.BEGIN_MARKER + md5 + Hash.END_MARKER );
        assertEquals(md5, actual);
    }

    @Test
    public void testRemoving() {
        String md5 = "5a105e8b9d40e1329780d62ea2265d8a";
        String actual = hash.remove("test1" + Hash.BEGIN_MARKER + md5 + Hash.END_MARKER );
        assertEquals("test1", actual);
    }

    @Test
    public void testAppend() {
        String md5 = "5a105e8b9d40e1329780d62ea2265d8a";
        String actual = hash.append(md5, "test1");
        assertEquals("test1" + Hash.BEGIN_MARKER + md5 + Hash.END_MARKER , actual);
    }

    @Test
    public void testCalculation() {
        String actual = hash.calc("test1");
        assertEquals("5a105e8b9d40e1329780d62ea2265d8a", actual);
    }
}
