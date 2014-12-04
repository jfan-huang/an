/**
 * 
 */
package org.jfan.an.track.impl.delayqueue;

import java.util.concurrent.DelayQueue;

import org.jfan.an.track.impl.AbstractTrackService;
import org.jfan.an.track.impl.TrackNode;

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
					try {
						TraceDelayed track = queue.take();
						executor(track.getTrackNode());// DelayQueue不允许null值
					} catch (InterruptedException e) {
						// e.printStackTrace();// ignore
						// logger
					}
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
	protected void delay(TrackNode trackNode, long milli) {
		queue.offer(new TraceDelayed(trackNode, milli));
	}

}
