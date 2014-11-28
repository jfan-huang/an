package org.jfan.an.track;

/**
 * 定时任务Service <br>
 * <br>
 * 
 * @author JFan 2014年11月28日 下午3:02:29
 */
public interface TrackService {

	/**
	 * 放置一个任务
	 */
	public void placeTrack(Track track);

	/**
	 * 放置一个任务，如果时间过期，是否立即执行
	 */
	public void placeTrack(Track track, boolean pasc);

}
