package ru.sidvi.graylog.extractors;

import org.graylog2.plugin.Tools;
import org.graylog2.plugin.alarms.AlertCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vitaly Sidorov mail@vitaly-sidorov.com
 */
public class StreamDetailsUrlCreator {
    private static final int DEFAULT_TIME_COUNT = 5;
    private final Logger logger = LoggerFactory.getLogger(StreamDetailsUrlCreator.class);

    private String baseUri;
    private String id;
    private AlertCondition.CheckResult result;

    public StreamDetailsUrlCreator(String baseUri, String id, AlertCondition.CheckResult result) {
        this.baseUri = baseUri;
        this.id = id;
        this.result = result;
    }

    public String createUrl() {
        return buildStreamDetailsURL();
    }

    private String buildStreamDetailsURL() {

        int time = calculateTime(result.getTriggeredCondition().getParameters().get("time"));
        String alertStart = Tools.getISO8601String(result.getTriggeredAt().minusMinutes(time));
        String alertEnd = Tools.getISO8601String(result.getTriggeredAt());

        String result = baseUri + "/streams/" + id + "/messages?rangetype=absolute&from=" + alertStart + "&to=" + alertEnd + "&q=*";

        logger.debug("Stream details uri is {}", result);
        return result;
    }

    private int calculateTime(Object timeParam) {
        Integer result = DEFAULT_TIME_COUNT;
        if (timeParam != null) {
            result = (int) timeParam;
        }
        logger.debug("Time count to retrive messages is {}", result);
        return result;
    }
}
