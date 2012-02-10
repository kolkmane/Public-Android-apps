package nl.proxy.EkoMetro.UI;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import nl.proxy.EkoMetro.R;

public class MetroControl extends MetroSubUI implements OnClickListener {
	private boolean mcPaused = true;
	private View mcPauseBtn;
	private View mcPlayBtn;
	
	public MetroControl(Activity act, int pid) {
		super(act, pid, R.layout.control);
		LOG_TAG="MetroControl";
				
		mcPauseBtn = act.findViewById(R.id.ibPause);
		mcPlayBtn = act.findViewById(R.id.ibPlay);

		mcPauseBtn.setOnClickListener(this);
		mcPlayBtn.setOnClickListener(this);
		setPaused(true);
	}
	
	public boolean getPaused() {
		return mcPaused;
	}
	
	public void setPaused(boolean pause) {
		mcPaused = pause;
		mcPauseBtn.setClickable(!mcPaused);
		// mcPauseBtn.setVisibility(mcPaused?View.INVISIBLE:View.VISIBLE);
		mcPlayBtn.setClickable(mcPaused);
		// mcPlayBtn.setVisibility(mcPaused?View.VISIBLE:View.INVISIBLE);
	}

	public void onClick(View view) {
		String tag = view.getTag().toString();
		if	(tag.contentEquals("pause")) {
			Log.d(LOG_TAG, "Paused");
			setPaused(true);
		}
		if	(tag.contentEquals("play")) {
			Log.d(LOG_TAG, "play");
			setPaused(false);
		}
	}
}
