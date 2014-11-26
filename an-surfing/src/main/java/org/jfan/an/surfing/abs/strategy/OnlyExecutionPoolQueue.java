/**
 * 
 */
package org.jfan.an.surfing.abs.strategy;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jfan.an.surfing.SurfingSource;
import org.jfan.an.surfing.abs.LoadStrategy;

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
public class OnlyExecutionPoolQueue<T> implements LoadStrategy<T> {

	private static final int MAXIMUM_CAPACITY = 1 << 7;// 128

	private ExecutorService executorService;
	private Queue<T>[] values;
	private int threshold;

	public OnlyExecutionPoolQueue(Class<T> clzss) {
		this(clzss, MAXIMUM_CAPACITY);
	}

	public OnlyExecutionPoolQueue(Class<T> clzss, int threshold) {
		if (threshold < 0)
			throw new IllegalArgumentException("Illegal initial capacity: " + threshold);
		if (threshold > MAXIMUM_CAPACITY)
			throw new IllegalArgumentException("The initial maximum capacity: " + MAXIMUM_CAPACITY);

		this.threshold = threshold;
		this.values = new ArrayBlockingQueue[this.threshold];
		// this.values = new ArrayBlockingQueue<T>(2)[this.threshold];// TODO
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

		// TODO 未完成的实现，下面的代码不正确
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
		if (null == executorService)
			synchronized (key) {
				if (null == executorService) {
					executorService = Executors.newFixedThreadPool(this.threshold);// fixed25
				}
			}
		return executorService;
	}

	// ####
	// ## set func

	/**
	 * @param executorService 要设置的 executorService
	 */
	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

}
