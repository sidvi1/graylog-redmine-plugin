package ru.sidvi.graylog;

import org.graylog2.plugin.PluginModule;

public class RedmineModule extends PluginModule {

    @Override
    protected void configure() {
        addAlarmCallback(RedmineAlarmCallback.class);
    }
}
