package org.jfan.an.log;

import org.jfan.an.log.helpers.SysoutLogger;

/**
 * The <code>LoggerFactory</code> is a utility class producing Loggers for
 * various logging APIs, most notably for log4j, logback and JDK 1.4 logging.
 * Other implementations such as {@link org.slf4j.impl.NOPLogger NOPLogger} and
 * {@link org.slf4j.impl.SimpleLogger SimpleLogger} are also supported.
 * 
 * <p>
 * <code>LoggerFactory</code> is essentially a wrapper around an
 * {@link ILoggerFactory} instance bound with <code>LoggerFactory</code> at
 * compile time.
 * 
 * <p>
 * Please note that all methods in <code>LoggerFactory</code> are static. <br>
 * <br>
 * 
 * @author JFan - 2014年10月30日 上午10:33:42
 */
public final class LoggerFactory {

	private static SysoutLogger logger = new SysoutLogger();

	// private constructor prevents instantiation
	private LoggerFactory() {
	}

	/**
	 * Return a logger named corresponding to the class passed as parameter,
	 * using the statically bound {@link ILoggerFactory} instance.
	 * 
	 * @param clazz the returned logger will be named after clazz
	 * @return logger
	 */
	public static Logger getLogger(Class<?> clazz) {
		return getLogger(clazz.getName());
	}

	/**
	 * Return a logger named according to the name parameter using the
	 * statically bound {@link ILoggerFactory} instance.
	 * 
	 * @param name The name of the logger.
	 * @return logger
	 */
	public static Logger getLogger(String name) {
		// ILoggerFactory iLoggerFactory = getILoggerFactory();
		// return iLoggerFactory.getLogger(name);
		return logger;
	}

}
