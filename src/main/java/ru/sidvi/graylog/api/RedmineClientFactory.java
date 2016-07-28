package ru.sidvi.graylog.api;


/**
 * Creates client from parameters.
 *
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
public class RedmineClientFactory {

    public RedmineClient create(String serverUrl, String apiKey) {
        return new RestApiClient(serverUrl, apiKey);
    }
}
