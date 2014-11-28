/**
 * 
 */
package org.jfan.an.track.timer;

import org.jfan.an.track.RunTF;
import org.jfan.an.track.impl.timer.TimerTrackServiceImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * <br>
 * <br>
 * 
 * @author JFan - 2014年11月5日 上午11:18:26
 */
public class TimerTrackServiceImplTest {

	private TimerTrackServiceImpl service;

	@Before
	public void setUp() throws Exception {
		service = new TimerTrackServiceImpl();
	}

	@Test
	public void placeTrack() {
		service.placeTrack(new RunTF("Timer_1"));
		service.placeTrack(new RunTF("Timer_2"));
		service.placeTrack(new RunTF("Timer_3"));
		service.placeTrack(new RunTF("Timer_4"));

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
