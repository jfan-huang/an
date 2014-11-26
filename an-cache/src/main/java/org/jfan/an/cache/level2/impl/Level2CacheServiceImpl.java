package org.jfan.an.cache.level2.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jfan.an.cache.BaseCacheService;
import org.jfan.an.cache.level2.ExpLimit;
import org.jfan.an.cache.level2.Level2CacheService;
import org.jfan.an.cache.level2.LocalNotFoundNotice;
import org.jfan.an.log.Logger;
import org.jfan.an.log.LoggerFactory;

/**
 * 两层缓存操作
 * 
 * @author JFan 2014-1-29 下午4:48:46
 */
@SuppressWarnings("unchecked")
public class Level2CacheServiceImpl implements Level2CacheService {

	private static Logger logger = LoggerFactory.getLogger(Level2CacheServiceImpl.class);

	private ExpLimit expLimit;
	private BaseCacheService localCache;
	private BaseCacheService amassCache;

	private LocalNotFoundNotice notice;
	private ExecutorService executorService;

	// ####
	// ##对本地缓存的操作

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * org.an.cache.base.level2.Level2CacheService#putLocal(java.lang.String,
	 * java.lang.Object, int)
	 */
	@Override
	public <V> boolean putLocal(String key, V value, int localExp) {
		return localCache.set(key, (Object) value, localExp);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * org.an.cache.base.level2.Level2CacheService#getLocal(java.lang.String)
	 */
	@Override
	public <V> V getLocal(String key) {
		Object object = localCache.get(key);
		return (V) object;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * org.an.cache.base.level2.Level2CacheService#deleteLocal(java.lang.String)
	 */
	@Override
	public boolean deleteLocal(String key) {
		return localCache.delete(key);
	}

	// ####
	// ##对集中式缓存的操作

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * org.an.cache.base.level2.Level2CacheService#putAmass(java.lang.String,
	 * java.lang.Object, int)
	 */
	@Override
	public <V> boolean putAmass(String key, V value, int amassExp) {
		return amassCache.set(key, (Object) value, amassExp);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * org.an.cache.base.level2.Level2CacheService#getAmass(java.lang.String)
	 */
	@Override
	public <V> V getAmass(String key) {
		Object object = amassCache.get(key);
		return (V) object;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * org.an.cache.base.level2.Level2CacheService#deleteAmass(java.lang.String)
	 */
	@Override
	public boolean deleteAmass(String key) {
		return amassCache.delete(key);
	}

	// ####
	// ##两层缓存操作

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.cache.base.BaseCacheService#get(java.lang.String)
	 */
	@Override
	public <V> V get(String key) {
		// 先从本地缓存读取内容，没有再从集中缓存中读取
		V v = getLocal(key);
		if (null == v) {
			v = getAmass(key);
			if (null != v)
				notice(key, v);
		}
		return v;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.cache.base.level2.Level2CacheService#get(java.lang.String,
	 * int)
	 */
	public <V> V get(String key, int localExp) {
		V v = getLocal(key);
		if (null == v) {
			v = getAmass(key);
			if (null != v) {
				putLocal(key, v, localExp);
				notice(key, v);
			}
		}
		return v;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.cache.base.BaseCacheService#isExist(java.lang.String)
	 */
	@Override
	public boolean isExist(String key) {
		return null != get(key);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.cache.base.BaseCacheService#delete(java.lang.String)
	 */
	@Override
	public boolean delete(String key) {
		// 出于数据可靠性，先删除集中式中的缓存，在删除本地缓存
		deleteAmass(key);
		deleteLocal(key);
		return true;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.cache.base.BaseCacheService#set(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public <V> boolean set(String key, V value) {
		return set(key, value, DEF_EXP, expLimit());
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.cache.base.BaseCacheService#set(java.lang.String,
	 * java.lang.Object, int)
	 */
	@Override
	public boolean set(String key, Object value, int exp) {
		return set(key, value, exp, expLimit());
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.cache.base.level2.Level2CacheService#set(java.lang.String,
	 * java.lang.Object, int, org.an.cache.base.level2.ExpLimit)
	 */
	@Override
	public boolean set(String key, Object value, int exp, ExpLimit expLimit) {
		int local = expLimit.localExp(exp);
		return set(key, value, local, exp);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.cache.base.level2.Level2CacheService#setExpLimit(org.an.cache
	 * .base.level2.ExpLimit)
	 */
	@Override
	public void setExpLimit(ExpLimit expLimit) {
		if (null != this.expLimit) {
			int amassExp = 60;
			int localExp = this.expLimit.localExp(amassExp);
			int nowLocalExp = expLimit.localExp(amassExp);
			logger.warn("The '{}' timeLimit Reset; test, old: {} -- {}, now: {} -- {}.", this, amassExp, localExp, amassExp, nowLocalExp);
		}
		this.expLimit = expLimit;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * org.an.cache.level2.Level2CacheService#setNotice(org.an.cache.level2.
	 * LocalNotFoundNotice)
	 */
	@Override
	public void setNotice(LocalNotFoundNotice notice) {
		this.notice = notice;
	}

	// ####
	// ## private func

	/**
	 * 将key-value分别存储于‘本地’、‘集中’缓存中
	 */
	private <V> boolean set(String key, V value, int localExp, int amassExp) {
		// 出于性能考虑，先缓存本地，在缓存集中缓存
		// 本地失败，不会再向集中缓存
		boolean ok = putLocal(key, value, localExp);
		if (ok) {
			ok = putAmass(key, value, amassExp);
			// 本地成功，集中失败，则本地也删除，告知外部 失败
			if (!ok)
				deleteLocal(key);
		}
		return ok;
	}

	/**
	 * 通知回调
	 */
	private <T> void notice(final String key, final Object value) {
		if (null == notice)
			return;

		executorService().submit(new Runnable() {
			public void run() {
				notice.notice(key, value);
			}
		});
	}

	private ExecutorService executorService() {
		if (null == executorService)
			synchronized (notice) {
				if (null == executorService)
					executorService = Executors.newFixedThreadPool(10);
			}
		return executorService;
	}

	private ExpLimit expLimit() {
		if (null == expLimit) {
			expLimit = new DefExpLimit();
			logger.warn("The TimeLimit Not Set, use default '{}'.", expLimit.getClass());
		}
		return expLimit;
	}

	// ####
	// ## set func

	/**
	 * @param localCache 要设置的 localCache
	 */
	public void setLocalCache(BaseCacheService localCache) {
		if (amassCache instanceof Level2CacheService)
			throw new IllegalArgumentException("LocalCache Not achieve class " + Level2CacheService.class);
		this.localCache = localCache;
	}

	/**
	 * @param amassCache 要设置的 amassCache
	 */
	public void setAmassCache(BaseCacheService amassCache) {
		if (amassCache instanceof Level2CacheService)
			throw new IllegalArgumentException("AmassCache Not achieve class " + Level2CacheService.class);
		this.amassCache = amassCache;
	}

	/**
	 * @param executorService 要设置的 executorService
	 */
	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

}
