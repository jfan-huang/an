/**
 * 
 */
package org.jfan.an.track.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jfan.an.log.Logger;
import org.jfan.an.log.LoggerFactory;
import org.jfan.an.track.Track;
import org.jfan.an.track.TrackService;

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
		long milli = track.timeMillis();
		if (milli <= System.currentTimeMillis()) {
			if (pasc)
				executor(track);
			else
				logger.warn("Task '{}' has been past, is no longer running.", track.getClass());
		} else {
			delay(track, milli);
		}
	}

	/**
	 * 通知实现方式：时间还没到
	 */
	protected abstract void delay(Track track, long milli);

	/**
	 * 提供给实现方式：执行任务
	 */
	protected void executor(Track track) {
		logger.info("Submit TaskRun '{}'.", track.getClass());
		executorService().submit(runnable(track));// Void
	}

	/**
	 * 如果需要使用Runnable，请使用此方法统一返回
	 */
	protected Runnable runnable(final Track track) {
		return new Runnable() {
			/*
			 * （非 Javadoc）
			 * 
			 * @see java.lang.Runnable#run()
			 */
			@Override
			public void run() {
				try {
					logger.info("TaskRun '{}'.", track.getClass());
					track.onRun();
				} catch (Exception e) {
					logger.error("TaskRun '{}' ERROR: {}.", track.getClass(), e.getMessage());
					track.onError(e);
				}
			}
		};
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
