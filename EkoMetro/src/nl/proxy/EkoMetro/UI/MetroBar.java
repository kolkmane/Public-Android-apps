package nl.proxy.EkoMetro.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/*
 * MetroBar is the class for showing the music-bar and notes during
 * the usage of the metronome, though a lot of the methods and ideas might
 * also be usefull for a score program.
 * 
 * The code for using the thread etc. is copied from the net.
 */
public class MetroBar extends SurfaceView implements SurfaceHolder.Callback {
	private final String MB_TAG = "MetroBar";
	private int mbCurrBeat = 1;
	private int mbNPM = 0;
	private int mbTPN = 2;
	
	private Paint mbBlackPaint;
	private Paint mbWhitePaint;
	
	private RectF mbOuter;
	private RectF mbInner;
	
	private float flagarr[] = null;
	
	public MetroBar(Context context) {
		super(context);
		
		Log.d(MB_TAG, "Constructor called");
		
		mbBlackPaint = new Paint();
		mbBlackPaint.setColor(Color.BLACK);
		mbBlackPaint.setAntiAlias(true);
		mbBlackPaint.setTextSize(26);
		mbBlackPaint.setStyle(Paint.Style.FILL);
		
		mbWhitePaint = new Paint(mbBlackPaint);
		mbWhitePaint.setColor(Color.WHITE);

		getHolder().addCallback(this);
	}
	
	/*
	 * Set the number of Notes-Per-Measure
	 */
	public void setNPM(int npm) {
		mbNPM = npm;
	}
	
	/*
	 * Set the Time-Per-Note (1/tpn == type of note)
	 */
	public void setTPN(int tpn) {
		mbTPN = tpn;
	}
	
	/*
	 * showBeat tells what the current beatNum is in a measure.
	 * This controls up until what note the bar should be drawn.
	 */
	public void showBeat(int beatNum) {
		mbCurrBeat = beatNum;
		doUpdate();
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
	
	@Override
	public void onDraw(Canvas canvas) {
		float width = getWidth();
		float height = getHeight();
		int drb;
		
		canvas.drawColor(Color.WHITE);
		
		for (drb=0; (drb<mbCurrBeat) && (drb < mbNPM); drb++) {
			canvas.save(Canvas.MATRIX_SAVE_FLAG);
			canvas.translate((width*(0.25f+((0.6f/mbNPM)*drb))), ((drb == 0 ? 0.575f : 0.425f)*height));
			canvas.drawOval(mbOuter,mbBlackPaint);
			if	((mbTPN == 2) || (mbTPN == 1)) {
				 canvas.drawOval(mbInner,mbWhitePaint);
			}
			if	(mbTPN > 1) {
			   // canvas.drawLine((float)(width*0.05),(float)0.075*height,(float)(width*0.05),(float)-0.35*height,mbBlackPaint);
			   canvas.drawLine(0.0f,0.075f*height,0.0f,-0.35f*height,mbBlackPaint);
			   canvas.drawLine(-1f,0.075f*height,-1f,-0.35f*height,mbBlackPaint);
			}
			if	(mbTPN == 8) {
				// Single Flag
				canvas.save(Canvas.MATRIX_SAVE_FLAG);
				canvas.translate(0.0f, -0.35f*height);
				drawFlag(canvas, mbOuter.right-mbOuter.left, 0.1f*height);
				canvas.restore();
			}
			if	(mbTPN == 16) {
				// Double Flag
				canvas.save(Canvas.MATRIX_SAVE_FLAG);
				canvas.translate(0.0f, -0.35f*height);
				drawFlag(canvas, mbOuter.right-mbOuter.left, 0.1f*height);
				canvas.restore();
				canvas.save(Canvas.MATRIX_SAVE_FLAG);
				canvas.translate(0.0f, -0.25f*height);
				drawFlag(canvas, mbOuter.right-mbOuter.left, 0.1f*height);
				canvas.restore();
			}
			canvas.restore();
		}
		canvas.drawLine(0.01f*width, 0.20f*height, 0.99f*width, 0.20f*height, mbBlackPaint);
		canvas.drawLine(0.01f*width, 0.35f*height, 0.99f*width, 0.35f*height, mbBlackPaint);
		canvas.drawLine(0.01f*width, 0.50f*height, 0.99f*width, 0.50f*height, mbBlackPaint);
		canvas.drawLine(0.01f*width, 0.65f*height, 0.99f*width, 0.65f*height, mbBlackPaint);
		canvas.drawLine(0.01f*width, 0.80f*height, 0.99f*width, 0.80f*height, mbBlackPaint);
		canvas.drawText(""+mbNPM,0.1f*width,0.48f*height,mbBlackPaint);
		canvas.drawText(""+mbTPN,0.1f*width,0.78f*height,mbBlackPaint);
	}
	
	public void drawFlag(Canvas canvas, float flWidth, float flHeight) {
		int x;
		int idx = 0;
		
		if	(flagarr == null) {
			flagarr = new float[4*(int)(1+flWidth)];
			for (x=0; x<flWidth; x++) {
				float factor = x / flWidth;
				float curve = (float)(Math.sin(((factor*2))*Math.PI) * (0-flHeight/2));
				float up_y = factor * (flHeight / 2f);
				float down_y = flHeight - up_y;
				canvas.drawLine(x, up_y+curve, x, down_y+curve, mbBlackPaint);
				flagarr[idx++] = x;
				flagarr[idx++] = up_y+curve;
				flagarr[idx++] = x;
				flagarr[idx++] = down_y+curve;
			}
		}
		else {
			canvas.drawLines(flagarr, mbBlackPaint);
		}
	}

	//@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		doUpdate();
	}

	//@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		float width = getWidth();
		float height = getHeight();
		float ovwidth = width*0.05f;
		float ovheight = height*0.15f;
		
		while (ovwidth / ovheight > 2) {
			ovwidth /= 1.1;
		}
		
		mbOuter = new RectF(-ovwidth,0f,0f,ovheight);
		mbInner = new RectF(ovwidth * -0.87f,ovheight * 0.13f,ovwidth * -0.13f,ovheight * 0.87f);
		doUpdate();
	}

	//@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
        // No more own thread, nothing to do for now.
	}
}
