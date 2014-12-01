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
	 * 处理回源事宜
	 */
	public T load(SurfingSource<T> source, String key, Object... args);

	/**
	 * 在回源拿到数据之后，贮存好了对象之后，调用此方法。释放一些 load 中占用的资源
	 */
	public void loadAfter(String key);

}
