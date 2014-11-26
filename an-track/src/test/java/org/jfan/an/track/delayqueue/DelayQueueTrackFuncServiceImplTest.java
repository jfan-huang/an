package org.jfan.an.track.delayqueue;

import org.jfan.an.track.RunTF;
import org.jfan.an.track.delayqueue.DelayQueueTrackFuncServiceImpl;
import org.junit.Test;

public class DelayQueueTrackFuncServiceImplTest {

	private DelayQueueTrackFuncServiceImpl service = new DelayQueueTrackFuncServiceImpl();

	@Test
	public void testPasc() {
		System.out.println(123);
		service.placeTrack(new RunTF());
		System.out.println(321);

		for (;;) {
		}
	}

	@Test
	public void testPlaceTrackTrackFuncBoolean() {
		// fail("尚未实现");
	}

}
