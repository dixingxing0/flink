package org.apache.flink.runtime.logconfig.manager;

import org.apache.flink.runtime.logconfig.LogConfigManager;
import org.apache.flink.runtime.logconfig.LogConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Default log config manager, maintain all the log config by a ArrayList.
 * <p>
 * For dynamic log config is not a routine operation, so we define a max size to prevent abuse.
 */
public class DefaultLogConfigManager implements LogConfigManager {

	private static final int DEFAULT_MAX = 10;
	private int max = DEFAULT_MAX;
	private List<LogConfig> logConfigList = new ArrayList<>();

	public DefaultLogConfigManager() {
	}

	public DefaultLogConfigManager(int max) {
		this.max = max;
	}

	@Override
	public void addLogConfig(LogConfig logConfig) {

	}

	@Override
	public List<LogConfig> listAllLogConfig() {
		return null;
	}

	@Override
	public void removeLogConfig(LogConfig logConfig) {

	}
}
