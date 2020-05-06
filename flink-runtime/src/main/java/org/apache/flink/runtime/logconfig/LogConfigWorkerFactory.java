package org.apache.flink.runtime.logconfig;

import org.apache.flink.runtime.logconfig.worker.Log4j2ConfigWorker;
import org.apache.flink.runtime.logconfig.worker.Log4jConfigWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * LogConfigWorker factory.
 */
public class LogConfigWorkerFactory {
	private static final Logger LOG = LoggerFactory.getLogger(LogConfigWorkerFactory.class);

	private static volatile LoggingBackend loggingBackend;

	/**
	 * Enum of logging backend.
	 */
	public enum LoggingBackend {
		LOG4J(Log4jConfigWorker.class),
		LOG4J2(Log4j2ConfigWorker.class),
		UNKNOWN(null);
		private final Class<? extends LogConfigWorker> workerClass;

		LoggingBackend(Class<? extends LogConfigWorker> workerClass) {
			this.workerClass = workerClass;
		}
	}

	/**
	 * Create the LogConfigWorker instance corresponding to the specific implementation:
	 * <ul>
	 * <li>Log4j2ConfigWorker
	 * <li>Log4jConfigWorker
	 * <li>LogbackConfigWorker
	 * </ul>
	 *
	 * @param logConfig The config info, contains the REST request payload.
	 * @return The worker instance.
	 */
	public LogConfigWorker createWorker(LogConfig logConfig) {
		LoggingBackend loggingBackend = getLoggingBackend();
		if (loggingBackend != null && loggingBackend.workerClass != null) {
			try {
				return loggingBackend.workerClass.getConstructor(LogConfig.class).newInstance(logConfig);
			} catch (Exception e) {
				LOG.error("Error while create LogConfigWorker instance : " + loggingBackend.workerClass, e);
			}
		}
		return null;
	}

	/**
	 * Get slf4j logging backend.
	 */
	public static LoggingBackend getLoggingBackend() {
		if (loggingBackend == null) {
			synchronized (LoggingBackend.class) {
				if (loggingBackend == null) {
					loggingBackend = getLoggingBackendInternal();
				}
			}
		}
		return loggingBackend;
	}

	protected static LoggingBackend getLoggingBackendInternal() {
		String loggerFactoryClassStr = getLoggerFactoryClassStr();
		if (loggerFactoryClassStr == null) {
			return LoggingBackend.UNKNOWN;
		}
		// both log4j1x and log4j2x use this factory.
		if ("org.apache.logging.slf4j.Log4jLoggerFactory".equalsIgnoreCase(loggerFactoryClassStr)) {
			if (hasLog4j2x()) {
				return LoggingBackend.LOG4J2;
			} else if (hasLog4j1x()) {
				return LoggingBackend.LOG4J;
			} else {
				LOG.warn("Both log4j1x and log4j2x are not deteced.");
				return LoggingBackend.UNKNOWN;
			}
		}
		// TODO logback: ch.qos.logback.classic.util.ContextSelectorStaticBinder
		else {
			LOG.warn("Unsupported logging backend : {}", loggerFactoryClassStr);
			return LoggingBackend.UNKNOWN;
		}
	}

	protected static boolean hasLog4j2x() {
		// log4j2x logger class
		String log4j2LoggerClassStr = "org.apache.logging.log4j.core.Logger";
		try {
			Class<?> log4j2LoggerClass = Class.forName(log4j2LoggerClassStr);
			String log4jVersion = log4j2LoggerClass.getPackage().getSpecificationVersion();
			if (log4jVersion != null && log4jVersion.startsWith("2")) {
				return true;
			}
		} catch (ClassNotFoundException e) {
			LOG.info("Class not found : {}", log4j2LoggerClassStr);
		}
		return false;
	}

	protected static boolean hasLog4j1x() {
		// log4j1x logger class
		String log4j1LoggerClassStr = "org.apache.log4j.Logger";
		try {
			Class.forName(log4j1LoggerClassStr);
			return true;
		} catch (ClassNotFoundException e2) {
			LOG.warn("Class not found : {}", log4j1LoggerClassStr);
		}
		return false;
	}

	/**
	 * @return logger factory class name.
	 * @see <a href="http://www.slf4j.org/codes.html#StaticLoggerBinder">StaticLoggerBinder</a>
	 */
	protected static String getLoggerFactoryClassStr() {
		try {
			String binderClassStr = "org.slf4j.impl.StaticLoggerBinder";
			Class<?> binderClass = Class.forName(binderClassStr);
			Method getSingleton = binderClass.getDeclaredMethod("getSingleton");
			Object instance = getSingleton.invoke(null);
			Method getLoggerFactoryClassStr = binderClass.getDeclaredMethod("getLoggerFactoryClassStr");
			return (String) getLoggerFactoryClassStr.invoke(instance);
		} catch (Throwable e) {
			LOG.error("Failed to get slf4j LoggerFactoryClass.", e);
			return null;
		}
	}
}
