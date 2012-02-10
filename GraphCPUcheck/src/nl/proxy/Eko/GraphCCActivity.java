package nl.proxy.Eko;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;

public class GraphCCActivity extends Activity implements SeekBar.OnSeekBarChangeListener {
	private GraphCCGraphics GCCA_graph;
	private GraphCCScheduler GCCA_sched;
	
	private int GCCA_fps = 60;
	private boolean GCCA_unlim = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        GCCA_graph = new GraphCCGraphics(this, R.id.GraphParent);
        
        ((SeekBar)findViewById(R.id.CtrlSpeed)).setOnSeekBarChangeListener(this);
    }
    
    @Override
    public void onStop() {
    	super.onStop();
    	GCCA_sched.pause();
    	//GCCA_graph.detach();
    }
    
    @Override
    public void onStart() {
    	super.onResume();
        
        GCCA_sched = GraphCCScheduler.getCurrent();
        GCCA_sched.setGraphics(GCCA_graph);
        
        GCCA_fps = ((SeekBar)findViewById(R.id.CtrlSpeed)).getProgress();
        onCheck(findViewById(R.id.CtrlUnlimited));
        GCCA_sched.setFPS(GCCA_fps);

    	//GCCA_graph.attach(this);
    }
    
    public void onCheck(View view) {
    	GCCA_unlim = ((CheckBox)view).isChecked();
    	if	(GCCA_unlim) {
    		GCCA_sched.setFPS(0);
    	} else {
    		GCCA_sched.setFPS(GCCA_fps);
    	}
    }

	@Override
	public void onProgressChanged(SeekBar view, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
    	GCCA_fps = view.getProgress();
    	if (GCCA_fps == 0) {
    		GCCA_fps=1;
    	}
    	if	(!GCCA_unlim) {
    		GCCA_sched.setFPS(GCCA_fps);
    	}
	
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}
}