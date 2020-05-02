package org.apache.flink.runtime.logconfig;

/**
 * Core interface for log configuration.
 * JobManager and TaskExecutor will call this class directly.
 * Also act as a LogCongfigWorker repository.
 */
public interface LogConfigWorkerManager {

	/**
	 * Change the log level for a specific logger at runtime.
	 */
	void changeLevel(LogConfig logConfig);

	/**
	 * Cancel log level setting, set the log level back to what it was before this worker running.
	 */
	void cancel(LogConfig logConfig);
}
