package nl.sense.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class SC_GraphView extends Activity {
	private final Handler mHandler = new Handler();
	private Runnable mTimer1;
	private GraphView graphView;
	private GraphViewSeries exampleSeries1;
		private double graph2LastXValue = 5d;

	private double getRandom() {
		double high = 120;
		double low = 30;
		return Math.random() * (high - low) + low;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sense_layout);

		// graph with dynamically genereated horizontal and vertical labels
	//	if (getIntent().getStringExtra("type").equals("bar")) {
			graphView = new LineGraphView(
					this // context
					, "Getting data..." // heading
			);
/*		} else {
			graphView = new LineGraphView(
					this // context
					, "GraphViewDemo" // heading
			);
		}*/


		// ----------
		exampleSeries1 = new GraphViewSeries(new GraphViewData[] {
				new GraphViewData(1, 1.0d)
				, new GraphViewData(2, 2.0d)
				, new GraphViewData(3, 3.0d)
				, new GraphViewData(4, 4.0d)
				, new GraphViewData(5, 5.0d)
		});

		// graph with custom labels and drawBackground
		//if (getIntent().getStringExtra("type").equals("bar")) {
			graphView = new LineGraphView(
					this
					, "Noise levels in dB(A)"
			);
		/*} else {
			graphView = new LineGraphView(
					this
					, "GraphViewDemo"
			);
			((LineGraphView) graphView).setDrawBackground(true);
		}*/
		graphView.addSeries(exampleSeries1); // data
		graphView.setViewPort(1, 4);
		graphView.setScalable(true);

		LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
		layout.addView(graphView);
	}

	@Override
	protected void onPause() {
		mHandler.removeCallbacks(mTimer1);
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();

		mTimer1 = new Runnable() {
			@Override
			public void run() {
				graph2LastXValue += 1d;
				exampleSeries1.appendData(new GraphViewData(graph2LastXValue, getRandom()), true);
				mHandler.postDelayed(this, 600);
			}
		};
		mHandler.postDelayed(mTimer1, 600);
	}
}
