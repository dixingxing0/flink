package org.apache.flink.runtime.logconfig.manager;

import org.apache.flink.runtime.logconfig.LogConfig;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Log4jConfigManager extends AbstractLogConfigManager {

	@Override
	public String getLogLevel(LogConfig logConfig) {
		Logger logger = getLogger(logConfig);
		return logger.getLevel().toString();
	}

	private Logger getLogger(LogConfig logConfig) {
		Logger logger;
		if (isRootLogger(logConfig)) {
			logger = Logger.getRootLogger();
		} else {
			logger = Logger.getLogger(logConfig.getLogger());
		}
		return logger;
	}

	@Override
	public void doChangeLogLevel(LogConfig logConfig, String logLevel) {
		Level level = Level.toLevel(logLevel);
		Logger logger = getLogger(logConfig);
		logger.setLevel(level);
	}

	public boolean isRootLogger(LogConfig logConfig) {
		return "ROOT".equalsIgnoreCase(logConfig.getLogger());
	}
}
