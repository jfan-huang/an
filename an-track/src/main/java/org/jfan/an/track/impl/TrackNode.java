/**
 * 
 */
package org.jfan.an.track.impl;

import org.jfan.an.track.Track;

/**
 * <br>
 * <br>
 * 
 * @author JFan 2014年12月1日 下午4:11:10
 */
public final class TrackNode {

	private Track track;

	private boolean loop;
	private long intervalMillis;
	private boolean withFixedDelay;

	/**
	 * @return track
	 */
	public Track getTrack() {
		return track;
	}

	/**
	 * @param track 要设置的 track
	 */
	public void setTrack(Track track) {
		this.track = track;
	}

	/**
	 * @return loop
	 */
	public boolean isLoop() {
		return loop;
	}

	/**
	 * @param loop 要设置的 loop
	 */
	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	/**
	 * @return intervalMillis
	 */
	public long getIntervalMillis() {
		return intervalMillis;
	}

	/**
	 * @param intervalMillis 要设置的 intervalMillis
	 */
	public void setIntervalMillis(long intervalMillis) {
		this.intervalMillis = intervalMillis;
	}

	/**
	 * @return withFixedDelay
	 */
	public boolean isWithFixedDelay() {
		return withFixedDelay;
	}

	/**
	 * @param withFixedDelay 要设置的 withFixedDelay
	 */
	public void setWithFixedDelay(boolean withFixedDelay) {
		this.withFixedDelay = withFixedDelay;
	}

}
