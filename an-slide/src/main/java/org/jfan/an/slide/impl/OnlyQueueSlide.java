/**
 * 
 */
package org.jfan.an.slide.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jfan.an.slide.impl.abs.OnlyAbstractSlide;

/**
 * <br>
 * <br>
 * 
 * @author JFan - 2014年11月26日 下午6:11:30
 */
public class OnlyQueueSlide extends OnlyAbstractSlide {

	private static Lock lock = new ReentrantLock();

	private BlockingQueue<Runnable> queue;
	private boolean init = false;

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public void submit(Runnable task) {
		RunnableFuture<Void> future = newTaskFor(task);
		queue.offer(future);
		execute(future);
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public <T> Future<T> submit(Callable<T> task) {
		RunnableFuture<T> future = newTaskFor(task);
		queue.offer(future);
		execute(future);
		return future;
	}

	// ####
	// ## private func

	private void execute(Runnable command) {
		if (!init) {
			// Args.notNull(queue, "'queue'");// 调用execute方法之前，已经往 queue中塞入东西了
			try {
				lock.lock();
				if (!init) {
					new Thread(new Runnable() {
						public void run() {
							for (;;) {// TODO 是否可行？
								Runnable runnable = queue.poll();
								runnable.run();
							}
						}
					}).start();
					init = true;
				}
			} finally {
				lock.unlock();
			}
		}
	}

	private RunnableFuture<Void> newTaskFor(Runnable runnable) {
		return new FutureTask<Void>(runnable, null);
	}

	private <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
		return new FutureTask<T>(callable);
	}

	// ####
	// ## set func

	/**
	 * @param queue 要设置的 queue
	 */
	public void setQueue(BlockingQueue<Runnable> queue) {
		this.queue = queue;
	}

}
