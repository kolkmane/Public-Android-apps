package nl.proxy.Eko;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

public class GraphCCGraphics extends SurfaceView implements SurfaceHolder.Callback {
    private Activity GCCG_Activity;
	private ViewGroup GCCG_ParentView;
	private int GCCG_Pid;
	private Paint GCCG_BlackPaint;
	private Paint GCCG_WhitePaint;
	private final int GCCS_MAX_PREVS = 10;
	private int GCCS_curIdx = 0;
	private long GCCS_prevTimes[] = new long[GCCS_MAX_PREVS];
	
	public GraphCCGraphics(Activity act, int pid) {
		super(act);
		// TODO Auto-generated constructor stub
        GCCG_Pid = pid;
        attach(act);
        
        GCCG_BlackPaint = new Paint();
        GCCG_BlackPaint.setColor(Color.BLACK);
		GCCG_BlackPaint.setAntiAlias(true);
		GCCG_BlackPaint.setTextSize(26);
		GCCG_BlackPaint.setStyle(Paint.Style.FILL);
		
		GCCG_WhitePaint = new Paint(GCCG_BlackPaint);
		GCCG_WhitePaint.setColor(Color.WHITE);
		
        getHolder().addCallback(this);
	}

	public void attach(Activity act) {
		if	(GCCG_Activity != null) {
			detach();
		}
        GCCG_Activity = act;
        GCCG_ParentView = (ViewGroup)act.findViewById(GCCG_Pid);
        GCCG_ParentView.addView(this);
	}
	
	public void detach() {
		if	(GCCG_ParentView != null) {
			GCCG_ParentView.removeView(this);
		}
		GCCG_Activity = null;
		GCCG_ParentView = null;
	}
	
	public void doUpdate() {
		SurfaceHolder sh = getHolder();
        Canvas c = null;

        try {
        	c = sh.lockCanvas(null);
        	if	(c != null) {
        		// We have a canvas, so we can continue.
        		synchronized (sh) {
        			onDraw(c);
        		}
        	}
        } finally {
        	// do this in a finally so that if an exception is thrown
        	// during the above, we don't leave the Surface in an
        	// inconsistent state
        	if (c != null) {
        		sh.unlockCanvasAndPost(c);
        	}
        }
	}
	
	/*
	 * SurfaceView overriden methods
	 */
	
	@Override
	public void onDraw(Canvas cvs) {
		float width = cvs.getWidth();
		float height = cvs.getHeight();
		int a;
		float x,y,prevx,prevy;
		long totalTime;
		int oldidx;
		long currTime = System.currentTimeMillis();		

		prevx = prevy = 0f;
		cvs.drawPaint(GCCG_WhitePaint);
		for (a=0; a <= 360; a+=10) {
			x=(width * a)/360f;
			y=(float)((Math.sin(Math.PI*((currTime/10) + a)/180)+1)*(height/2));
			if	(x==0) {
				cvs.drawPoint(x,y,GCCG_BlackPaint);
			} else {
				cvs.drawLine(prevx,prevy,x,y,GCCG_BlackPaint);
			}
			prevx = x;
			prevy = y;
		}
		
		oldidx = (GCCS_curIdx + 1) % GCCS_MAX_PREVS;
		totalTime = System.currentTimeMillis() - GCCS_prevTimes[oldidx];
		GCCS_prevTimes[GCCS_curIdx] = currTime;
		GCCS_curIdx = oldidx;
		
		if	(totalTime > 0) {
			cvs.drawText((GCCS_MAX_PREVS*1000f)/totalTime+" fps", 0, height-40, GCCG_BlackPaint);
		}
		
		cvs.drawText("dim:"+(int)width+"x"+(int)height, 0, height-20, GCCG_BlackPaint);
		
		cvs.drawText(currTime+"", 0, height, GCCG_BlackPaint);
	}
	
	/*
	 * SurfaceHolder.Callback methods
	 * @see android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder, int, int, int)
	 */
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		doUpdate();
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		doUpdate();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}
}
