package org.apache.flink.runtime.logconfig;

import org.apache.flink.runtime.rest.messages.logconfig.LogConfigResponseBody;

/**
 * Change log level for a specific logging backend, e.g. log4j2.
 */
public interface LogConfigManager {
	/**
	 * Change log level at runtime.
	 */
	LogConfigResponseBody changeLogLevel(LogConfig logConfig);
}
