package org.jfan.an.surfing;

/**
 * 冲浪，‘高速贮存’对象 <br>
 * <br>
 * 
 * @author JFan - 2014年10月29日 上午11:18:02
 */
public interface Surfing<T> {

	/**
	 * 根据入参，取得一个‘高速贮存’的对象
	 */
	public T ammunition(Object... args);

}
