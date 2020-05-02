package org.apache.flink.runtime.logconfig.worker;

import org.apache.flink.runtime.logconfig.LogConfig;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;

public class LogbackConfigWorker extends AbstractLogConfigWorker {
	private org.apache.logging.log4j.Logger logger;

	public LogbackConfigWorker(LogConfig logConfig) {
		super(logConfig);
		if (isRootLogger()) {
			this.logger = LogManager.getRootLogger();
		} else {
			this.logger = LogManager.getLogger(logConfig.getLogger());
		}
	}

	@Override
	public String currentLogLevel(String loggerName) {
		return logger.getLevel().name();
	}

	@Override
	public void doChangeLogLevel(String loggerName, String logLevel) {
		Level level = Level.toLevel(logLevel);
		if (isRootLogger()) {
			Configurator.setRootLevel(level);
		} else {
			Configurator.setLevel(logConfig.getLogger(), level);
		}
	}
}
