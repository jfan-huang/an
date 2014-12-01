/**
 * 
 */
package org.jfan.an.track.basic;

import org.jfan.an.track.Track;

/**
 * Track接口的抽象实现，主要是屏蔽一些不常设置的方法，例如 pasc()、onError() <br>
 * <br>
 * 
 * @author JFan 2014年12月1日 下午4:45:33
 */
public abstract class OnlyTrack implements Track {

	private long howLong;

	public OnlyTrack() {
	}

	public OnlyTrack(long howLong) {
		this.howLong = howLong;
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public long timeMillis() {
		return System.currentTimeMillis() + howLong;
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public boolean pasc() {
		return true;
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public void onError(Exception e) {
		// ignore
		// logger
	}

}
