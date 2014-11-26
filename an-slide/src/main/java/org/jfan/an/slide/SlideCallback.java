/**
 * 
 */
package org.jfan.an.slide;

/**
 * ‘滑梯’任务接口<br>
 * 具备在发生错误以及任务结束时，给予一个回调方法<br>
 * 
 * @author JFan - 2014年11月26日 下午3:37:06
 */
public interface SlideCallback<T> {

	/**
	 * 处理任务方法
	 */
	public T call() throws Exception;

	/**
	 * 发生错误时，回调方法
	 */
	public void error(Exception ex);

	/**
	 * 处理任务 结束时，回调方法
	 */
	public void back(T value);

}
