/**
 * 
 */
package org.jfan.an.track;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * <br>
 * <br>
 * 
 * @author JFan 2014年12月4日 上午10:30:24
 */
public abstract class AbstractTrackServiceTest {

	public static int MAX_S = 5;

	private TrackService trackService;

	@Before
	public void setUp() throws Exception {
		trackService = getTrackService();
	}

	public abstract TrackService getTrackService();

	@Test
	public void placeTrack() throws Exception {
		RunTF[] runs = initObj(5);
		System.out.println("------------------");

		for (RunTF run : runs)
			trackService.placeTrack(run);

		for (int i = 1; i <= MAX_S + 1; i++) {
			Thread.sleep(1000);
			check(runs, i);
			System.out.println("sleep " + i);
		}

		// test pasc() is false
		// ...
	}

	@Test
	public void placeTrackLoop() {
		// ...
	}

	private void check(RunTF[] runs, int s) throws Exception {
		for (RunTF run : runs) {
			int seconds = run.getSeconds();
			// System.out.println(s + "\t" + seconds);

			if (s > seconds) {
				// 一定运行了
				checkRun(run);
			} else {
				// 因为线程暂停的原因，有可能已经执行了
				if (run.isRun())
					checkRun(run);
				else
					checkNotRun(run);
			}
		}
	}

	private void checkRun(RunTF run) throws Exception {
		Assert.assertTrue(run.isRun());
		if (run.isRunErr()) {
			Thread.sleep(10);// onError 是异步调用
			Assert.assertTrue(run.isErr());
		}
	}

	private void checkNotRun(RunTF run) {
		Assert.assertFalse(run.isRun());
		Assert.assertFalse(run.isRunErr());
		Assert.assertFalse(run.isErr());
	}

	private RunTF[] initObj(int size) {
		List<RunTF> list = new ArrayList<RunTF>();
		for (int i = 1; i <= size; i++)
			list.add(new RunTF());
		return list.toArray(new RunTF[list.size()]);
	}

}
