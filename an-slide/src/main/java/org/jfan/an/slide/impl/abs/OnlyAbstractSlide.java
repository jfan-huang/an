/**
 * 
 */
package org.jfan.an.slide.impl.abs;

import java.util.concurrent.Callable;

import org.apache.http.util.Args;
import org.jfan.an.cache.AddCacheService;
import org.jfan.an.slide.SlideCallback;

/**
 * <br>
 * <br>
 * 
 * @author JFan - 2014年11月26日 下午6:46:26
 */
public abstract class OnlyAbstractSlide extends AbstractSlide {

	private String key = "slide_only_" + this.hashCode();
	private AddCacheService cacheService;
	private long intervalMilliseconds;// 隔多久检测一次

	protected <T> Callable<T> toCallable(final SlideCallback<T> task) {
		return new Callable<T>() {
			@Override
			public T call() throws Exception {
				boolean add = cacheService.add(key, 1);
				if (add)
					return task.call();
				else
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
