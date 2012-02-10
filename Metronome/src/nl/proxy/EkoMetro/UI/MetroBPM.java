package nl.proxy.EkoMetro.UI;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import nl.proxy.EkoMetro.R;
import nl.proxy.EkoMetro.Ctrl.Manager;

public class MetroBPM extends MetroSubUI implements OnClickListener {
	private final String MBPM_TAG = "MetroBPM";
	public static final int MBPM_MINVALUE = 30;
	public static final int MBPM_MAXVALUE = 500;
	public static final int MBPM_DEFVALUE = 120;

	private int mbpm_BPM = -1;
	private TextView mbpm_tvBPM;
	
	private Manager mbpm_Mgr;
	
	public MetroBPM (Activity act, int pid) {
		super(act, pid, R.layout.bpmselector);
		Log.d(MBPM_TAG, "Constructor called");
		super.LOG_TAG = MBPM_TAG+".super";
		
		mbpm_tvBPM = (TextView)act.findViewById(R.id.tv_bpm);
		setValue(MBPM_DEFVALUE);
		act.findViewById(R.id.ibup_1).setOnClickListener(this);
		act.findViewById(R.id.ibup_10).setOnClickListener(this);
		act.findViewById(R.id.ibup_100).setOnClickListener(this);
		act.findViewById(R.id.ibdown_1).setOnClickListener(this);
		act.findViewById(R.id.ibdown_10).setOnClickListener(this);
		act.findViewById(R.id.ibdown_100).setOnClickListener(this);
	}
	
	public void setManager(Manager mgr) {
		mbpm_Mgr = mgr;
	}
	
	public int setValue(int newval) {
		int oldBPM=mbpm_BPM;
		if	(newval < MBPM_MINVALUE) {
			mbpm_BPM=MBPM_MINVALUE;
			mbpm_tvBPM.setText(""+mbpm_BPM);
		} else if (newval > MBPM_MAXVALUE) {
			mbpm_BPM = MBPM_MAXVALUE; 
			mbpm_tvBPM.setText(""+mbpm_BPM);
		} else if (newval != mbpm_BPM) {
			mbpm_BPM = newval;
			mbpm_tvBPM.setText(""+mbpm_BPM);
		}
		
		if	((oldBPM != mbpm_BPM) && (mbpm_Mgr != null)) {
			mbpm_Mgr.doUpdate();
		}
		return mbpm_BPM;
	}
	
	public int getValue() {
		return mbpm_BPM;
	}

	//@Override
	public void onClick(View btn) {
		String tag = btn.getTag().toString();
		if	(tag.length() > 0) {
			try {
				Long delta = Long.valueOf(tag);
				setValue((int)(mbpm_BPM + delta));
			} catch (NumberFormatException e) {
				Toast.makeText(getActivity(),"Wrong button TAG for this method!",Toast.LENGTH_SHORT).show();
			}
		}
	}
}
