package org.apache.flink.runtime.logconfig;

import org.apache.flink.runtime.logconfig.manager.Log4j2ConfigManager;
import org.apache.flink.runtime.logconfig.manager.Log4jConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * LogConfigManager factory.
 */
public class LogConfigManagerFactory {
	private static final Logger LOG = LoggerFactory.getLogger(LogConfigManagerFactory.class);

	private static volatile LoggingBackend loggingBackend;

	/**
	 * Enum of logging backend.
	 */
	public enum LoggingBackend {
		LOG4J(Log4jConfigManager.class),
		LOG4J2(Log4j2ConfigManager.class),
		UNKNOWN(null);
		private final Class<? extends LogConfigManager> managerClass;

		LoggingBackend(Class<? extends LogConfigManager> managerClass) {
			this.managerClass = managerClass;
		}
	}

	/**
	 * Create the LogConfigManager instance corresponding to the specific implementation:
	 * <ul>
	 * <li>Log4j2ConfigManager
	 * <li>Log4jConfigManager
	 * <li>LogbackConfigManager
	 * </ul>
	 *
	 * @return The manager instance.
	 */
	public LogConfigManager createManager() {
		LoggingBackend loggingBackend = getLoggingBackend();
		if (loggingBackend != null && loggingBackend.managerClass != null) {
			try {
				return loggingBackend.managerClass.getConstructor().newInstance();
			} catch (Exception e) {
				LOG.error("Error while create LogConfigManager instance : " + loggingBackend.managerClass, e);
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
	public static String getLoggerFactoryClassStr() {
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
