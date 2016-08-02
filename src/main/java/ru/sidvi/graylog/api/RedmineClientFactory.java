package ru.sidvi.graylog.api;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates client from parameters.
 *
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
public class RedmineClientFactory {
    private final Logger logger = LoggerFactory.getLogger(RedmineClientFactory.class);

    public RedmineClient create(String serverUrl, String apiKey) {
        RestApiClient client = new RestApiClient(serverUrl, apiKey);
        logger.debug("Redmine client for url {} and api key {} created", serverUrl, apiKey);
        return client;
    }
}
