/**
 * 
 */
package org.jfan.an.surfing.impl;

import org.jfan.an.cache.level2.Level2CacheService;
import org.jfan.an.surfing.abs.AbstractSurfing;

/**
 * 基于缓存的‘高速贮存’实现 <br>
 * 从缓存中取到对象，需要去‘集中式缓存’判断一个版本值，如果不一样，则回源<br>
 * <br>
 * 本地存储的对象可以设定失效时间<br>
 * 集中存储的版本值，使用默认失效时间<br>
 * 
 * @author JFan - 2014年10月30日 下午1:29:10
 */
public class CacheVersionSurfing<T> extends AbstractSurfing<T> {

	public static final String SUFFIX = ":Version";// 刷版本的地方，请自行拼接，并刷amass中的version值(+1)

	private int expSeconds;
	private int startVersion = 1;
	private Level2CacheService cacheService;

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public T getObject(String key) {
		VEntry<T> ve = cacheService.getLocal(key);
		if (null != ve) {
			String versionKey = key + SUFFIX;
			Integer version = cacheService.getAmass(versionKey);
			if (null == version) {
				cacheService.setAmass(versionKey, ve.getVersion());
				return ve.getValue();
			}
			if (version == ve.getVersion())
				return ve.getValue();
		}
		return null;
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public void setObject(String key, T value) {
		String versionKey = key + SUFFIX;
		Integer version = cacheService.getAmass(versionKey);

		VEntry<T> ve = new VEntry<T>();
		ve.setValue(value);
		ve.setVersion(null != version ? version : startVersion);

		cacheService.setLocal(key, ve, expSeconds);

		if (null == version)
			cacheService.setAmass(versionKey, ve.getVersion());// == startVersion
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
	 * @param startVersion 要设置的 startVersion
	 */
	public void setStartVersion(int startVersion) {
		this.startVersion = startVersion;
	}

	/**
	 * @param cacheService 要设置的 cacheService
	 */
	public void setCacheService(Level2CacheService cacheService) {
		this.cacheService = cacheService;
	}

}

class VEntry<T> {

	private T value;
	private int version;

	/**
	 * @return value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * @param value 要设置的 value
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * @return version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version 要设置的 version
	 */
	public void setVersion(int version) {
		this.version = version;
	}

}
