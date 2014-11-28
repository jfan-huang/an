/**
 * 
 */
package org.jfan.an.slide;

/**
 * ‘滑梯’任务接口<br>
 * 具备在发生错误以及任务结束时，给予一个回调方法<br>
 * 
 * TODO 暂未实现支持
 * 
 * @author JFan - 2014年11月26日 下午3:37:06
 */
public interface SlideCallbackNow<T> extends SlideCallback<T> {

	/**
	 * 当对象添加到‘滑梯’时，给予一个相应，是否在执行了<br>
	 * 次方法需要返回一个bool值，指示，是否继续任务<br>
	 * 
	 * answer：true，忽略方法返回值，继续执行任务<br>
	 * answer：false，方法返回真值则继续等待任务执行，返回假值则放弃任务<br>
	 */
	public boolean nowAnswer(boolean answer);

}
