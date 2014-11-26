/**
 * 
 */
package org.jfan.an.slide.impl.abs;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.jfan.an.slide.Slide;
import org.jfan.an.slide.SlideCallback;

/**
 * ‘滑梯’抽象类，主要封装 submit(SlideCallback) 的调用 <br>
 * <br>
 * 
 * @author JFan - 2014年11月26日 下午6:46:26
 */
public abstract class AbstractSlide implements Slide {

	protected <T> Callable<T> toCallable(final SlideCallback<T> task) {
		return new Callable<T>() {
			@Override
			public T call() throws Exception {
				return task.call();
			}
		};
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public <T> void submit(SlideCallback<T> task) {
		Callable<T> callable = toCallable(task);
		Future<T> future = submit(callable);

		try {
			T value = future.get();
			task.back(value);
		} catch (InterruptedException e) {
			// e.printStackTrace();// ignore, 不应该发生的
		} catch (ExecutionException e) {
			task.error(e);
		}
	}

}
