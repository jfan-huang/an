/**
 * 
 */
package org.jfan.an.surfing.impl;

import java.lang.reflect.Array;

import org.jfan.an.surfing.abs.AbstractSurfing;
import org.jfan.weapon.secret.hash.HashAlgorithms;

/**
 * 基于固定对象数组的‘高速贮存’实现 <br>
 * <br>
 * 
 * @author JFan - 2014年10月30日 上午10:17:16
 */
public class MemoryFixedSurfing<T> extends AbstractSurfing<T> {

	private static final int MAXIMUM_CAPACITY = 1 << 9;// 512,出于性能考虑，限制上限

	private int threshold;

	private T[] values;
	private Class<T> clzss;

	public MemoryFixedSurfing(Class<T> clzss) {
		this(clzss, MAXIMUM_CAPACITY);
	}

	@SuppressWarnings("unchecked")
	public MemoryFixedSurfing(Class<T> clzss, int threshold) {
		if (threshold < 0)
			throw new IllegalArgumentException("Illegal initial capacity: " + threshold);
		if (threshold > MAXIMUM_CAPACITY)
			throw new IllegalArgumentException("The initial maximum capacity: " + MAXIMUM_CAPACITY);

		this.clzss = clzss;
		this.threshold = threshold;
		values = (T[]) Array.newInstance(this.clzss, this.threshold);
	}

	/**
	 * {@inheritDoc} <br>
	 */
	public T getObject(String key) {
		int hash = tohash(key);
		return values[hash];
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public void setObject(String key, T value) {
		int hash = tohash(key);
		values[hash] = value;
	}

	private int tohash(String key) {
		return HashAlgorithms.additiveHash(key, threshold);
	}

}
