/**
 * 
 */
package org.jfan.an.surfing.abs;

import java.util.Map;
import java.util.Map.Entry;

import org.jfan.an.surfing.Surfing;
import org.jfan.an.surfing.SurfingSource;
import org.jfan.an.surfing.SurfingSourceAll;

/**
 * Surfing接口的抽象实现类 <br>
 * <br>
 * 增加：预载全部待贮存对象功能<br>
 * 增加：使用 strategy 策略机进行回源<br>
 * 
 * @author JFan - 2014年10月30日 下午1:29:10
 */
public abstract class AbstractSurfing<T> implements Surfing<T> {

	// 回源接口，此参数是必须的，此处选择外部主动调用set方法设置
	protected SurfingSource<T> source;
	// 在set source时，判定是否SurfingSourceAll的实现
	protected boolean supportLoadAll = false;
	// 回源策略
	private LoadStrategy<T> strategy;

	/**
	 * 初始化方法，在子类完成set时，调用此方法进行init
	 */
	public void initial() {
		if (supportLoadAll) {
			// init : load all data
			SurfingSourceAll<T> ssa = (SurfingSourceAll<T>) source;
			Map<String, T> all = ssa.toSourceAll();
			if (null != all)
				for (Entry<String, T> entry : all.entrySet())
					setObject(entry.getKey(), entry.getValue());// set all
		}
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

		if (null == value) {
			// load
			if (null != strategy)
				value = strategy.load(source, key, args);
			else
				value = source.toSource(args);
			// set back
			if (null != value)// TODO null 策略？ 防止‘假数据’频繁回源
				setObject(key, value);
		}

		return value;
	}

	// ####
	// ## set func

	/**
	 * @param source 要设置的 source
	 */
	public void setSource(SurfingSource<T> source) {
		if (SurfingSourceAll.class.isAssignableFrom(source.getClass()))
			supportLoadAll = true;
		this.source = source;
	}

	/**
	 * @param strategy 要设置的 strategy
	 */
	public void setStrategy(LoadStrategy<T> strategy) {
		this.strategy = strategy;
	}

}
