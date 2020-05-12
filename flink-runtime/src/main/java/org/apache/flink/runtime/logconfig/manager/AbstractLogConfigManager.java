package org.apache.flink.runtime.logconfig.manager;

import org.apache.flink.runtime.logconfig.LogConfig;
import org.apache.flink.runtime.logconfig.LogConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class AbstractLogConfigManager implements LogConfigManager {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	private static ConcurrentHashMap<String, LogConfig> allLogConfigs = new ConcurrentHashMap<>();
	protected ScheduledExecutorService scheduledExecutorService;

	public AbstractLogConfigManager() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(r -> {
			String name = "log-config";
			Thread t = new Thread(r, name);
			t.setDaemon(true);
			return t;
		});
		//
		scheduledExecutorService.scheduleWithFixedDelay(this::resetLogLevel, 0, 1, TimeUnit.SECONDS);
	}

	public abstract String getLogLevel(LogConfig logConfig);

	public abstract void doChangeLogLevel(LogConfig logConfig, String logLevel);

	@Override
	public void changeLevel(LogConfig logConfig) {
		LogConfig last = allLogConfigs.get(logConfig.getLogger());
		String originLevel;
		if (last != null) {
			originLevel = last.getOriginLevel();
			// remove last changer for this logger
		} else {
			originLevel = getLogLevel(logConfig);
		}
		logConfig.setOriginLevel(originLevel);
		allLogConfigs.put(logConfig.getLogger(), logConfig);
		doChangeLogLevel(logConfig, logConfig.getTargetLevel());
	}

	@Override
	public void clear(LogConfig logConfig) {
		LOG.info("Reseting logger level : {} to {}", logConfig.getLogger(), logConfig.getResetLevel());
		doChangeLogLevel(logConfig, logConfig.getOriginLevel());
		allLogConfigs.remove(logConfig.getLogger());
	}

	@Override
	public Collection<LogConfig> listAllLogConfig() {
		return allLogConfigs.values();
	}

	@Override
	public void clearAll() {
		for (LogConfig logConfig : allLogConfigs.values()) {
			LOG.info("Reseting logger level : {} to {}", logConfig.getLogger(), logConfig.getResetLevel());
			doChangeLogLevel(logConfig, logConfig.getOriginLevel());
		}
		allLogConfigs.clear();
	}

	@Override
	public void shutdown() {
		if (scheduledExecutorService != null) {
			scheduledExecutorService.shutdown();
		}
	}

	protected void resetLogLevel() {
		List<LogConfig> expires = new ArrayList<>();
		for (LogConfig logConfig : allLogConfigs.values()) {
			if (logConfig.expired()) {
				expires.add(logConfig);
			}
		}
		for (LogConfig logConfig : expires) {
			allLogConfigs.remove(logConfig.getLogger());
			LOG.info("Timeout reseting logger level : {} to {}", logConfig.getLogger(), logConfig.getResetLevel());
			doChangeLogLevel(logConfig, logConfig.getOriginLevel());
		}
	}
}
