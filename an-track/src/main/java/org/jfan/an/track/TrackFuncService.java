package org.jfan.an.track;

public interface TrackFuncService {

	/**
	 * 放置一个任务
	 */
	public void placeTrack(TrackFunc track);

	/**
	 * 放置一个任务，如果时间过期，是否立即执行
	 */
	public void placeTrack(TrackFunc track, boolean pasc);

}
