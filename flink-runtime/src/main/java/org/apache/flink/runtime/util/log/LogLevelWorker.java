package org.apache.flink.runtime.util.log;

import org.apache.flink.util.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * The param format is :
 * type|loggerName|level|expireSecs|resetLevel
 * </pre>
 * <pre>
 * Example: params=jobManager|root|debug|30|info
 * This will change job manager's "root" log level to "debug", and after 30 seconds set it back to "info".
 * </br>
 * type : jobManager or taskManager
 * loggerName : the logger name org.apache.kafka
 * level : ALL, TRACE, DEBUG, INFO, WARN, ERROR, FATAL ,OFF
 * expireSecs : define a timer to set log level to the resetLevel after specific seconds
 * resetLevel : the log level to set back, default is 'info'
 * </pre>
 */
public class LogLevelWorker {
	private static final Logger LOG = LoggerFactory.getLogger(LogLevelWorker.class);

	private static final String TYPE_JM = "jobManager";
	private static final String TYPE_TM = "taskManager";
	private static final String ROOT_LOGGER = "root";
	private static ConcurrentHashMap<String, LogLevelWorker> workers = new ConcurrentHashMap<>();

	private String type;
	private org.apache.log4j.Logger logger;
	private Level level;
	private Level resetLevel = Level.INFO;
	private int expireSecs = 60;
	private ScheduledExecutorService scheduledExecutorService;

	public LogLevelWorker(String paramsString) {
		if (StringUtils.isNullOrWhitespaceOnly(paramsString)) {
			throw new IllegalArgumentException("Illegal log level params : " + paramsString + ", see LogLevelWorker.");
		}
		String[] params = paramsString.split("\\|");
		if (params.length < 3) {
			throw new IllegalArgumentException("Illegal log level params : " + paramsString + ", see LogLevelWorker.");
		}

		this.type = params[0];
		this.logger = params[1].isEmpty() || ROOT_LOGGER.equalsIgnoreCase(params[1])
			? LogManager.getRootLogger() : LogManager.getLogger(params[1]);
		this.level = Level.toLevel(params[2].toUpperCase());
		if (params.length > 3) {
			try {
				this.expireSecs = Integer.parseInt(params[3]);
			} catch (Exception e) {
				throw new IllegalArgumentException("Illegal log level params : " + paramsString + ", see LogLevelWorker.");
			}
		}
		if (params.length > 4) {
			this.resetLevel = Level.toLevel(params[4].toUpperCase());
		}
	}

	/**
	 * Get all effective workers.
	 *
	 * @return All workers which has not been expires.
	 */
	public static ConcurrentHashMap<String, LogLevelWorker> getWorkers() {
		return workers;
	}

	/**
	 * Change log level for log4j logger.
	 */
	public void changeLogLevel() {
		synchronized (LogLevelWorker.class) {
			LOG.info("Setting log level from {} to {} for logger {}.", logger.getLevel(), level, logger.getName());
			logger.setLevel(level);
			scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(r -> {
				String name = "log4j-level-worker-" + System.currentTimeMillis();
				Thread t = new Thread(r, name);
				t.setDaemon(true);
				return t;
			});

			LogLevelWorker last = workers.putIfAbsent(logger.getName(), this);
			if (last != null) {
				LOG.info("Remove last log level worker for logger : {}", last);
				if (last.scheduledExecutorService != null) {
					last.scheduledExecutorService.shutdown();
				}
			}

			scheduledExecutorService.scheduleWithFixedDelay(() -> {
				LOG.info("Reseting log level from {} to {} for logger {}.", logger.getLevel(), resetLevel, logger.getName());
				logger.setLevel(resetLevel);
				scheduledExecutorService.shutdown();
			}, this.expireSecs, this.expireSecs, TimeUnit.SECONDS);
		}
	}


	/**
	 * @return is for job manager.
	 */
	public boolean forJobManager() {
		return TYPE_JM.equalsIgnoreCase(this.getType());
	}

	/**
	 * @return is for task manager.
	 */
	public boolean forTaskManager() {
		return TYPE_TM.equalsIgnoreCase(this.getType());
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public org.apache.log4j.Logger getLogger() {
		return logger;
	}

	public void setLogger(org.apache.log4j.Logger logger) {
		this.logger = logger;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Level getResetLevel() {
		return resetLevel;
	}

	public void setResetLevel(Level resetLevel) {
		this.resetLevel = resetLevel;
	}

	public int getExpireSecs() {
		return expireSecs;
	}

	public void setExpireSecs(int expireSecs) {
		this.expireSecs = expireSecs;
	}

	@Override
	public String toString() {
		return "LogLevelWorker{" +
			"loggerName=" + logger.getName() +
			", level=" + level +
			", resetLevel=" + resetLevel +
			", expireSecs=" + expireSecs +
			'}';
	}

}
