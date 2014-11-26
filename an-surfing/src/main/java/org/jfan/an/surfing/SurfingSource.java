/**
 * 
 */
package org.jfan.an.surfing;

/**
 * 描述数据如何回源，如何确认唯一标识 <br>
 * <br>
 * 
 * @author JFan - 2014年10月29日 上午11:18:07
 */
public interface SurfingSource<T> {

	/**
	 * 根据入参回源查询数据
	 */
	public T toSource(Object... args);

	/**
	 * 根据入参（回源的入参）转换成唯一的key
	 */
	public String toKey(Object... args);

}
