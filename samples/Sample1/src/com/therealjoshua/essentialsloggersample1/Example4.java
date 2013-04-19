package com.therealjoshua.essentialsloggersample1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.therealjoshua.essentials.logger.AsyncLogger;
import com.therealjoshua.essentials.logger.DatabaseLogger;
import com.therealjoshua.essentials.logger.FileLogger;
import com.therealjoshua.essentials.logger.Log;
import com.therealjoshua.essentials.logger.Logger;
/**
 * This example shows how to add other types of loggers
 */
public class Example4 extends Activity {
	
	private static final String TAG = "MainActivity";
	private boolean hasLogger = false;
	
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
		btn.setText("Add Loggers");
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (hasLogger) {
					Log.d(TAG, "You already have these loggers");
				} else {
					Logger fileLogger = new FileLogger(Example4.this.getApplicationContext());
					fileLogger.setLogLevel(Log.WARN);
					Logger databaseLogger = new DatabaseLogger(Example4.this.getApplicationContext());
					databaseLogger.setLogLevel(Log.ERROR);
					
					Log.addLogger(new AsyncLogger(fileLogger));
					Log.addLogger(new AsyncLogger(databaseLogger));
					hasLogger = true;
				}
			}
		});
		ll.addView(btn);
	}
}
