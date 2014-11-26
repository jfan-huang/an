package org.jfan.an.log;

/**
 * * The org.slf4j.Logger interface is the main user entry point of SLF4J API.
 * It is expected that logging takes place through concrete implementations of
 * this interface.
 *
 * <h3>Typical usage pattern:</h3>
 * 
 * <pre>
 * import org.slf4j.Logger;
 * import org.slf4j.LoggerFactory;
 * 
 * public class Wombat {
 * 
 *   <span style="color:green">final static Logger logger = LoggerFactory.getLogger(Wombat.class);</span>
 *   Integer t;
 *   Integer oldT;
 * 
 *   public void setTemperature(Integer temperature) {
 *     oldT = t;        
 *     t = temperature;
 *     <span style="color:green">logger.debug("Temperature set to {}. Old temperature was {}.", t, oldT);</span>
 *     if(temperature.intValue() > 50) {
 *       <span style="color:green">logger.info("Temperature has risen above 50 degrees.");</span>
 *     }
 *   }
 * }
 * </pre>
 * 
 * @author JFan - 2014年10月30日 上午10:30:47
 */
public interface Logger {

	/**
	 * Is the logger instance enabled for the TRACE level?
	 * 
	 * @return True if this Logger is enabled for the TRACE level, false
	 *         otherwise.
	 * 
	 * @since 1.4
	 */
	public boolean isTraceEnabled();

	/**
	 * Log a message at the TRACE level according to the specified format and
	 * arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled
	 * for the TRACE level.
	 * </p>
	 *
	 * @param format the format string
	 * @param argArray an array of arguments
	 * 
	 * @since 1.4
	 */
	public void trace(String format, Object... argArray);

	/**
	 * Is the logger instance enabled for the DEBUG level?
	 * 
	 * @return True if this Logger is enabled for the DEBUG level, false
	 *         otherwise.
	 * 
	 */
	public boolean isDebugEnabled();

	/**
	 * Log a message at the DEBUG level according to the specified format and
	 * arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled
	 * for the DEBUG level.
	 * </p>
	 *
	 * @param format the format string
	 * @param argArray an array of arguments
	 */
	public void debug(String format, Object... argArray);

	/**
	 * Is the logger instance enabled for the INFO level?
	 * 
	 * @return True if this Logger is enabled for the INFO level, false
	 *         otherwise.
	 */
	public boolean isInfoEnabled();

	/**
	 * Log a message at the INFO level according to the specified format and
	 * arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled
	 * for the INFO level.
	 * </p>
	 *
	 * @param format the format string
	 * @param argArray an array of arguments
	 */
	public void info(String format, Object... argArray);

	/**
	 * Is the logger instance enabled for the WARN level?
	 * 
	 * @return True if this Logger is enabled for the WARN level, false
	 *         otherwise.
	 */
	public boolean isWarnEnabled();

	/**
	 * Log a message at the WARN level according to the specified format and
	 * arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled
	 * for the WARN level.
	 * </p>
	 *
	 * @param format the format string
	 * @param argArray an array of arguments
	 */
	public void warn(String format, Object... argArray);

	/**
	 * Is the logger instance enabled for the ERROR level?
	 * 
	 * @return True if this Logger is enabled for the ERROR level, false
	 *         otherwise.
	 */
	public boolean isErrorEnabled();

	/**
	 * Log a message at the ERROR level according to the specified format and
	 * arguments.
	 * 
	 * <p>
	 * This form avoids superfluous object creation when the logger is disabled
	 * for the ERROR level.
	 * </p>
	 *
	 * @param format the format string
	 * @param argArray an array of arguments
	 */
	public void error(String format, Object... argArray);

}
