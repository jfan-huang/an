/**
 * 
 */
package org.jfan.an.track.timer;

import org.jfan.an.track.RunTF;
import org.jfan.an.track.timer.TimerTrackFuncServiceImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * <br>
 * <br>
 * 
 * @author JFan - 2014年11月5日 上午11:18:26
 */
public class TimerTrackFuncServiceImplTest {

	private TimerTrackFuncServiceImpl service;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		service = new TimerTrackFuncServiceImpl();
	}

	@Test
	public void placeTrack() {
		service.placeTrack(new RunTF());

		try {
			for (int i = 1; i <= RunTF.MAX_S + 1; i++) {
				System.out.println(i);
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
		}

		System.out.println("END");
	}

}
