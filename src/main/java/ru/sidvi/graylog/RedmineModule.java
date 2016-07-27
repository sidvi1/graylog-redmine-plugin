package ru.sidvi.graylog;

import org.graylog2.plugin.PluginModule;
import ru.sidvi.graylog.api.RedmineClientFactory;
import ru.sidvi.graylog.hash.Hash;
import ru.sidvi.graylog.hash.IssueMarker;
import ru.sidvi.graylog.hash.MD5Hash;
import ru.sidvi.graylog.template.IssueTemplater;
import ru.sidvi.graylog.template.TemplateEngineAdapter;


public class RedmineModule extends PluginModule {

    @Override
    protected void configure() {
        bind(Hash.class).to(MD5Hash.class).asEagerSingleton();
        bind(RedmineClientFactory.class);
        bind(IssueMarker.class);
        bind(IssueTemplater.class);
        bind(TemplateEngineAdapter.class);
        bind(Redmine.class);

        addAlarmCallback(RedmineAlarmCallback.class);
    }
}
