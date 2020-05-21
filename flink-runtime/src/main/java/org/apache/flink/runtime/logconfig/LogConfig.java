package org.apache.flink.runtime.logconfig;

import java.io.Serializable;

/**
 * Payload for dynamic log level settings sent to the ResourceManager.
 */
public class LogConfig implements Serializable {
	private String logger;
	private String targetLevel;

	public String getLogger() {
		return logger;
	}

	public void setLogger(String logger) {
		this.logger = logger;
	}

	public String getTargetLevel() {
		return targetLevel;
	}

	public void setTargetLevel(String targetLevel) {
		this.targetLevel = targetLevel;
	}
}
