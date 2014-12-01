/**
 * 
 */
package org.jfan.an.surfing.abs;

import java.util.Map;
import java.util.Map.Entry;

import org.jfan.an.log.Logger;
import org.jfan.an.log.LoggerFactory;
import org.jfan.an.surfing.Surfing;
import org.jfan.an.surfing.SurfingSource;
import org.jfan.an.surfing.SurfingSourceAll;
import org.jfan.an.surfing.SurfingSourceAllRefresher;
import org.jfan.an.track.TrackFactory;
import org.jfan.an.track.TrackService;
import org.jfan.an.track.basic.LoopTrack;

/**
 * Surfing接口的抽象实现类 <br>
 * <br>
 * 增加：预载全部待贮存对象功能<br>
 * 增加：使用 strategy 策略机进行回源<br>
 * 
 * @author JFan - 2014年10月30日 下午1:29:10
 */
public abstract class AbstractSurfing<T> implements Surfing<T> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	// 回源接口，此参数是必须的，此处选择外部主动调用set方法设置
	protected SurfingSource<T> source;
	// 回源策略
	private LoadStrategy<T> strategy;
	private boolean supportNull;
	// 任务服务
	private TrackService trackService;

	/**
	 * 初始化方法，在子类完成set时，调用此方法进行init
	 */
	public void initial() {
		Class<?> clz;
		if (null != source && SurfingSourceAll.class.isAssignableFrom(clz = source.getClass())) {
			// init : load all data
			doLoadAll((SurfingSourceAll<T>) source);

			// init : Refresher Track
			if (SurfingSourceAllRefresher.class.isAssignableFrom(clz)) {
				final SurfingSourceAllRefresher<T> ssar = (SurfingSourceAllRefresher<T>) source;
				trackService().placeTrack(new LoopTrack(0, ssar.intervalMilliseconds()) {
					public boolean pasc() {
						return false;// 第一次不运行
					}

					public void onRun() {
						doLoadAll(ssar);// load all
					}
				});
			}
		}

		supportNull = (null != strategy && LoadNullStrategy.class.isAssignableFrom(strategy.getClass()));
	}

	/**
	 * 贮存对象
	 */
	public abstract void setObject(String key, T value);

	/**
	 * 取得贮存对象
	 */
	public abstract T getObject(String key);

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public T ammunition(Object... args) {
		String key = source.toKey(args);
		T value = getObject(key);// abstract

		// load
		if (null == value)
			value = doLoad(key, args);

		return value;
	}

	/**
	 * 回源
	 */
	protected T doLoad(String key, Object... args) {
		T value;
		if (null != strategy) {
			value = strategy.load(source, key, args);
			doSetObject(value, key, args);
			strategy.loadAfter(key);
		} else {
			value = source.toSource(args);
			doSetObject(value, key, args);
		}
		return value;
	}

	/**
	 * 装载全部数据
	 */
	protected void doLoadAll(SurfingSourceAll<T> ssa) {
		Map<String, T> all = ssa.toSourceAll();
		if (null != all)
			for (Entry<String, T> entry : all.entrySet())
				setObject(entry.getKey(), entry.getValue());// set all
	}

	/**
	 * setObject()之前，增加null策略
	 */
	protected void doSetObject(T value, String key, Object... args) {
		// set back
		if (null != value)
			setObject(key, value);

		// null 策略？ 防止‘假数据’频繁回源
		else if (supportNull) {
			T value2 = ((LoadNullStrategy<T>) strategy).loadNull(key, args);
			setObject(key, value2);

		} else {
			// logger
		}
	}

	// ####
	// ## private func

	private TrackService trackService() {
		if (null == trackService) {
			trackService = TrackFactory.newTimer();
			logger.info("'TrackService' is not specified, the default implementation using {}.", trackService.getClass());
		}
		return trackService;
	}

	// ####
	// ## set func

	/**
	 * @param source 要设置的 source
	 */
	public void setSource(SurfingSource<T> source) {
		this.source = source;
	}

	/**
	 * @param strategy 要设置的 strategy
	 */
	public void setStrategy(LoadStrategy<T> strategy) {
		this.strategy = strategy;
	}

	/**
	 * @param trackService 要设置的 trackService
	 */
	public void setTrackService(TrackService trackService) {
		this.trackService = trackService;
	}

}
