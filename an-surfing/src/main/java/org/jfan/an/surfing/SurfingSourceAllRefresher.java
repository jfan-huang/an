/**
 * 
 */
package org.jfan.an.surfing;

/**
 * 数据回源<br>
 * 具备预载全部内容 的能力<br>
 * 具备定时重新装载全部内容的能力<br>
 * 
 * @author JFan - 2014年10月29日 上午11:18:07
 */
public interface SurfingSourceAllRefresher<T> extends SurfingSourceAll<T> {

	/**
	 * 定时重载全部数据的间隔
	 */
	public abstract long intervalMilliseconds();

	/**
	 * 返回一个bool，表示是否绝对地延迟<br>
	 * <br>
	 * 
	 * true: 上一次任务结束开始，才开始计算延迟时间 <br>
	 * false：发起上一次任务，立即计算延迟时间
	 */
	public boolean withFixedDelay();

}
