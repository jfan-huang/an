/**
 * 
 */
package org.jfan.an.track.timer;

import org.jfan.an.track.AbstractTrackServiceTest;
import org.jfan.an.track.TrackService;
import org.jfan.an.track.impl.timer.TimerTrackServiceImpl;

public class TimerTrackServiceImplTest extends AbstractTrackServiceTest {

	/**
	 * {@inheritDoc} <br>
	 */
	@Override
	public TrackService getTrackService() {
		return new TimerTrackServiceImpl();
	}

}
