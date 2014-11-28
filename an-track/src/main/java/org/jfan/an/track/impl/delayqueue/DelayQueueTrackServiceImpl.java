/**
 * 
 */
package org.jfan.an.track.impl.delayqueue;

import java.util.concurrent.DelayQueue;

import org.jfan.an.track.Track;
import org.jfan.an.track.impl.AbstractTrackService;

/**
 * 使用延迟队列，实现的任务服务 <br>
 * <br>
 * 
 * @author JFan - 2014年10月30日 下午4:00:05
 */
public class DelayQueueTrackServiceImpl extends AbstractTrackService {

	private DelayQueue<TraceDelayed> queue = new DelayQueue<TraceDelayed>();

	public DelayQueueTrackServiceImpl() {
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

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	protected void delay(Track track, long milli) {
		queue.offer(new TraceDelayed(track, milli));
	}

}
