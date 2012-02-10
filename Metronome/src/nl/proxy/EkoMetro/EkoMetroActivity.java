package nl.proxy.EkoMetro;

import nl.proxy.EkoMetro.Ctrl.Manager;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class EkoMetroActivity extends Activity {
	private final String LOG_TAG = "EkoMetroActivity";
	
	/** StatusClass, containing application data **/
	private Manager emaEms;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(LOG_TAG, "onCreate");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		emaEms = Manager.getCurrent();
	}

	@Override
	public void onRestart() {
		Log.d(LOG_TAG, "onRestart");
		super.onRestart();
	}

	@Override
	public void onStart() {
		Log.d(LOG_TAG, "onStart");

		emaEms.setActivity(this);
		
		super.onStart();
	}

	@Override
	public void onResume() {
		Log.d(LOG_TAG, "onResume");
		super.onResume();
		emaEms.restoreSettings();
	}

	@Override
	public void onPause() {
		Log.d(LOG_TAG, "onPause");
		super.onPause();
		emaEms.saveSettings(this);
	}

	@Override
	public void onStop() {
		Log.d(LOG_TAG, "onStop");

		emaEms.clearActivity();

		super.onStop();
	}

	public void onDestroy() {
		Log.d(LOG_TAG, "onDestroy");
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		emaEms.setMute(true);
		super.onBackPressed();
	}
}
