/**
 * 
 */
package org.jfan.an.surfing.abs.strategy;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.jfan.an.cache.AddCacheService;
import org.jfan.an.cache.BaseCacheService;
import org.jfan.an.surfing.SurfingSource;

/**
 * 装载策略：只有一个回源 <br>
 * 适合集群情况<br>
 * 
 * @author JFan 2014年12月1日 上午11:29:41
 */
public class OnlyFutureLoadGroup<T> extends OnlyFutureLoad<T> {

	private int exp = BaseCacheService.EXP_3_S;
	private AddCacheService cacheService;
	private String suffix = ":Flag";

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	protected Callable<T> callable(final SurfingSource<T> source, final String key, final Object... args) {
		return new Callable<T>() {
			@SuppressWarnings("unchecked")
			public T call() throws Exception {
				String keyFlag = key + suffix;

				// 回源拿数据
				if (cacheService.add(keyFlag, 1, exp)) {
					return source.toSource(args);

					// 等着取别人拿到的数据
				} else {
					for (;;) {
						// 用判断flag是否存在，也不是万全的做法，考虑使用 消息通知
						// 有可能在 exp 时间内，回源任然未完成
						if (!cacheService.isExist(keyFlag))
							return (T) cacheService.get(key);

						waitGo(100);// JProfiler分析，次方法稍微耗时，存在红色线程
					}
				}
			}
		};
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public void loadAfter(String key) {
		String keyFlag = key + suffix;
		cacheService.delete(keyFlag);

		super.loadAfter(key);
	}

	/**
	 * 等待'hm'毫秒的时间
	 */
	private void waitGo(int hm) {
		long nanoTime = System.nanoTime() + (TimeUnit.MILLISECONDS.toNanos(hm));
		for (;;) {
			if (System.nanoTime() >= nanoTime)
				return;
		}
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
	 * @param suffix 要设置的 suffix
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * @param exp 要设置的 exp
	 */
	public void setExp(int exp) {
		this.exp = exp;
	}

}
