/**
 * 
 */
package org.jfan.an.slide;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import org.jfan.an.cache.AddCacheService;
import org.jfan.an.slide.impl.ExecutorPoolSlide;
import org.jfan.an.slide.impl.GroupExecutorPoolSlide;
import org.jfan.an.slide.impl.GroupQueueSlide;
import org.jfan.an.slide.impl.QueueSlide;

/**
 * ‘滑滑梯，一次只能滑下来一个’，包装工厂类<br>
 * 约定一次只能有x个任务在处理<br>
 * 
 * @author JFan - 2014年11月26日 下午3:28:34
 */
public final class SlideFactory {

	/**
	 * 使用本地线程池实现，指定可同时执行的数量 <br>
	 * 只适合单机情况<br>
	 * 
	 * @param threadNum 可同时执行的数量
	 */
	public static final Slide newExecutorPool(int threadNum) {
		ExecutorPoolSlide slide = new ExecutorPoolSlide();
		slide.setThreadNum(threadNum);
		return slide;
	}

	/**
	 * 使用本地线程池实现<br>
	 * 只适合单机情况<br>
	 * 
	 * @param executor 线程池实现
	 */
	public static final Slide newExecutorPool(ExecutorService executor) {
		ExecutorPoolSlide slide = new ExecutorPoolSlide();
		slide.setExecutor(executor);
		return slide;
	}

	/**
	 * 使用本地线程池实现，指定可同时执行的数量 <br>
	 * 适合集群情况<br>
	 * 
	 * @param threadNum 可同时执行的数量
	 */
	public static final Slide newExecutorPoolOnly(int threadNum, AddCacheService cacheService, long intervalMilliseconds) {
		GroupExecutorPoolSlide slide = new GroupExecutorPoolSlide();
		slide.setIntervalMilliseconds(intervalMilliseconds);
		slide.setCacheService(cacheService);
		slide.setThreadNum(threadNum);
		return slide;
	}

	/**
	 * 使用本地线程池实现<br>
	 * 适集群情况<br>
	 * 
	 * @param executor 线程池实现
	 */
	public static final Slide newExecutorPoolOnly(ExecutorService executor, AddCacheService cacheService, long intervalMilliseconds) {
		GroupExecutorPoolSlide slide = new GroupExecutorPoolSlide();
		slide.setIntervalMilliseconds(intervalMilliseconds);
		slide.setCacheService(cacheService);
		slide.setExecutor(executor);
		return slide;
	}

	/**
	 * 使用队列实现，一次只能处理一个任务<br>
	 * 只适合单机情况<br>
	 */
	public static final Slide newQueue() {
		QueueSlide slide = new QueueSlide();
		slide.setQueue(new LinkedBlockingQueue<Runnable>());
		return slide;
	}

	/**
	 * 使用队列实现，一次只能处理一个任务<br>
	 * 适合集群情况<br>
	 */
	public static final Slide newQueueOnly(AddCacheService cacheService, long intervalMilliseconds) {
		GroupQueueSlide slide = new GroupQueueSlide();
		slide.setIntervalMilliseconds(intervalMilliseconds);
		slide.setQueue(new LinkedBlockingQueue<Runnable>());
		slide.setCacheService(cacheService);
		return slide;
	}

}
