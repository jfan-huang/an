/**
 * 
 */
package org.jfan.an.cache.redis;

import org.jfan.an.cache.AddCacheService;
import org.jfan.an.log.Logger;
import org.jfan.an.log.LoggerFactory;

import redis.clients.jedis.Jedis;

/**
 * <br>
 * <br>
 * 
 * @author JFan - 2014年11月5日 下午3:29:31
 */
public class RedisCacheServiceImpl implements AddCacheService {

	private static final Logger logger = LoggerFactory.getLogger(RedisCacheServiceImpl.class);

	// private ShardedJedis jedis;// 切片额客户端连接
	private Jedis jedis;// 非切片额客户端连接

	public RedisCacheServiceImpl() {
		throw new RuntimeException("Not fully implemented.");
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <V> V get(String key) {
		if (null == key) {
			logger.warn("Get Redis By KEY: Null.");
			return null;
		}

		String value = jedis.get(key);
		if (logger.isDebugEnabled())
			logger.debug("Get Redis, key: {}, value: {}", key, value);
		return (null == value ? null : (V) djson(value));
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public boolean isExist(String key) {
		boolean exist = jedis.exists(key);
		if (logger.isDebugEnabled())
			logger.debug("IsExist Redis, key: {}, exist: {}", key, exist);
		return exist;
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public boolean delete(String key) {
		if (null == key) {
			logger.warn("Delete Redis By key: Null.");
			return true;
		}

		Long del = jedis.del(key);
		if (logger.isDebugEnabled())
			logger.debug("Delete Redis, key: {}, delNum: {}", key, del);
		return true;

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
			logger.warn("Set Redis The NULL. key: {}, value: {}, exp: {}", key, value, exp);
			return false;
		}

		int exp_ = (exp > 0 ? exp : DEF_EXP);
		exp_ = exp_ >= DEF_EXP ? DEF_EXP : exp_;

		String json = json(value);
		jedis.setex(key, exp_, json);
		if (logger.isDebugEnabled())
			logger.debug("Set Redis toCache. key: {}, exp: {}, result: {}, value: {}", key, exp_, true, json);
		return true;
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public <V> boolean add(String key, V value) {
		return add(key, value, DEF_EXP);
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public <V> boolean add(String key, V value, int exp) {
		if (null == key || null == value) {
			logger.warn("Add Redis The NULL Param... key: {}, value: {}, exp: {}.", key, value, exp);
			return false;
		}

		int exp_ = (exp > 0 ? exp : DEF_EXP);
		exp_ = exp_ >= DEF_EXP ? DEF_EXP : exp_;

		String json = json(value);
		boolean result = (0 < jedis.setnx(key, json));
		if (result)
			jedis.expire(key, exp_);
		if (logger.isDebugEnabled())
			logger.debug("Add Redis toCache. key: {}, exp: {}, result: {}, value: {}", key, exp_, result, value);
		return result;
	}

	// ####
	// ## private func

	private <V> String json(V value) {
		// TODO 待实现
		return "";
	}

	private <V> V djson(String json) {
		// TODO 待实现
		return null;
	}

	// ####
	// ## set func

	/**
	 * @param jedis 要设置的 jedis
	 */
	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}

}
