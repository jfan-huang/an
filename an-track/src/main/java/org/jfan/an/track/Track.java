/**
 * 
 */
package org.jfan.an.track;

/**
 * 任务描述 <br>
 * <br>
 * 
 * @author JFan - 2014年10月30日 下午3:50:01
 */
public interface Track {

	/**
	 * 未来运行的一个时间值（毫秒），指示什么时候执行
	 */
	public long timeMillis();

	/**
	 * 如果放置的任务运行时间，已经过期，这里决定是否立即执行（false：抛弃任务）
	 */
	public boolean pasc();

	/**
	 * 任务主体
	 */
	public void onRun();

	/**
	 * 任务主体--发生异常时
	 */
	public void onError(Exception e);

}
