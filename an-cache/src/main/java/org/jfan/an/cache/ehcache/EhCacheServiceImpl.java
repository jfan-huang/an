package org.jfan.an.cache.ehcache;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.jfan.an.cache.BaseCacheService;
import org.jfan.an.log.Logger;
import org.jfan.an.log.LoggerFactory;

/**
 * 基于Ehcache本地缓存（单机） <br>
 * <br>
 * 
 * @author JFan - 2014年10月30日 上午11:30:22
 */
public class EhCacheServiceImpl implements BaseCacheService {

	private static Logger logger = LoggerFactory.getLogger(EhCacheServiceImpl.class);
	private static Lock lock = new ReentrantLock();

	private Cache cache;

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <V> V get(String key) {
		if (null == key) {
			logger.warn("Get EhCache By KEY: Null.");
			return null;
		}

		Element value = cache().get(key);
		if (logger.isDebugEnabled())
			logger.debug("Get EhCache, key: {}, value: {}", key, value);
		return (null == value ? null : (V) value.getObjectValue());
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public boolean isExist(String key) {
		boolean exist = (get(key) != null);
		if (logger.isDebugEnabled())
			logger.debug("IsExist EhCache, key: {}, exist: {}", key, exist);
		return exist;
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public boolean delete(String key) {
		if (null == key) {
			logger.warn("Delete EhCache By key: Null.");
			return true;
		}

		boolean result = cache().remove(key);
		if (logger.isDebugEnabled())
			logger.debug("Delete EhCache, key: {}, resule: {}", key, result);
		return result;
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public <V> boolean set(String key, V value) {
		return set(key, value, DEF_EXP);
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public <V> boolean set(String key, V value, int exp) {
		if (null == key || null == value) {
			logger.warn("Set EhCache The NULL. key: {}, value: {}, exp: {}", key, value, exp);
			return false;
		}

		int exp_ = (exp > 0 ? exp : DEF_EXP);
		exp_ = exp_ >= DEF_EXP ? DEF_EXP : exp_;

		Element element = new Element(key, value, false, exp_, exp_);
		cache().put(element);
		if (logger.isDebugEnabled())
			logger.debug("Set EhCache toCache. key: {}, exp: {}, result: {}, value: {}", key, exp_, true, value);
		return true;
	}

	// ####
	// ## private func

	private Cache cache() {
		if (null == cache) {
			lock.lock();
			try {
				if (null == cache) {
					String cacheName = CacheManager.DEFAULT_NAME;
					CacheManager cm = CacheManager.getInstance();
					Ehcache ehcache = cm.addCacheIfAbsent("DEF_" + this.hashCode());
					cache = (Cache) ehcache;
					if (null == cache)
						throw new IllegalArgumentException("Cache was not available for instance.");
					logger.info("Cache is not set, the default instance of use '{}'.", cacheName);
				}
			} finally {
				lock.unlock();
			}
		}
		return cache;
	}

	// ####
	// ## set func

	/**
	 * @param cache 要设置的 cache
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
	}

}
