/**
 * 
 */
package org.jfan.an.surfing.impl;

import org.jfan.an.cache.BaseCacheService;
import org.jfan.an.surfing.abs.AbstractSurfing;

/**
 * 基于缓存的‘高速贮存’实现 <br>
 * <br>
 * 
 * @author JFan - 2014年10月30日 下午1:29:10
 */
public class CacheSurfing<T> extends AbstractSurfing<T> {

	private int expSeconds;
	private BaseCacheService cacheService;

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public T getObject(String key) {
		return cacheService.get(key);
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public void setObject(String key, T value) {
		if (0 >= expSeconds)
			cacheService.set(key, value);
		else
			cacheService.set(key, value, expSeconds);
	}

	// ####
	// ## set func

	/**
	 * @param expSeconds 要设置的 expSeconds
	 */
	public void setExpSeconds(int expSeconds) {
		this.expSeconds = expSeconds;
	}

	/**
	 * @param cacheService 要设置的 cacheService
	 */
	public void setCacheService(BaseCacheService cacheService) {
		this.cacheService = cacheService;
	}

}
