package org.jfan.an.cache;


/**
 * 缓存服务基类 <br>
 * <br>
 * 
 * @author JFan - 2014年10月30日 上午11:01:46
 */
public interface BaseCacheService {

	// 秒
	public static final int EXP_3_S = 3;
	public static final int EXP_5_S = 5;
	public static final int EXP_15_S = 15;
	public static final int EXP_30_S = 30;
	public static final int EXP_45_S = 45;

	// 分钟
	public static final int EXP_1_M = 60;
	public static final int EXP_1_M_30_S = 90;
	public static final int EXP_2_M = 120;
	public static final int EXP_2_M_30_S = 150;
	public static final int EXP_3_M = 180;
	public static final int EXP_5_M = 300;
	public static final int EXP_10_M = 600;
	public static final int EXP_15_M = 900;
	public static final int EXP_30_M = 1800;
	public static final int EXP_45_M = 2700;

	// 小时
	public static final int EXP_1_H = 3600;
	public static final int EXP_1H_30_M = 5400;
	public static final int EXP_2_H = 7200;
	public static final int EXP_2H_30_M = 9000;
	public static final int EXP_3_H = 10800;

	// 天
	public static final int EXP_1_D = 86400;
	public static final int EXP_2_D = EXP_1_D * 2;
	public static final int EXP_3_D = EXP_1_D * 3;
	public static final int EXP_5_D = EXP_1_D * 5;
	public static final int EXP_7_D = EXP_1_D * 7;

	/**
	 * 默认7天，Memcache最大时效时间
	 */
	public static final int DEF_EXP = EXP_7_D;

	public static final String DEFAULT_CHARSET = "UTF-8";

	// ###############################################################

	// ## SELECT

	/**
	 * get cache by key, null will return if not found
	 */
	public <V> V get(String key);

//	/**
//	 * get cache by key, null will return if not found <br>
//	 * And security requirements of conversion
//	 */
//	public <V> V get(String key, Transcoder<V> trans);

	/**
	 * The existence of
	 */
	public boolean isExist(String key);

	// ## DELETE

	/**
	 * delete cache by key
	 */
	public boolean delete(String key);

	// ## SET

	/**
	 * put 'key' 'value' to cache, Failure time after 'DEF_EXP'
	 */
	public <V> boolean set(String key, V value);

//	/**
//	 * put 'key' 'value' to cache, Failure time after 'DEF_EXP' <br>
//	 * And security requirements of conversion
//	 */
//	public <V> boolean set(String key, V value, Transcoder<V> trans);

	/**
	 * put 'key' 'value' to cache, Failure time after 'exp'
	 */
	public <V> boolean set(String key, V value, int exp);

//	/**
//	 * put 'key' 'value' to cache, Failure time after 'exp' <br>
//	 * And security requirements of conversion
//	 */
//	public <V> boolean set(String key, V value, int exp, Transcoder<V> trans);

}
