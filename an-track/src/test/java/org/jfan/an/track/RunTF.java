package org.jfan.an.track;

import java.util.Date;

import org.jfan.an.track.TrackFunc;
import org.jfan.weapon.probability.random.RandomUtil;

public class RunTF implements TrackFunc {

	public static int MAX_S = 10;

	private String str;
	private int s;

	public RunTF() {
		s = RandomUtil.nextInt(-10, MAX_S);
		str = new Date() + "\t" + s;
		System.out.println(s);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.track.TrackFunc#timeMillis()
	 */
	@Override
	public long timeMillis() {
		long n = System.currentTimeMillis();
		long nn = n + (s * 1000);
		return nn;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.track.TrackFunc#pasc()
	 */
	@Override
	public boolean pasc() {
		return true;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.track.TrackFunc#onRun()
	 */
	@Override
	public void onRun() {
		System.out.println("RUN: " + str + " - runFunc - " + new Date());

		if (1 == RandomUtil.nextInt(5))
			throw new RuntimeException("XXX 模拟发生错误 XXX");
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.track.TrackFunc#onError(java.lang.Throwable)
	 */
	@Override
	public void onError(Throwable t) {
		System.out.println("RUN: " + str + " - onErroe - " + new Date() + " - " + t.getMessage());
	}

}
