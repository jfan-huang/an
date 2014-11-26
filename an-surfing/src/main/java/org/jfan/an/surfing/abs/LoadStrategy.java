/**
 * 
 */
package org.jfan.an.surfing.abs;

import org.jfan.an.surfing.SurfingSource;

/**
 * 在发生 load回源请求数据时，交由此接口处理 <br>
 * <br>
 * 
 * @author JFan - 2014年11月21日 下午4:55:54
 */
public interface LoadStrategy<T> {

	/**
	 * load Processing
	 */
	public T load(SurfingSource<T> source, String key, Object... args);

}
