package org.apache.flink.runtime.logconfig;

import java.util.Collection;

/**
 * Maintain all effective log config settings.
 */
public interface LogConfigManager {
	/**
	 * List all effective log config settings.
	 *
	 * @return all effective log config settings.
	 */
	Collection<LogConfig> listAllLogConfig();

	/**
	 * Change the log level for a specific logger at runtime.
	 */
	void changeLevel(LogConfig logConfig);

	/**
	 * Cancel log level setting.
	 */
	void clear(LogConfig logConfig);

	/**
	 * Cancel all the log level settings.
	 */
	void clearAll();

	/**
	 * Shutdown the scheduledExecutorService.
	 */
	void shutdown();
}
