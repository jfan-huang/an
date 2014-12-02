/**
 * 
 */
package org.jfan.an.track.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jfan.an.log.Logger;
import org.jfan.an.log.LoggerFactory;
import org.jfan.an.track.Track;
import org.jfan.an.track.TrackLoop;
import org.jfan.an.track.TrackService;
import org.jfan.an.utils.Args;

/**
 * 任务服务抽象类 <br>
 * 包装一下检测：是否过时抛弃、是否立即执行等<br>
 * 
 * @author JFan - 2014年10月30日 下午4:01:14
 */
public abstract class AbstractTrackService implements TrackService {

	// 各回各家，各找各妈。
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private ExecutorService executorService;
	private int maximumPoolSize = 5;

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public void placeTrack(Track track) {
		placeTrack(track, track.pasc());
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public void placeTrack(Track track, boolean pasc) {
		boolean loop = TrackLoop.class.isAssignableFrom(track.getClass());
		if (loop)
			Args.check(0 < ((TrackLoop) track).intervalMillis(), "The time interval is not correct.");

		// init TrackNode
		TrackNode trackNode = new TrackNode();
		trackNode.setTrack(track);
		if (loop) {
			trackNode.setLoop(loop);
			TrackLoop tl = (TrackLoop) track;
			trackNode.setIntervalMillis(tl.intervalMillis());
			trackNode.setWithFixedDelay(tl.withFixedDelay());
		}

		// check run
		long milli = track.timeMillis();
		long time = System.currentTimeMillis();
		if (milli <= time) {
			if (pasc)
				executor(trackNode);
			else {
				logger.warn("Task '{}' has been past, is no longer running.", track.getClass());

				// loop - next run
				if (trackNode.isLoop())
					delay(trackNode, time + trackNode.getIntervalMillis());
			}
		} else {
			delay(trackNode, milli);
		}
	}

	/**
	 * 通知实现方式：交给具体的实现
	 */
	protected abstract void delay(TrackNode trackNode, long milli);

	/**
	 * 提供给实现方式：执行任务
	 * 
	 * 如果子类执行任务时，不是使用此方法提交任务，那么请自行实现Loop功能
	 */
	protected void executor(TrackNode trackNode) {
		logger.info("Submit TaskRun '{}'.", trackNode.getTrack().getClass());
		executorService().submit(runnable(trackNode));// Future<Void>

		// loop - withFixed submit
		if (trackNode.isWithFixedDelay())
			loop(trackNode);
	}

	/**
	 * 如果需要使用Runnable，请使用此方法统一返回
	 */
	protected Runnable runnable(final TrackNode trackNode) {
		return new Runnable() {
			/*
			 * （非 Javadoc）
			 * 
			 * @see java.lang.Runnable#run()
			 */
			@Override
			public void run() {
				Track track = trackNode.getTrack();
				try {
					if (logger.isDebugEnabled())
						logger.debug("TaskRun '{}'.", track.getClass());
					track.onRun();
				} catch (Exception e) {
					logger.error("TaskRun '{}' ERROR: {}.", track.getClass(), e.getMessage());
					track.onError(e);
				}

				// loop - withFixed End
				if (!trackNode.isWithFixedDelay())
					loop(trackNode);
			}
		};
	}

	private void loop(TrackNode trackNode) {
		if (trackNode.isLoop()) {
			long milli = System.currentTimeMillis() + trackNode.getIntervalMillis();
			delay(trackNode, milli);
		}
	}

	/**
	 * 指定线程池 | 使用固定大小的线程池（默认5，可设定）
	 */
	private ExecutorService executorService() {
		if (null == executorService) {
			logger.info("The ExecutorService Not Init, use default {}.", maximumPoolSize);
			executorService = Executors.newFixedThreadPool(maximumPoolSize);
		}
		return executorService;
	}

	// ####
	// ## set func

	/**
	 * @param executorService 要设置的 executorService
	 */
	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	/**
	 * @param maximumPoolSize 要设置的 maximumPoolSize
	 */
	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}

}
