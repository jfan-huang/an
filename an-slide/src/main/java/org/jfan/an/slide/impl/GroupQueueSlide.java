/**
 * 
 */
package org.jfan.an.slide.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.http.util.Args;
import org.jfan.an.cache.AddCacheService;
import org.jfan.an.slide.SlideCallback;

/**
 * 通过阻塞队列，实现‘滑梯’，<b>适合集群环境</b><br>
 * 一次只能有一个在执行<br>
 * <br>
 * 通过集中式缓存的ADD功能，实现抢占任务点，抢不到的在指定间隔之后再抢<br>
 * 
 * @author JFan - 2014年11月26日 下午6:11:30
 */
public class GroupQueueSlide extends QueueSlide {

	private String key = "slide_q_only_" + this.hashCode();
	private AddCacheService cacheService;
	private long intervalMilliseconds;

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	protected <T> Callable<T> toCallable(final SlideCallback<T> task) {
		return new Callable<T>() {
			@Override
			public T call() throws Exception {
				int exp = (int) TimeUnit.MILLISECONDS.toSeconds(intervalMilliseconds) - 1;// 早1秒失效
				boolean add = cacheService.add(key, 1, exp);
				if (add)
					return task.call();

				Thread.sleep(intervalMilliseconds);// TODO 寻找更好的方案
				return call();
			}
		};
	}

	// ####
	// ## set func

	/**
	 * @param cacheService 要设置的 cacheService
	 */
	public void setCacheService(AddCacheService cacheService) {
		this.cacheService = cacheService;
	}

	/**
	 * @param intervalMilliseconds 要设置的 intervalMilliseconds
	 */
	public void setIntervalMilliseconds(long intervalMilliseconds) {
		Args.check(0 >= intervalMilliseconds, "'intervalMilliseconds' should be a value greater than zero.");
		this.intervalMilliseconds = intervalMilliseconds;
	}

}
