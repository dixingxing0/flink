package org.apache.flink.runtime.logconfig.worker;

import org.apache.flink.runtime.logconfig.LogConfig;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class Log4j2ConfigWorker extends AbstractLogConfigWorker {
	private org.apache.logging.log4j.Logger logger;

	public Log4j2ConfigWorker(LogConfig logConfig) {
		super(logConfig);
		if (isRootLogger()) {
			this.logger = LogManager.getRootLogger();
		} else {
			this.logger = LogManager.getLogger(logConfig.getLogger());
		}
	}

	@Override
	public String currentLogLevel() {
		return logger.getLevel().name();
	}

	@Override
	public void doChangeLogLevel(String logLevel) {
		Level level = Level.toLevel(logLevel);
		String loggerName = logConfig.getLogger();
		if (isRootLogger()) {
			loggerName = LogManager.ROOT_LOGGER_NAME;
		}
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration config = ctx.getConfiguration();
		LoggerConfig loggerConfig = config.getLoggerConfig(loggerName);
		loggerConfig.setLevel(level);
		ctx.updateLoggers();  // This causes all Loggers to refetch information from their LoggerConfig.
	}
}
