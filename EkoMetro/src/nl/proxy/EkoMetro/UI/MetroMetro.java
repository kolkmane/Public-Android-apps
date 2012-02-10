package nl.proxy.EkoMetro.UI;

import android.app.Activity;
import android.util.Log;

public class MetroMetro extends MetroSubUI {
	private final String MM_TAG = "MetroMetro";
	private int mNPM;
	private MetroBar mBar;
	
	public MetroMetro (Activity act, int pid) {
		super(pid);
		Log.d(MM_TAG, "Constructor called");
		super.LOG_TAG=MM_TAG+".super";
		mBar = new MetroBar(act);
		setView(mBar);
		attach(act);
	}
	
	public void setNPM(int npm) {
		if	(npm != mNPM) {
			mNPM = npm;
			mBar.setNPM(npm);
		}
	}
	
	public void setTPN(int tpn) {
		mBar.setTPN(tpn);
	}
	
	public void doBeat(int beat) {
		mBar.showBeat(beat);
	}
	
	public void doUpdate() {
		mBar.doUpdate();
	}
}
