package nl.proxy.EkoMetro.Worker;

import nl.proxy.EkoMetro.R;
import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.Toast;

// Based on: http://www.anddev.org/viewtopic.php?p=11352

public class MetroSound {
	public static final int SOUND_BASE = 1;
	public static final int SOUND_FLAT = 2;
	private static final int SOUND_MAXNUM = SOUND_FLAT;

	private SoundPool soundPool;
	private int soundPoolMap[] = new int[SOUND_MAXNUM+1];
	private Activity context;
	
	public MetroSound(Activity act) {
		context=act;
		initSounds();
	}
	
	public void detach() {
		context = null;
	}
	
	public void attach(Activity act) {
		context = act;
	}

	private void initSounds() {
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		// soundPoolMap = new HashMap<Integer, Integer>();
		
		soundPoolMap[SOUND_BASE] = soundPool.load(context, R.raw.plop, 1);
		soundPoolMap[SOUND_FLAT] = soundPool.load(context, R.raw.connect, 1);
	}

	public void playSound(int sound) {
		if	((sound < 1) || (sound > SOUND_MAXNUM)) {
			if	(context != null) {
				Toast.makeText(context, "sound out of bound: "+sound, Toast.LENGTH_SHORT).show();
			}
			return;
		}
		
		soundPool.play(soundPoolMap[sound], 1f, 1f, 1, 0, 1f);
	}

	public void doBeat(int beat) {
		if	(beat == 1) {
			playSound(SOUND_BASE);
		} else {
			playSound(SOUND_FLAT);
		}
	}
}
