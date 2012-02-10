package nl.proxy.EkoMetro.UI;

import nl.proxy.EkoMetro.R;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import android.widget.SeekBar;

public class MetroVolume extends MetroSubUI implements SeekBar.OnSeekBarChangeListener {
	private AudioManager mvAM;
	private int mvMaxVol;
	private SeekBar mvVolBar;

	private String LOG_TAG = "MetroVolume";

	public MetroVolume(Activity act, int pid) {
		super(act, pid, R.layout.volselector);
		Log.d(LOG_TAG, "Constructor called");
		super.LOG_TAG = this.LOG_TAG+".super";

		mvVolBar = (SeekBar)act.findViewById(R.id.volBar);
		mvAM = (AudioManager) act.getSystemService(Context.AUDIO_SERVICE);
		mvMaxVol = mvAM.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		reset();
		mvVolBar.setOnSeekBarChangeListener(this);
	}
	
	@Override
	public void attach(Activity act) {
		super.attach(act);
		reset();
	}
	
	// Set the progress bar to the current value
	// Needed for initial value and later, when restarting the application.
	public void reset() {
		if	(mvAM != null) {
			int l_currVol = mvAM.getStreamVolume(AudioManager.STREAM_MUSIC);
			mvVolBar.setProgress((l_currVol * 100) / mvMaxVol);
		}
	}
	
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		mvAM.setStreamVolume(AudioManager.STREAM_MUSIC, (mvMaxVol*progress)/100, 0);
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
		// Nothing intended
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		// Nothing intended
	}
}
