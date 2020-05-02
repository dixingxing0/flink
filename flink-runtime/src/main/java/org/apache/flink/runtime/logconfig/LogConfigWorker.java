package org.apache.flink.runtime.logconfig;

/**
 * Change log level at runtime.
 */
public interface LogConfigWorker {

	/**
	 * Change the log level for a specific logger at runtime.
	 * <p>
	 * This will also start a new thread to schedule a log level revert setting.
	 */
	void changeLevel();

	/**
	 * Cancel log level setting, set the log level back to what it was before this worker running.
	 */
	void cancel();

	/**
	 * Simply shut down the scheduler.
	 */
	void shutdown();
}
