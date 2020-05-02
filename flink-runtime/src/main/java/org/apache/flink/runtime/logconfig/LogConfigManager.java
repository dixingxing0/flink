package org.apache.flink.runtime.logconfig;

import java.util.List;

/**
 * Maintan all effective log config settings.
 */
public interface LogConfigManager {

	/**
	 * Add for new log config setting.
	 *
	 * @param logConfig
	 */
	void addLogConfig(LogConfig logConfig);

	/**
	 * List all effective log config settings.
	 *
	 * @return all effective log config settings.
	 */
	List<LogConfig> listAllLogConfig();

	/**
	 * Remove the specific log config setting.
	 *
	 * @param logConfig
	 */
	void removeLogConfig(LogConfig logConfig);
}
