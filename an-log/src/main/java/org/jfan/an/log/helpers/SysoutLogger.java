/**
 * 
 */
package org.jfan.an.log.helpers;

import java.io.PrintStream;
import java.util.Arrays;

import org.jfan.an.log.Logger;

/**
 * <br>
 * <br>
 * 
 * @author JFan - 2014年10月30日 上午10:38:52
 */
public class SysoutLogger implements Logger {

	// Trace:0 ~~ error:4
	public static Integer LEVEL = 0;

	private PrintStream out = System.out;
	private PrintStream err = System.err;

	private void out(String msg, Object... args) {
		int length = 0;
		Throwable t = null;
		if (null != args && 0 < (length = args.length) && args[length - 1] instanceof Throwable)
			t = (Throwable) args[length - 1];

		PrintStream ps = (null != t ? err : out);

		Object[] objects = (null == args || 0 >= length ? null : (null == t ? args : (1 == length ? null : Arrays.copyOf(args, length - 1))));
		if (null == objects || 0 >= objects.length)
			ps.println(msg);
		else {
			ps.printf(msg, objects);
			ps.print("\r\n");
		}

		if (null != t)
			t.printStackTrace(ps);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.log.Logger#isTraceEnabled()
	 */
	@Override
	public boolean isTraceEnabled() {
		return LEVEL <= 0;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.log.Logger#trace(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void trace(String format, Object... argArray) {
		if (isTraceEnabled())
			out(format, argArray);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.log.Logger#isDebugEnabled()
	 */
	@Override
	public boolean isDebugEnabled() {
		return LEVEL <= 1;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.log.Logger#debug(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void debug(String format, Object... argArray) {
		if (isDebugEnabled())
			out(format, argArray);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.log.Logger#isInfoEnabled()
	 */
	@Override
	public boolean isInfoEnabled() {
		return LEVEL <= 2;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.log.Logger#info(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void info(String format, Object... argArray) {
		if (isInfoEnabled())
			out(format, argArray);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.log.Logger#isWarnEnabled()
	 */
	@Override
	public boolean isWarnEnabled() {
		return LEVEL <= 3;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.log.Logger#warn(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void warn(String format, Object... argArray) {
		if (isWarnEnabled())
			out(format, argArray);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.log.Logger#isErrorEnabled()
	 */
	@Override
	public boolean isErrorEnabled() {
		return LEVEL <= 4;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.log.Logger#error(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void error(String format, Object... argArray) {
		if (isErrorEnabled())
			out(format, argArray);
	}

}
