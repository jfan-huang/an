/**
 * 
 */
package org.jfan.an.surfing.abs.strategy;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.jfan.an.surfing.SurfingSource;
import org.jfan.an.surfing.abs.LoadStrategy;

/**
 * 装载策略：只有一个回源 <br>
 * 只适合单机情况<br>
 * 
 * @author JFan - 2014年11月24日 下午4:27:40
 */
public class OnlyFutureLoad<T> implements LoadStrategy<T> {

	private ConcurrentMap<String, Future<T>> stast = new ConcurrentHashMap<String, Future<T>>();

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public T load(SurfingSource<T> source, String key, Object... args) {
		T value;
		FutureTask<T> future = new FutureTask<T>(callable(source, key, args));

		Future<T> oldFuture = stast.putIfAbsent(key, future);
		if (null == oldFuture) {
			future.run();
			value = value(future, source, args);
		} else {
			future = null;
			value = value(oldFuture, source, args);
		}
		return value;
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public void loadAfter(String key) {
		stast.remove(key);
	}

	/**
	 * 构造 FutureTask 中取数据的执行过程Callable对象
	 */
	protected Callable<T> callable(final SurfingSource<T> source, final String key, final Object... args) {
		return new Callable<T>() {
			public T call() throws Exception {
				return source.toSource(args);
			}
		};
	}

	/**
	 * get value
	 */
	private T value(Future<T> future, SurfingSource<T> source, Object... args) {
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			// e.printStackTrace();// TODO logger
			return source.toSource(args);
		}
	}

}
