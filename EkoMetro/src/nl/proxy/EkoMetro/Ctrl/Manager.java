package nl.proxy.EkoMetro.Ctrl;

import java.util.Timer;

import nl.proxy.EkoMetro.UI.MetroBPM;
import nl.proxy.EkoMetro.UI.MetroControl;
import nl.proxy.EkoMetro.UI.MetroMeasure;
import nl.proxy.EkoMetro.UI.MetroMetro;
import nl.proxy.EkoMetro.UI.MetroVolume;
import nl.proxy.EkoMetro.Worker.MetroSound;
import nl.proxy.EkoMetro.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Manager {
	// Internal, hidden variables
	private static final String LOG_TAG = "EkoMetroStatus"; // Logging tag
	private static Manager emsCurrent; // needed for Singleton functionality

	// Preference keys
	private static final String PREFS_BPM = "BeatsPerMinute";
	private static final String PREFS_NPM = "NotesPerMeasure";
	private static final String PREFS_TPN = "NoteLength";
	private static final String PREFS_PAUSED = "Paused";

	// GUI objects
	private Activity     emsActivity;// current activity
	private MetroMeasure emsMeasure; // measure-selector
	private MetroBPM     emsBPM;     // Beats-per-minute selector
	private MetroVolume  emsVol;     // Mediavolume interface
	private MetroMetro   emsBar;     // graphical representation
	private MetroControl emsCtrl;    // control buttons
	
	// Worker objects
	private MetroSound	emsSound;    // soundobject
	private Timer		emsTimer;	 // scheduler
	private MetroScheduleTask emsTask; // huidige geschedulde task
	
	// Phone information for pause on incoming call
	private TelephonyManager emsPhoneMgr;
	
	// helper variables
	private int emsLastNPM = -1;
	private int emsLastTPN = -1;
	private int emsLastBPM = -1;
	private boolean emsInitialized = false;
	private boolean emsMute = false;

	private Manager() {
		if (emsCurrent != null) {
			Log.e(LOG_TAG,
					"illegal call to constructor: this is a singleton and it already exists!");
		}
		emsCurrent = this;
		restoreSettings();
	}

	public static synchronized Manager getCurrent() {
		if (emsCurrent == null) {
			emsCurrent = new Manager();
		}
		return emsCurrent;
	}

	public void setActivity(Activity act) {
		emsActivity = act;
		
		if (emsVol == null) {
			emsVol = new MetroVolume(emsActivity, R.id.volParent);
		} else {
			emsVol.attach(emsActivity);
		}
		
		if (emsBPM == null) {
			emsBPM = new MetroBPM(emsActivity, R.id.bpmParent);
			emsBPM.setManager(this);
		} else {
			emsBPM.attach(emsActivity);
		}
		
		if (emsBar == null) {
			emsBar = new MetroMetro(emsActivity, R.id.metroParent);
		} else {
			emsBar.attach(emsActivity);
		}
		
		if (emsMeasure == null) {
			emsMeasure = new MetroMeasure(emsActivity, R.id.measParent);
			emsMeasure.setManager(this);
		} else {
			emsMeasure.attach(emsActivity);
		}
		
		if (emsCtrl == null) {
			emsCtrl = new MetroControl(emsActivity, R.id.controlParent);
		} else {
			emsCtrl.attach(emsActivity);
		}
		
		if	(emsSound == null) {
			emsSound = new MetroSound(emsActivity);
		} else {
			emsSound.attach(emsActivity);
		}
		
		if	(emsTimer == null) {
			emsTimer = new Timer();
		}
		
		if	(emsTask == null) {
			emsTask = new MetroScheduleTask(this,emsTimer,emsBPM.getValue(),1);
		}
		
		emsPhoneMgr = (TelephonyManager)emsActivity.getSystemService(Context.TELEPHONY_SERVICE);
		emsMute = false;
	}
	
	public void doPause() {
		emsCtrl.setPaused(true);
	}
	
	public void setMute(boolean mute) {
		emsMute = mute;
	}

	public void clearActivity() {
		if	(emsPhoneMgr.getCallState() != TelephonyManager.CALL_STATE_IDLE) {
			setMute(true);
		}
		emsActivity = null;
		emsPhoneMgr = null;
		emsVol.detach();
		emsBPM.detach();
		emsBar.detach();
		emsMeasure.detach();
		emsSound.detach();
		emsCtrl.detach();
	}

	// Restore should only be called when we have an activity...
	public void restoreSettings() {
		int tpn;
		int npm;
		int bpm;
		boolean pause;
		
		SharedPreferences settings;
		if	((emsActivity != null) && (!emsInitialized)) {
			settings = emsActivity.getPreferences(Context.MODE_PRIVATE);
	
			bpm = settings.getInt(PREFS_BPM, MetroBPM.MBPM_DEFVALUE);
			npm = settings.getInt(PREFS_NPM, 0);
			tpn = settings.getInt(PREFS_TPN, 0);
			pause = settings.getBoolean(PREFS_PAUSED, true);
			
			emsBPM.setValue(bpm);
			
			emsBar.setNPM(npm);
			emsBar.setTPN(tpn);
			emsBar.doUpdate();
			
			emsMeasure.radSelect(npm, tpn);
			
			emsCtrl.setPaused(pause);
			
			emsTask = emsTask.changeSpeed(bpm);
			emsInitialized = true;
		}
	}

	// save might be called when we don't have an activity, so we need it as parameter
	public void saveSettings(Activity act) {
		SharedPreferences settings;
		settings = act.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();

		editor.putInt(PREFS_BPM, emsBPM.getValue());
		editor.putInt(PREFS_NPM, emsMeasure.getNPM());
		editor.putInt(PREFS_TPN, emsMeasure.getTPN());
		editor.putBoolean(PREFS_PAUSED, emsCtrl.getPaused());
 
		editor.commit();
	}
	
	public boolean doPlay(int beat) {
		if	(emsMeasure.getNPM() == 0) {
			return false;
		}
		if	(emsCtrl.getPaused()) {
			// Report we do nothing.
			return false;			
		} else {
			// calculation to get value of 1 to NPM instead of 0 to NPM-1

			beat = 1+((beat - 1) % emsMeasure.getNPM());
			if	(emsMute == false)
				emsSound.doBeat(beat);
			
			if	(emsActivity != null) {
				// Only necessary if we have a activity
				emsBar.doBeat(beat);
			}
			
			return true;
		}
	}
	
	public void doUpdate() {
		int bpm = emsBPM.getValue();
		int npm = emsMeasure.getNPM();
		int tpn = emsMeasure.getTPN();
		boolean updateBar = false;

		if	(npm != emsLastNPM) {
			emsLastNPM = npm;
			emsBar.setNPM(npm);
			updateBar = true;
		}
		
		if	(npm != emsLastTPN) {
			emsLastTPN = tpn;
			emsBar.setTPN(tpn);
			updateBar = true;
		}
		
		if	(updateBar) {
			emsBar.doUpdate();
		}
		
		if	(bpm != emsLastBPM) {
			emsTask = emsTask.changeSpeed(bpm);
		}
	}
}
