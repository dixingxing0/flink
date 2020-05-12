package org.apache.flink.runtime.logconfig.manager;

import org.apache.flink.runtime.logconfig.LogConfig;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class Log4j2ConfigManager extends AbstractLogConfigManager {

	@Override
	public String getLogLevel(LogConfig logConfig) {
		return getLogger(logConfig).getLevel().name();
	}

	@Override
	public void doChangeLogLevel(LogConfig logConfig, String logLevel) {
		Level level = Level.toLevel(logLevel);
		String loggerName = logConfig.getLogger();
		if (isRootLogger(logConfig)) {
			loggerName = LogManager.ROOT_LOGGER_NAME;
		}
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration config = ctx.getConfiguration();
		LoggerConfig loggerConfig = config.getLoggerConfig(loggerName);
		loggerConfig.setLevel(level);
		ctx.updateLoggers();  // This causes all Loggers to refetch information from their LoggerConfig.
	}

	public Logger getLogger(LogConfig logConfig) {
		if (isRootLogger(logConfig)) {
			return LogManager.getRootLogger();
		} else {
			return LogManager.getLogger(logConfig.getLogger());
		}
	}

	public boolean isRootLogger(LogConfig logConfig) {
		return "ROOT".equalsIgnoreCase(logConfig.getLogger())
			|| LogManager.ROOT_LOGGER_NAME.equalsIgnoreCase(logConfig.getLogger());
	}
}
