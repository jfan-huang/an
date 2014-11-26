/**
 * 
 */
package org.jfan.an.surfing.abs.strategy;

import org.jfan.an.cache.AddCacheService;
import org.jfan.an.surfing.SurfingSource;
import org.jfan.an.surfing.abs.LoadStrategy;

/**
 * 装载策略：对于集群中的所有机器而言，只用一个回源 <br>
 * 通过集中式缓存的add功能实现<br>
 * 
 * @author JFan - 2014年11月24日 下午4:27:40
 */
public class OnlyAmassFlag<T> implements LoadStrategy<T> {

	public static String FLAG_PREFIX = "OAF_";

	private AddCacheService cacheService;
	private String prefix = FLAG_PREFIX;

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.surfing.abs.LoadStrategy#load(org.an.surfing.SurfingSource,
	 * java.lang.String, java.lang.Object[])
	 */
	@Override
	public T load(SurfingSource<T> source, String key, Object... args) {
		String flagKey = prefix + key;
		boolean add = cacheService.add(flagKey, 1);
		if (add) {
			return source.toSource(args);
		} else {
			return null;// TODO 这个策略应该适用于 预载模式，当此处拿不到标识的时候，应当返回旧数据，不应返回null，也不应等待
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
	 * @param prefix 要设置的 prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}
