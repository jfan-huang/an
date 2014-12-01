/**
 * 
 */
package org.jfan.an.track;

/**
 * 任务描述 <br>
 * 支持定时循环执行<br>
 * <br>
 * 如果pasc()==false并且首次任务已经过期了，则循环从添加任务开始计算<br>
 * 
 * @author JFan - 2014年10月30日 下午3:50:01
 */
public interface TrackLoop extends Track {

	/**
	 * 每次任务的间隔时间（毫秒）
	 */
	public long intervalMillis();

	/**
	 * 返回一个bool，指示相对延迟，还是绝对延迟<br>
	 * <br>
	 * true：绝对于上次任务发起之后<br>
	 * false：相对于上一次任务完成时间<br>
	 */
	public boolean withFixedDelay();

}
