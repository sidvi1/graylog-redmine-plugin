##########
Alert Description: ${check_result.resultDescription}
Date: ${check_result.triggeredAt}
Stream ID: ${stream.id}
Stream title: ${stream.title}
Stream description: ${stream.description}
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
