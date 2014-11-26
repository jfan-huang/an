/**
 * 
 */
package org.jfan.an.slide.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.http.util.Args;
import org.jfan.an.slide.impl.abs.AbstractSlide;

/**
 * <br>
 * <br>
 * 
 * @author JFan - 2014年11月26日 下午4:37:22
 */
public class ExecutorPoolSlide extends AbstractSlide {

	private static Lock lock = new ReentrantLock();

	private int threadNum;
	private ExecutorService executor;

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public void submit(Runnable task) {
		executor().submit(task);
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public <T> Future<T> submit(Callable<T> task) {
		Future<T> future = executor().submit(task);
		return future;
	}

	// ####
	// ## private func

	private ExecutorService executor() {
		if (null == executor)
			try {
				lock.lock();
				if (null == executor) {
					Args.check(1 > threadNum, "'threadNum' minimum should be 1.");
					executor = Executors.newFixedThreadPool(threadNum);
				}
			} finally {
				lock.unlock();
			}

		return executor;
	}

	// ####
	// ## set func

	/**
	 * @param threadNum 要设置的 threadNum
	 */
	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	/**
	 * @param executor 要设置的 executor
	 */
	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

}
