/**
 * 
 */
package org.jfan.an.track;

import org.jfan.an.track.impl.timer.TimerTrackServiceImpl;

/**
 * TrackService工厂类<br>
 * 提供各种得到TrackService实例的方法<br>
 * 
 * @author JFan 2014年11月28日 下午3:02:07
 */
public final class TrackFactory {

	private TrackFactory() {
	}

	/**
	 * 构建一个由Timer实现的任务服务
	 */
	public static final TrackService newTimer() {
		return new TimerTrackServiceImpl();
	}

}
