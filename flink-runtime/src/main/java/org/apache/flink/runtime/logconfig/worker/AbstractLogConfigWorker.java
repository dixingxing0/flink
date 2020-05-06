package org.apache.flink.runtime.logconfig.worker;

import org.apache.flink.runtime.logconfig.LogConfig;
import org.apache.flink.runtime.logconfig.LogConfigWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class AbstractLogConfigWorker implements LogConfigWorker {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	protected String originLogLevel;
	protected LogConfig logConfig;
	protected ScheduledExecutorService scheduledExecutorService;

	public AbstractLogConfigWorker(LogConfig logConfig) {
		this.logConfig = logConfig;
	}

	/**
	 * Change the log level for a specific logger at runtime.
	 * <p>
	 * This will also start a new thread to schedule a log level revert setting, @see {@link #resetLogLevel()}.
	 */
	@Override
	public void changeLevel() {
		synchronized (AbstractLogConfigWorker.class) {
			LOG.info("Changing logger level : {} to {}", logConfig.getLogger(), logConfig.getTargetLevel());
			originLogLevel = currentLogLevel();
			doChangeLogLevel(logConfig.getTargetLevel());
			scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(r -> {
				String name = "log-level-change-" + System.currentTimeMillis();
				Thread t = new Thread(r, name);
				t.setDaemon(true);
				return t;
			});

			//
			scheduledExecutorService.scheduleWithFixedDelay(this::resetLogLevel, 0, this.logConfig.getTimeout(), TimeUnit.SECONDS);
		}
	}

	/**
	 * return current log level
	 */
	public abstract String currentLogLevel();

	/**
	 * Change the log level for a specific logger at runtime.
	 */
	public abstract void doChangeLogLevel(String logLevel);

	protected void resetLogLevel() {
		LOG.info("Reseting logger level : {} to {}", logConfig.getLogger(), logConfig.getResetLevel());
		doChangeLogLevel(logConfig.getResetLevel());
		scheduledExecutorService.shutdown();
	}

	@Override
	public void cancel() {
		if (originLogLevel == null) {
			LOG.info("Origin log level is null, ignore.");
		} else {
			LOG.info("Cancelling for logger {}, reseting log level to origin level : {}", logConfig.getLogger(), originLogLevel);
			doChangeLogLevel(originLogLevel);
			scheduledExecutorService.shutdown();
		}
	}

	@Override
	public void shutdown() {
		scheduledExecutorService.shutdown();
	}

	public boolean isRootLogger() {
		return "ROOT".equalsIgnoreCase(logConfig.getLogger());
	}

}
