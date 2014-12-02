package org.jfan.an.track.delayqueue;

import org.jfan.an.track.RunTF;
import org.jfan.an.track.impl.delayqueue.DelayQueueTrackServiceImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

//TODO
@Ignore
public class DelayQueueTrackServiceImplTest {

	private DelayQueueTrackServiceImpl service;

	@Before
	public void setUp() throws Exception {
		service = new DelayQueueTrackServiceImpl();
	}

	@Test
	public void testPasc() throws InterruptedException {
		service.placeTrack(new RunTF("DelayQueue_1"));
		service.placeTrack(new RunTF("DelayQueue_2"));
		service.placeTrack(new RunTF("DelayQueue_3"));
		service.placeTrack(new RunTF("DelayQueue_4"));

		int i = 1;
		for (;;) {
			System.out.println(i++);
			Thread.sleep(1000);
		}
	}

}
