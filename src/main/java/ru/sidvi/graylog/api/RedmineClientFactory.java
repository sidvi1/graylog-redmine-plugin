package ru.sidvi.graylog.api;


/**
 * Creates client from parameters.
 */
public class RedmineClientFactory {

    public RedmineClient create(String serverUrl, String apiKey) {
        return new RestApiClient(serverUrl, apiKey);
    }
}
