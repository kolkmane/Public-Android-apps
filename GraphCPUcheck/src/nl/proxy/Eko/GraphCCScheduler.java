package nl.proxy.Eko;

import android.util.Log;

public class GraphCCScheduler extends Thread {
	private static final boolean GCCS_Debug = false;
	private static GraphCCScheduler currentGCCS = null;
	private int GCCS_fps;
	private int GCCS_sleeptime;
	private GraphCCGraphics GCCS_graph;
	private boolean GCCS_keepRunning = true;

	private GraphCCScheduler() {
		super("GCCScheduler");
		this.start();
		if (GCCS_Debug) Log.d("GraphCCScheduler", "Constructed");
	}

	public static GraphCCScheduler getCurrent() {
		if (currentGCCS == null) {
			currentGCCS = new GraphCCScheduler();
		}
		// currentGCCS.start();
		return currentGCCS;
	}

	public void setGraphics(GraphCCGraphics graph) {
		GCCS_graph = graph;
	}

	public void setFPS(int fps) {
		GCCS_fps = fps;
		if (fps > 0) {
			GCCS_sleeptime = 1000 / fps;
		}
	}

	public void unpause() {
		GCCS_keepRunning = true;
	}

	public void pause() {
		boolean retry = true;
		GCCS_keepRunning = false;

		while (retry) {
			try {
				join();
				retry = false;
			} catch (InterruptedException e) {
				// we will try it again and again...
			}
		}

		currentGCCS = null;
	}

	@Override
	public void run() {
		long currtime;
		long nexttime;

		while (GCCS_keepRunning) {
			currtime = System.currentTimeMillis();
			if (GCCS_graph != null) {
				GCCS_graph.doUpdate();
			}
			if (GCCS_fps > 0) {
				try {
					nexttime = currtime + GCCS_sleeptime;
					currtime = System.currentTimeMillis();
					if (currtime < nexttime) {
						sleep(nexttime - currtime, 0);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
