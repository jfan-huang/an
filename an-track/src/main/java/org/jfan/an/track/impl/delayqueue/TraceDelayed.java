/**
 * 
 */
package org.jfan.an.track.impl.delayqueue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.jfan.an.track.Track;

/**
 * <br>
 * <br>
 * 
 * @author JFan - 2014年10月30日 下午4:04:25
 */
public class TraceDelayed implements Delayed {

	private Track track;
	private long milli;

	public TraceDelayed(Track track, long milli) {
		this.track = track;
		this.milli = milli;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Delayed o) {
		long nanoIn = (null == o ? 0 : ((TraceDelayed) o).milli);
		return (milli == nanoIn ? 0 : (milli > nanoIn ? 1 : -1));
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.util.concurrent.Delayed#getDelay(java.util.concurrent.TimeUnit)
	 */
	@Override
	public long getDelay(TimeUnit unit) {
		return TimeUnit.MILLISECONDS.convert(milli, unit);
	}

	/**
	 * @return track
	 */
	public Track getTrack() {
		return track;
	}

}