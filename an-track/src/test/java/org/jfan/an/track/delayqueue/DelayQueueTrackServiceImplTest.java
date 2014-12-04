package org.jfan.an.track.delayqueue;

import org.jfan.an.track.AbstractTrackServiceTest;
import org.jfan.an.track.TrackService;
import org.jfan.an.track.impl.delayqueue.DelayQueueTrackServiceImpl;

public class DelayQueueTrackServiceImplTest extends AbstractTrackServiceTest {

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public TrackService getTrackService() {
		return new DelayQueueTrackServiceImpl();
	}

}
