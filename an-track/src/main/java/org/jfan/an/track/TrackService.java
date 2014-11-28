package org.jfan.an.track;

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
