package nl.proxy.EkoMetro.Ctrl;

import java.util.Timer;
import java.util.TimerTask;

public class MetroScheduleTask extends TimerTask {
	private Manager mstMgr;
	private Timer mstTmr;

	private int mstBPM;
	private int mstBeat;
	
	public MetroScheduleTask(Manager mgr, Timer tmr, int bpm, int beat) {
		super();
		mstMgr = mgr;
		mstBPM = bpm;
		mstBeat = beat;
		mstTmr = tmr;
		mstTmr.scheduleAtFixedRate(this, 60000/mstBPM, 60000/mstBPM);
	}
	
	public MetroScheduleTask changeSpeed(int bpm) {
		if	(bpm != mstBPM) {
			MetroScheduleTask newTask = new MetroScheduleTask(mstMgr, mstTmr, bpm, mstBeat);
			cancel();
			return newTask;
		} else {
			return this;
		}
	}
	
	@Override
	public void run() {
		if	(mstMgr.doPlay(mstBeat)) {
			mstBeat = (mstBeat%240)+1;
		}
	}
}
