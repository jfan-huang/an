package org.jfan.an.track;

import org.jfan.weapon.probability.random.RandomUtil;

public class RunTF implements Track {

	private int seconds;
	private boolean run, runErr;
	private boolean err;

	public RunTF() {
		int max = AbstractTrackServiceTest.MAX_S;
		seconds = RandomUtil.nextInt(-max, max);
		System.out.println("seconds: " + seconds);
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public long timeMillis() {
		long n = System.currentTimeMillis();
		long nn = n + (seconds * 1000);
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
		run = true;
		System.out.println(">>Run......" + seconds);

		if (1 == RandomUtil.nextInt(5)) {
			runErr = true;
			// System.out.println("模拟发生异常。");
			throw new RuntimeException("XXX 模拟发生错误 XXX");
		}
	}

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public void onError(Exception e) {
		err = true;
	}

	// ####
	// ## get func

	/**
	 * @return seconds
	 */
	public int getSeconds() {
		return seconds;
	}

	/**
	 * @return run
	 */
	public boolean isRun() {
		return run;
	}

	/**
	 * @return runErr
	 */
	public boolean isRunErr() {
		return runErr;
	}

	/**
	 * @return err
	 */
	public boolean isErr() {
		return err;
	}

}
