/**
 * 
 */
package org.jfan.an.surfing.abs.strategy;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jfan.an.surfing.SurfingSource;
import org.jfan.an.surfing.abs.LoadStrategy;
import org.jfan.weapon.secret.hash.HashAlgorithms;

/**
 * 初始化固定大小的 ‘线程池组’<br>
 * 根据key 2 hash，定位到 组 中的某个线程池 <br>
 * 相同的key，都交由同一个线程池去执行（只有一个在执行）<br>
 * <br>
 * 
 * TODO ‘线程池组’性能是个问题？
 * 
 * @author JFan - 2014年11月21日 下午5:43:14
 */
public class OnlyExecutionPoolGroup<T> implements LoadStrategy<T> {

	private static final int MAXIMUM_CAPACITY = 1 << 7;// 128

	private ExecutorService[] values;
	private int threshold;

	public OnlyExecutionPoolGroup() {
		this(MAXIMUM_CAPACITY);
	}

	public OnlyExecutionPoolGroup(int threshold) {
		if (threshold < 0)
			throw new IllegalArgumentException("Illegal initial capacity: " + threshold);
		if (threshold > MAXIMUM_CAPACITY)
			throw new IllegalArgumentException("The initial maximum capacity: " + MAXIMUM_CAPACITY);

		this.threshold = threshold;
		values = new ExecutorService[this.threshold];
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.surfing.abs.LoadStrategy#load(org.an.surfing.SurfingLoad,
	 * java.lang.String, java.lang.Object[])
	 */
	@Override
	public T load(final SurfingSource<T> source, final String key, final Object... args) {
		ExecutorService executor = executorService(key);

		Future<T> submit = executor.submit(new Callable<T>() {
			@Override
			public T call() throws Exception {
				return source.toSource(args);
			}
		});

		try {
			return submit.get();
			// return submit.get(6000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
			// } catch (TimeoutException e) {
			// e.printStackTrace();
		}
	}

	private ExecutorService executorService(String key) {
		int hash = HashAlgorithms.additiveHash(key, threshold);
		ExecutorService es = values[hash];
		if (null == es)
			synchronized (key) {
				es = values[hash];
				if (null == es) {
					es = Executors.newSingleThreadExecutor();// 单一线程
					values[hash] = es;
				}
			}
		return es;
	}

}
