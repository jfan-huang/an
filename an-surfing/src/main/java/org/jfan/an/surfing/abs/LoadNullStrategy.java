/**
 * 
 */
package org.jfan.an.surfing.abs;

/**
 * 回源策略 <br>
 * 增加‘null’值策略，防止拿不到数据，导致频繁回源<br>
 * 
 * @author JFan - 2014年11月25日 下午4:15:54
 */
public interface LoadNullStrategy<T> extends LoadStrategy<T> {

	/**
	 * 当回源得到一个null值的时候，调用此方法。
	 * 
	 * 需要返回一个具体的T值，以便防止之后的程序继续回源。
	 * 
	 * 如果继续返回null，后面的程序会继续回源
	 */
	public T loadNull(String key, Object... args);

}
