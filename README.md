# Redmine Plugin for Graylog

[![Build Status](https://travis-ci.org/sidvi1/graylog-redmine-plugin.svg?branch=master)](https://travis-ci.org/sidvi1/graylog-redmine-plugin)
[![Coverage Status](https://coveralls.io/repos/github/sidvi1/graylog-redmine-plugin/badge.svg?branch=master)](https://coveralls.io/github/sidvi1/graylog-redmine-plugin?branch=master)


Graylog alarm callback plugin for Redmine issue tracker.

**Required Graylog version:** 2.0 and later

Installation
------------

[Download the plugin](https://github.com/sidvi1/graylog-redmine-plugin/releases)
and place the `.jar` file in your Graylog plugin directory. The plugin directory
is the `plugins/` folder relative from your `graylog-server` directory by default
and can be configured in your `graylog.conf` file.

Restart `graylog-server` and you are done.

Usage
-----
In the fields subject and description can use templates.
As a template engine used [jmte](https://code.google.com/archive/p/jmte/wikis/GettingStarted.wiki).

Example subject: 
```
Graylog alert for stream: ${stream.title}: ${check_result.resultDescription}
```

Example description:
```
##########
Alert Description: ${check_result.resultDescription}
Date: ${check_result.triggeredAt}
Stream ID: ${stream.id}
Stream title: ${stream.title}
Stream description: ${stream.description}
Alert Condition Title: ${alertCondition.title}
${if stream_url}Stream URL: ${stream_url}${end}

Triggered condition: ${check_result.triggeredCondition}
##########
${if backlog}
Last messages accounting for this alert:
${foreach backlog message}
${message}
${end}
${else}
<No backlog>
${end}
;
```


Available template parameters:
- stream
- backlog
- backlog_size
- alert_condition
- stream_url
- check_result

Screenshots
-----

![Graylog form](screenshot_form.png)
![Redmine](screenshot_redmine.png)


