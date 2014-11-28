package org.jfan.an.track;

import java.util.Date;

import org.jfan.weapon.probability.random.RandomUtil;

public class RunTF implements Track {

	public static int MAX_S = 10;

	private String name;
	private int s;

	public RunTF(String name) {
		this.name = name;

		s = RandomUtil.nextInt(-10, MAX_S);
		System.out.println("执行时间（x秒后）：" + s + "\t" + name);
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public long timeMillis() {
		long n = System.currentTimeMillis();
		long nn = n + (s * 1000);
		return nn;
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public boolean pasc() {
		return true;
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public void onRun() {
		System.out.println(name + " 执行了：" + new Date());

		if (1 == RandomUtil.nextInt(5))
			throw new RuntimeException("XXX 模拟发生错误 XXX");
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public void onError(Exception e) {
		System.out.println(name + " 发生错误: " + new Date() + " - " + e.getMessage());
	}

}
