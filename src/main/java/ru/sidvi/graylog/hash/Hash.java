package ru.sidvi.graylog.hash;

/**
 *  Manage hash on strings.
 */
public interface Hash {
    String BEGIN_MARKER = "\nMARKER>>\n";
    String END_MARKER = "\n<<MARKER\n";

    String calc(String value);

    String append(String hash, String value);

    String extract(String value);

    String remove(String value);
}
