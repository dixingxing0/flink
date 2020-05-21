package org.apache.flink.runtime.rest.messages.logconfig;

import org.apache.flink.runtime.rest.messages.RequestBody;

public class LogConfigRequestBody implements RequestBody {
	private String logger;
	private String targetLevel;
}
