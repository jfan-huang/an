/**
 * 
 */
package org.jfan.an.track.basic;

import org.jfan.an.track.TrackLoop;

/**
 * TrackLoop抽象类，屏蔽一些不常用到的方法withFixedDelay() <br>
 * <br>
 * 
 * @author JFan 2014年12月1日 下午4:57:44
 */
public abstract class LoopTrack extends OnlyTrack implements TrackLoop {

	private long interval;

	public LoopTrack(long interval) {
		this.interval = interval;
	}

	public LoopTrack(long howLong, long interval) {
		super(howLong);
		this.interval = interval;
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public long intervalMillis() {
		return interval;
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public boolean withFixedDelay() {
		return false;
	}

}
