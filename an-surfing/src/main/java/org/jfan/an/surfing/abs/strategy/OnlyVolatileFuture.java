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
 * @deprecated 请使用OnlyFutureLoad
 */
public class OnlyVolatileFuture<T> implements LoadStrategy<T> {

	private ConcurrentMap<String, SpinStatus<T>> stast = new ConcurrentHashMap<String, SpinStatus<T>>();

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public T load(final SurfingSource<T> source, String key, final Object... args) {
		T value;
		FutureTask<T> ft = new FutureTask<T>(new Callable<T>() {
			public T call() throws Exception {
				return source.toSource(args);
			}
		});

		SpinStatus<T> status = new SpinStatus<T>();
		status.future = ft;

		SpinStatus<T> oldStatus = stast.putIfAbsent(key, status);
		if (null == oldStatus) {
			ft.run();
			value = value(ft, source, args);
			status.released = true;
		} else {
			ft = null;
			status = null;
			waitElder(oldStatus);
			value = value(oldStatus.future, source, args);
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

	protected void waitElder(SpinStatus<T> status) {
		while (!status.released) {
		}
	}

	private T value(Future<T> future, SurfingSource<T> source, Object... args) {
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			// e.printStackTrace();// 需要打印日志
			return source.toSource(args);
		}
	}

	public static class SpinStatus<T> {
		volatile boolean released;// 已经使用 ConcurrentMap 了，有没有必要继续使用此 标志位。待考虑
		Future<T> future;
	}

}
