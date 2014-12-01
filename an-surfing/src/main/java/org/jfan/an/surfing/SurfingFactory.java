/**
 * 
 */
package org.jfan.an.surfing;

import org.jfan.an.cache.AddCacheService;
import org.jfan.an.cache.BaseCacheService;
import org.jfan.an.cache.level2.ExpLimit;
import org.jfan.an.cache.level2.LocalNotFoundNotice;
import org.jfan.an.cache.level2.impl.Level2CacheServiceImpl;
import org.jfan.an.surfing.abs.LoadStrategy;
import org.jfan.an.surfing.abs.strategy.OnlyFutureLoad;
import org.jfan.an.surfing.abs.strategy.OnlyFutureLoadGroup;
import org.jfan.an.surfing.impl.CacheSurfing;
import org.jfan.an.surfing.impl.MemoryFixedSurfing;

/**
 * 冲浪‘高速贮存’接口 封包工厂对象 <br>
 * 获取各种实现的 工厂方法<br>
 * 外部只需要准备基础组件<br>
 * 
 * @author JFan - 2014年11月21日 下午4:33:12
 */
public final class SurfingFactory {

	private SurfingFactory() {
	}

	/**
	 * 存储在 T对象数组中，数组大小不可变
	 * 
	 * 适合永不变的对象
	 */
	public static final <T> Surfing<T> newMemoryFixedSize(SurfingSource<T> source, Class<T> clzss, int size) {
		MemoryFixedSurfing<T> memoryFixedSurfing = new MemoryFixedSurfing<T>(clzss, size);
		memoryFixedSurfing.setSource(source);

		memoryFixedSurfing.initial();
		return memoryFixedSurfing;
	}

	/**
	 * 存储在 缓存组件中，固定的超时时间
	 */
	public static final <T> Surfing<T> newCached(SurfingSource<T> source, BaseCacheService cacheService, int expSeconds) {
		return sacheSurfing(source, cacheService, expSeconds, null);
	}

	/**
	 * 存储在 缓存组件中，固定的超时时间，失效时只有一个回源（相同的key）（本地）
	 */
	public static final <T> Surfing<T> newCachedLoadOnly(SurfingSource<T> source, BaseCacheService cacheService, int expSeconds) {
		return sacheSurfing(source, cacheService, expSeconds, new OnlyFutureLoad<T>());
	}

	/**
	 * 存储在 缓存组件中，固定的超时时间，时效时只有一个回源（相同的key）（集群）
	 */
	public static final <T> Surfing<T> newCachedLoadOnlyGroup(SurfingSource<T> source, AddCacheService cacheService, int expSeconds) {
		OnlyFutureLoadGroup<T> onlyLoad = new OnlyFutureLoadGroup<T>();
		onlyLoad.setCacheService(cacheService);
		return sacheSurfing(source, cacheService, expSeconds, onlyLoad);
	}

	/**
	 * 存储在两层缓存中，固定的超时时间（远端），本地超时时间依赖于def（ {@link org.an.cache.level2.DefExpLimit}）
	 */
	public static final <T> Surfing<T> newLevel2Cached(SurfingSource<T> source, BaseCacheService localCache, BaseCacheService amassCache, int amassExpSeconds) {
		return newLevel2Cached(source, localCache, amassCache, amassExpSeconds, null, null);
	}

	/**
	 * 存储在两层缓存中，固定的超时时间（远端），本地超时时间由expLimit计算，并且具备条件通知能力（ {@link org.jfan.an.cache.level2.LocalNotFoundNotice}）
	 */
	public static final <T> Surfing<T> newLevel2Cached(SurfingSource<T> source, BaseCacheService localCache, BaseCacheService amassCache, int amassExpSeconds//
			, ExpLimit expLimit, LocalNotFoundNotice notice) {
		Level2CacheServiceImpl level2Cache = level2Cache(localCache, amassCache, expLimit, notice);
		return newCached(source, level2Cache, amassExpSeconds);
	}

	/**
	 * 存储在两层缓存中，固定的超时时间（远端），本地超时时间依赖于def（{@link org.an.cache.level2.DefExpLimit} ），失效时只有一个回源（相同的key）（本地）
	 */
	public static final <T> Surfing<T> newLevel2CachedLoadOnly(SurfingSource<T> source, BaseCacheService localCache, BaseCacheService amassCache, int amassExpSeconds) {
		return newLevel2CachedLoadOnly(source, localCache, amassCache, amassExpSeconds, null, null);
	}

	/**
	 * 存储在两层缓存中，固定的超时时间（远端），本地超时时间由expLimit计算，并且具备条件通知能力（ {@link org.jfan.an.cache.level2.LocalNotFoundNotice}），失效时只有一个回源（相同的key）（本地）
	 */
	public static final <T> Surfing<T> newLevel2CachedLoadOnly(SurfingSource<T> source, BaseCacheService localCache, BaseCacheService amassCache, int amassExpSeconds//
			, ExpLimit expLimit, LocalNotFoundNotice notice) {
		Level2CacheServiceImpl level2Cache = level2Cache(localCache, amassCache, expLimit, notice);
		return newCachedLoadOnly(source, level2Cache, amassExpSeconds);
	}

	/**
	 * 存储在两层缓存中，固定的超时时间（远端），本地超时时间依赖于def（{@link org.an.cache.level2.DefExpLimit} ），失效时只有一个回源（相同的key）（集群）
	 */
	public static final <T> Surfing<T> newLevel2CachedLoadOnlyGroup(SurfingSource<T> source, BaseCacheService localCache, AddCacheService amassCache, int amassExpSeconds) {
		return newLevel2CachedLoadOnlyGroup(source, localCache, amassCache, amassExpSeconds, null, null);
	}

	/**
	 * 存储在两层缓存中，固定的超时时间（远端），本地超时时间由expLimit计算，并且具备条件通知能力（ {@link org.jfan.an.cache.level2.LocalNotFoundNotice}），失效时只有一个回源（相同的key）（集群）
	 */
	public static final <T> Surfing<T> newLevel2CachedLoadOnlyGroup(SurfingSource<T> source, BaseCacheService localCache, AddCacheService amassCache, int amassExpSeconds//
			, ExpLimit expLimit, LocalNotFoundNotice notice) {
		Level2CacheServiceImpl level2Cache = level2Cache(localCache, amassCache, expLimit, notice);
		OnlyFutureLoadGroup<T> onlyLoad = new OnlyFutureLoadGroup<T>();
		onlyLoad.setCacheService(amassCache);
		return sacheSurfing(source, level2Cache, amassExpSeconds, onlyLoad);
	}

	// ####
	// ## private func

	private static final Level2CacheServiceImpl level2Cache(BaseCacheService localCache, BaseCacheService amassCache, ExpLimit expLimit, LocalNotFoundNotice notice) {
		Level2CacheServiceImpl cacheService = new Level2CacheServiceImpl();
		cacheService.setLocalCache(localCache);
		cacheService.setAmassCache(amassCache);
		cacheService.setExpLimit(expLimit);
		cacheService.setNotice(notice);
		return cacheService;
	}

	private static final <T> CacheSurfing<T> sacheSurfing(SurfingSource<T> source, BaseCacheService cacheService, int expSeconds, LoadStrategy<T> strategy) {
		CacheSurfing<T> cacheSurfing = new CacheSurfing<T>();
		cacheSurfing.setSource(source);
		cacheSurfing.setCacheService(cacheService);

		cacheSurfing.setExpSeconds(expSeconds);
		cacheSurfing.setStrategy(strategy);

		cacheSurfing.initial();
		return cacheSurfing;
	}

}
