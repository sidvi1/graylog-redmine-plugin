package ru.sidvi.graylog;

import org.graylog2.plugin.PluginModule;
import ru.sidvi.graylog.api.RedmineClientFactory;
import ru.sidvi.graylog.marker.DescriptionUniqMarker;
import ru.sidvi.graylog.marker.hash.Hash;
import ru.sidvi.graylog.marker.hash.MD5Hash;
import ru.sidvi.graylog.template.IssueTemplater;
import ru.sidvi.graylog.template.TemplateEngineAdapter;


/**
 * @author Vitaly Sidorov <mail@vitaly-sidorov.com>
 */
public class RedmineModule extends PluginModule {

    @Override
    protected void configure() {
        bind(RedmineClientFactory.class);
        bind(DescriptionUniqMarker.class);
        bind(IssueTemplater.class);
        bind(TemplateEngineAdapter.class);
        bind(Redmine.class);
        bind(Hash.class).to(MD5Hash.class);

        addAlarmCallback(RedmineAlarmCallback.class);
    }
}
