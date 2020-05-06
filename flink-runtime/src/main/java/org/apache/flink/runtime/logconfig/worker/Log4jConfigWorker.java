package org.apache.flink.runtime.logconfig.worker;

import org.apache.flink.runtime.logconfig.LogConfig;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Log4jConfigWorker extends AbstractLogConfigWorker {
	private org.apache.log4j.Logger logger;

	public Log4jConfigWorker(LogConfig logConfig) {
		super(logConfig);
		if (isRootLogger()) {
			this.logger = Logger.getRootLogger();
		} else {
			this.logger = Logger.getLogger(logConfig.getLogger());
		}
	}

	@Override
	public String currentLogLevel() {
		return logger.getLevel().toString();
	}

	@Override
	public void doChangeLogLevel(String logLevel) {
		Level level = Level.toLevel(logLevel);
		logger.setLevel(level);
	}
}
