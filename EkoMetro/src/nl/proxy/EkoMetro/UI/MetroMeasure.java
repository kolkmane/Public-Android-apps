package nl.proxy.EkoMetro.UI;

import nl.proxy.EkoMetro.R;
import nl.proxy.EkoMetro.Ctrl.Manager;
import android.app.Activity;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MetroMeasure extends MetroSubUI implements RadioGroup.OnCheckedChangeListener {
	private final String MM_TAG = "MetroMeasure";
	private RadioGroup mmMainView;
	private int mmNPM;
	private int mmTPN;
	private Manager mmMgr = null;
	
	public MetroMeasure(Activity act,int pid) {
		super(act,pid,R.layout.measure_selector);
		Log.d(MM_TAG, "Constructor called");
		super.LOG_TAG = MM_TAG+".super";
		mmMainView = (RadioGroup)act.findViewById(R.id.radMeasure);
		mmMainView.setOnCheckedChangeListener(this);
	}
	
	public int getNPM() {
		return mmNPM;
	}
	
	public int getTPN() {
		return mmTPN;
	}
	
	public void setManager(Manager mgr) {
		mmMgr = mgr;
	}
	
	public void radSelect(int npm, int tpn) {
		mmNPM=npm;
		mmTPN=tpn;
    	int idx;
    	RadioButton rb;
    	
    	for (idx = 1; (rb=(RadioButton)mmMainView.getChildAt(idx))!=null; idx++) {
    		if	(rb.getText().toString().contentEquals(npm+" / "+tpn)) {
    			rb.setChecked(true);
    		} else {
    			rb.setChecked(false);
    		}
    	}
	}

	public void onCheckedChanged(RadioGroup group, int index) {
		Log.d(MM_TAG, "onCheckedChanged called");
		if	(group == mmMainView) {
			if	(index == -1) {
				index = group.getCheckedRadioButtonId();
			}
	    	RadioButton checked = (RadioButton) getActivity().findViewById(index);
	    	String label = checked.getText().toString();
			Log.d(MM_TAG,"checked: "+label);
	    	String parts[] = label.split(" / ");
	    	mmNPM = Long.valueOf(parts[0]).intValue();
	    	mmTPN = Long.valueOf(parts[1]).intValue();
	    	
	    	if	(mmMgr != null) {
	    		mmMgr.doUpdate();
	    	}
		}
	}
}
