/**
 * 
 */
package org.jfan.an.surfing.abs;

import org.jfan.an.surfing.SurfingSource;

/**
 * 回源时，拿到null数据，导致后面每次都需要回源 <br>
 * 这里提供一个策略，处理此类情况<br>
 * 
 * @author JFan - 2014年11月25日 下午4:15:54
 */
public interface NullStrategy<T> {

	/**
	 * load null Processing
	 */
	public T loadNull(SurfingSource<T> source, String key, Object... args);// TODO 入参待定

}
