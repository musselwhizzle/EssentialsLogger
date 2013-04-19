package com.therealjoshua.essentialsloggersample1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.therealjoshua.essentials.logger.Log;

/**
 * This example shows how to change the log levels. You can use this to set your logs to say 
 * Log.DEBUG during development and then dynamically set it to Log.WARN during production.  
 */
public class Example2 extends Activity {
	
	private static final String TAG = "MainActivity";
	private int clickCount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		setContentView(ll);
		
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
				Log.v(TAG, "VERBOSE");
				Log.d(TAG, "DEBUG");
				Log.i(TAG, "INFO");
				Log.w(TAG, "WARN", tr);
				Log.e(TAG, "ERROR", tr);
			}
		});
		ll.addView(btn);
		
		
		btn = new Button(this);
		btn.setText("Set Log Level Log.VERBOSE");
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickCount = (clickCount + 1) % 3;
				Button btn = ((Button)v);
				switch (clickCount) {
					case 0: Log.setLogLevel(Log.VERBOSE); 
						btn.setText("Set Log Level Log.VERBOSE");
						break;
					case 1: Log.setLogLevel(Log.WARN); 
						btn.setText("Set Log Level Log.WARN");
						break;
					case 2: Log.setLogLevel(Log.SILENT); 
						btn.setText("Set Log Level Log.SILENT");
						break;
				}
			}
		});
		ll.addView(btn);
	}
}
