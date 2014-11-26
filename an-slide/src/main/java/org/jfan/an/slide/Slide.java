/**
 * 
 */
package org.jfan.an.slide;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * ‘滑梯’接口对象<br>
 * 一次处理x个任务<br>
 * 
 * @author JFan - 2014年11月26日 下午3:31:40
 */
public interface Slide {

	/**
	 * 提交一个 Runnable 到‘滑梯’中执行
	 */
	public void submit(Runnable task);

	/**
	 * 提交一个 Callable 到‘滑梯’中执行，并返回一个Future
	 */
	public <T> Future<T> submit(Callable<T> task);

	/**
	 * 提交一个 SlideCallback 到‘滑梯’中执行
	 */
	public <T> void submit(SlideCallback<T> task);

}
