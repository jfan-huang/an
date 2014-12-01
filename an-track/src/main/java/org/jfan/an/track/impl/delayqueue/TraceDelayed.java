/**
 * 
 */
package org.jfan.an.track.impl.delayqueue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.jfan.an.track.impl.TrackNode;

/**
 * 延迟队列 封装对象 <br>
 * <br>
 * 
 * @author JFan - 2014年10月30日 下午4:04:25
 */
public class TraceDelayed implements Delayed {

	private TrackNode trackNode;
	private long milli;

	public TraceDelayed(TrackNode trackNode, long milli) {
		this.trackNode = trackNode;
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

	// ####
	// ## set func

	/**
	 * @return track
	 */
	public TrackNode getTrackNode() {
		return trackNode;
	}

}
