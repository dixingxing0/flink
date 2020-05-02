package org.apache.flink.runtime.logconfig.manager;

import org.apache.flink.runtime.logconfig.LogConfig;
import org.apache.flink.runtime.logconfig.LogConfigWorkerFactory;
import org.apache.flink.runtime.logconfig.LogConfigWorkerManager;
import org.apache.flink.runtime.logconfig.worker.AbstractLogConfigWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class DefaultLogConfigWorkerManager implements LogConfigWorkerManager {
	private static final Logger logger = LoggerFactory.getLogger(DefaultLogConfigWorkerManager.class);

	private static ConcurrentHashMap<LogConfig, AbstractLogConfigWorker> workers = new ConcurrentHashMap<>();
	private LogConfigWorkerFactory logConfigWorkerFactory = new LogConfigWorkerFactory();

	@Override
	public void changeLevel(LogConfig logConfig) {
		AbstractLogConfigWorker last = workers.get(logConfig);
		if (last != null) {
			// remove last changer for this logger
			workers.remove(logConfig);
			last.shutdown();
		}
		// TODO Introduce LogConfigWorderFactory
		AbstractLogConfigWorker worker = logConfigWorkerFactory.createWorker(logConfig);
		workers.put(logConfig, worker);
		worker.changeLevel();
	}

	@Override
	public void cancel(LogConfig logConfig) {
		AbstractLogConfigWorker worker = workers.remove(logConfig);
		if (worker != null) {
			worker.cancel();
		}
	}
}
