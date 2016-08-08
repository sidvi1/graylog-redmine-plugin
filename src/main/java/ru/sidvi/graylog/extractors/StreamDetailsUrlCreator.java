package ru.sidvi.graylog.extractors;

import org.graylog2.plugin.Tools;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vitaly Sidorov mail@vitaly-sidorov.com
 */
class StreamDetailsUrlCreator {
    private final Logger logger = LoggerFactory.getLogger(StreamDetailsUrlCreator.class);

    private String baseUri;
    private String id;
    private int minutesAgo;
    private DateTime triggeredAt;

    public StreamDetailsUrlCreator(String baseUri, String streamId, int minutesAgo, DateTime triggeredAt) {
        this.baseUri = baseUri;
        this.id = streamId;
        this.minutesAgo = minutesAgo;
        this.triggeredAt = triggeredAt;
    }

    public String createUrl() {
        String alertStart = Tools.getISO8601String(triggeredAt.minusMinutes(minutesAgo));
        String alertEnd = Tools.getISO8601String(triggeredAt);

        String result1 = baseUri + "/streams/" + id + "/messages?rangetype=absolute&from=" + alertStart + "&to=" + alertEnd + "&q=*";

        logger.debug("Stream details uri is {}", result1);
        return result1;
    }
}
