package com.therealjoshua.essentialsloggersample1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.therealjoshua.essentials.logger.Log;

/**
 * This example shows how to set Log levels but a certain tag
 */
public class Example3 extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		setContentView(ll);
		
		Log.setLogLevel("set1", Log.ERROR);
		Log.setLogLevel("set2", Log.WARN);
		Log.setLogLevel("set3", Log.SILENT);
		
		Button btn = new Button(this);
		btn.setText("Generate Logs");
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Throwable tr = null;
				try {
					throw new IllegalArgumentException("Testing");
				} catch (Exception e) {
					tr = e;
				}
				Log.v("set1", "VERBOSE");
				Log.d("set1", "DEBUG");
				Log.i("set1", "INFO");
				Log.w("set1", "WARN", tr);
				Log.e("set1", "ERROR", tr);
				
				Log.v("set2", "VERBOSE");
				Log.d("set2", "DEBUG");
				Log.i("set2", "INFO");
				Log.w("set2", "WARN", tr);
				Log.e("set2", "ERROR", tr);
				
				Log.v("set3", "VERBOSE");
				Log.d("set3", "DEBUG");
				Log.i("set3", "INFO");
				Log.w("set3", "WARN", tr);
				Log.e("set3", "ERROR", tr);
			}
		});
		ll.addView(btn);
		
		
		btn = new Button(this);
		btn.setText("Set Log Levels by tag");
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.setLogLevel("set1", Log.WARN);
				Log.setLogLevel("set2", Log.ERROR);
				Log.setLogLevel("set3", Log.SILENT);
			}
		});
		ll.addView(btn);
	}
}
