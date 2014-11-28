/**
 * 
 */
package org.jfan.an.track.impl.timer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.jfan.an.track.Track;
import org.jfan.an.track.impl.AbstractTrackService;

/**
 * <br>
 * <br>
 * 
 * @author JFan - 2014年11月5日 上午10:12:11
 */
public class TimerTrackServiceImpl extends AbstractTrackService {

	private Timer timer = new Timer(true);

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.track.AbstractTrackFuncService#delay(org.an.track.TrackFunc,
	 * long)
	 */
	@Override
	protected void delay(final Track track, long milli) {
		timer.schedule(new TimerTask() {

			/*
			 * （非 Javadoc）
			 * 
			 * @see java.util.TimerTask#run()
			 */
			@Override
			public void run() {
				executor(track);
			}

		}, new Date(milli));
	}

}
