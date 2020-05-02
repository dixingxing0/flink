package org.apache.flink.runtime.logconfig;

import java.io.Serializable;

/**
 * Payload for dynamic log level settings sent to the ResourceManager.
 */
public class LogConfig implements Serializable {
	private String instanceType;
	private String taskManagerId;
	private String logger;
	private String targetLevel;
	private int timeout;
	private String resetLevel;
	private long startTime;

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public String getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}

	public String getTaskManagerId() {
		return taskManagerId;
	}

	public void setTaskManagerId(String taskManagerId) {
		this.taskManagerId = taskManagerId;
	}

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

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getResetLevel() {
		return resetLevel;
	}

	public void setResetLevel(String resetLevel) {
		this.resetLevel = resetLevel;
	}
}
