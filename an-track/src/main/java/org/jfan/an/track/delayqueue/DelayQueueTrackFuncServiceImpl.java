/**
 * 
 */
package org.jfan.an.track.delayqueue;

import java.util.concurrent.DelayQueue;

import org.jfan.an.track.AbstractTrackFuncService;
import org.jfan.an.track.TrackFunc;

/**
 * <br>
 * <br>
 * 
 * @author JFan - 2014年10月30日 下午4:00:05
 */
public class DelayQueueTrackFuncServiceImpl extends AbstractTrackFuncService {

	private DelayQueue<TraceDelayed> queue;

	public DelayQueueTrackFuncServiceImpl() {
		queue = new DelayQueue<TraceDelayed>();

		// Thread thread = new Thread(new R());
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				for (;;) {
					TraceDelayed track = queue.poll();
					if (null != track)
						executor(track.getTrack());
					// else
					// System.out.println("***********");
				}
			}
		});
		thread.setDaemon(true);
		thread.start();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * org.an.track.abs.AbstractTrackFuncService#delay(org.an.track.TrackFunc,
	 * long)
	 */
	@Override
	protected void delay(TrackFunc track, long milli) {
		queue.offer(new TraceDelayed(track, milli));
	}

}
