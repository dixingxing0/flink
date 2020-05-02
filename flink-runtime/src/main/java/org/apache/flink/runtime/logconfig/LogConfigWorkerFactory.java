package org.apache.flink.runtime.logconfig;

import org.apache.flink.runtime.logconfig.worker.AbstractLogConfigWorker;

/**
 * Produce LogConfigWorker instances.
 */
public class LogConfigWorkerFactory {

	/**
	 * Create right LogConfigWorker instance by the log implementation:
	 * <p>
	 * Log4j2ConfigWorker or Log4jConfigWorker or LogbackConfigWorker
	 * </p>
	 *
	 * @param logConfig the whole config info, contains the REST request payload.
	 * @return the right worker instance.
	 */
	public AbstractLogConfigWorker createWorker(LogConfig logConfig) {
		return null;
	}
}
