package org.apache.flink.runtime.logconfig.manager;

import org.apache.flink.runtime.logconfig.LogConfig;
import org.apache.flink.runtime.logconfig.LogConfigManager;
import org.apache.flink.runtime.rest.messages.logconfig.LogConfigResponseBody;
import org.apache.flink.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLogConfigManager implements LogConfigManager {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	public abstract void doChangeLogLevel(LogConfig logConfig, String logLevel);


	public abstract String getLogLevel(LogConfig logConfig);

	@Override
	public LogConfigResponseBody changeLogLevel(LogConfig logConfig) {
		String currentLevel = getLogLevel(logConfig);
		LOG.info("Changing log level for logger {}: from {} to {}",
			logConfig.getLogger(), currentLevel, logConfig.getTargetLevel());
		LogConfigResponseBody response = new LogConfigResponseBody();
		try {
			doChangeLogLevel(logConfig, logConfig.getTargetLevel());
			response.setStatus(LogConfigResponseBody.STATUS_SUCCESS);
			response.setMessage("");
		} catch (Throwable e) {
			response.setStatus(LogConfigResponseBody.STATUS_ERROR);
			response.setMessage("Error occured : " + e.getMessage() + " : " + ExceptionUtils.stringifyException(e));
			LOG.error("Error while changing log level : {}", e.getMessage(), e);
		}
		return response;
	}
}
