/**
 * 
 */
package org.jfan.an.track;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jfan.an.log.Logger;
import org.jfan.an.log.LoggerFactory;

/**
 * <br>
 * <br>
 * 
 * @author JFan - 2014年10月30日 下午4:01:14
 */
public abstract class AbstractTrackFuncService implements TrackFuncService {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private ExecutorService executorService;
	private int maximumPoolSize = 5;

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.track.TrackFuncService#placeTrack(org.an.track.TrackFunc)
	 */
	@Override
	public void placeTrack(TrackFunc track) {
		placeTrack(track, track.pasc());
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.track.TrackFuncService#placeTrack(org.an.track.TrackFunc,
	 * boolean)
	 */
	@Override
	public void placeTrack(TrackFunc track, boolean pasc) {
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
	protected abstract void delay(TrackFunc track, long milli);

	/**
	 * 提供给实现方式：执行任务
	 */
	protected void executor(TrackFunc track) {
		logger.info("Submit TaskRun '{}'.", track.getClass());
		Future<?> submit = executorService().submit(runnable(track));
		logger.debug("Future<?> submit = {}. ---->>>> Future ?????", submit);// TODO
	}

	/**
	 * 如果需要使用Runnable，请使用此方法统一返回
	 */
	protected Runnable runnable(final TrackFunc track) {
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
