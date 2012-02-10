package nl.proxy.EkoMetro.UI;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class MetroSubUI {
	public String LOG_TAG = "MetroSubUI";
	private int msu_pid;
	private Activity msu_act;
	private ViewGroup msu_pv;
	private View msu_mainview;
	
	public MetroSubUI(Activity act, int pid, int id) {
		msu_pid = pid;
		msu_mainview = act.getLayoutInflater().inflate(id,null);
		attach(act);
	}
	
	// Sometimes we have to split the constructor
	public MetroSubUI(int pid) {
		msu_pid = pid;
	}
	
	public void setView(View view) {
		msu_mainview = view;
	}

	public View getView() {
		return msu_mainview;
	}

	public Activity getActivity() {
		return msu_act;
	}
	
	public void attach (Activity act) {
		Log.d(LOG_TAG, "attach called");
		if	(msu_pid == 0) {
			Log.d(LOG_TAG, "parent not set");
		}
		if	(msu_mainview == null) {
			Log.d(LOG_TAG, "mainview nog set");
		}
		
		// Do nothing if we attach to current activity
		if	(msu_act == act) {
			Log.d(LOG_TAG, "attach called with current activity");
			return;
		}
		
		if	(msu_act != null) {
			Log.d(LOG_TAG, "attach called while still attached.... doing detach first");
			detach();
		}
		
		msu_act = act;
		msu_pv = (ViewGroup) msu_act.findViewById(msu_pid);
		if	(msu_pv != null) {
			msu_pv.addView(msu_mainview);
		} else {
			Log.d(LOG_TAG, "parentview not found!");
		}
	}
	
	public void detach() {
		Log.d(LOG_TAG, "detach called");
		if	(msu_act != null) {
			if	(msu_pv != null) {
				msu_pv.removeView(msu_mainview);
				msu_pv = null;
			} else {
				Log.d(LOG_TAG, "no parentview for detach");
			}
			msu_act = null;
		}
	}

}
