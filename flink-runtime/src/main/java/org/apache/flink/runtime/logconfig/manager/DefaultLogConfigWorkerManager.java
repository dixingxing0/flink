package org.apache.flink.runtime.logconfig.manager;

import org.apache.flink.runtime.logconfig.LogConfig;
import org.apache.flink.runtime.logconfig.LogConfigWorker;
import org.apache.flink.runtime.logconfig.LogConfigWorkerFactory;
import org.apache.flink.runtime.logconfig.LogConfigWorkerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class DefaultLogConfigWorkerManager implements LogConfigWorkerManager {
	private static final Logger logger = LoggerFactory.getLogger(DefaultLogConfigWorkerManager.class);

	private static ConcurrentHashMap<LogConfig, LogConfigWorker> workers = new ConcurrentHashMap<>();
	private LogConfigWorkerFactory logConfigWorkerFactory = new LogConfigWorkerFactory();

	@Override
	public void changeLevel(LogConfig logConfig) {
		LogConfigWorker last = workers.get(logConfig);
		if (last != null) {
			// remove last changer for this logger
			workers.remove(logConfig);
			last.shutdown();
		}
		LogConfigWorker worker = logConfigWorkerFactory.createWorker(logConfig);
		if (worker != null) {
			workers.put(logConfig, worker);
			worker.changeLevel();
		} else {
			//
		}
	}

	@Override
	public void cancel(LogConfig logConfig) {
		LogConfigWorker worker = workers.remove(logConfig);
		if (worker != null) {
			worker.cancel();
		}
	}
}
