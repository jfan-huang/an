/**
 * 
 */
package org.jfan.an.surfing;

import java.util.Map;

/**
 * 数据回源<br>
 * 具备预载全部内容 的能力<br>
 * 
 * @author JFan - 2014年10月29日 上午11:18:07
 */
public interface SurfingSourceAll<T> extends SurfingSource<T> {

	/**
	 * 一次性回源操作（内部自行处理key的构造，应保证与toKey方法一致）
	 */
	public Map<String, T> toSourceAll();

}
