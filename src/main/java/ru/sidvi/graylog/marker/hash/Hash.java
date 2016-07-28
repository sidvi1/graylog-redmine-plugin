package ru.sidvi.graylog.marker.hash;

/**
 * Manage hash on strings.
 *
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
public interface Hash {
    String BEGIN_MARKER = "\nMARKER>>\n";
    String END_MARKER = "\n<<MARKER\n";

    String calc(String value);

    String append(String hash, String value);

    String extract(String value);

    String remove(String value);
}
