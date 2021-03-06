package ru.sidvi.graylog;

import org.graylog2.plugin.PluginModule;
import ru.sidvi.graylog.api.RedmineClientFactory;
import ru.sidvi.graylog.marker.DescriptionUniqMarker;
import ru.sidvi.graylog.marker.UniqIssueMarker;
import ru.sidvi.graylog.marker.hash.Hash;
import ru.sidvi.graylog.marker.hash.MD5Hash;
import ru.sidvi.graylog.template.JMTEAdapter;
import ru.sidvi.graylog.template.TemplateEngine;


/**
 * @author Vitaly Sidorov mail@vitaly-sidorov.com
 */
public class RedmineModule extends PluginModule {

    @Override
    protected void configure() {
        bind(RedmineClientFactory.class);
        bind(UniqIssueMarker.class).to(DescriptionUniqMarker.class);
        bind(TemplateEngine.class).to(JMTEAdapter.class);
        bind(Redmine.class);
        bind(Hash.class).to(MD5Hash.class);

        addAlarmCallback(RedmineAlarmCallback.class);
    }
}
